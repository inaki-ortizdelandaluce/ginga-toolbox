package org.ginga.toolbox.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv.DataReductionMode;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.TimeUtil;

public class ObservationDetailsPrinterCmd {

    private PrintWriter writer;
    public static final Logger log = Logger.getLogger(TargetListPrinterCmd.class);

    public ObservationDetailsPrinterCmd(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    public ObservationDetailsPrinterCmd(PrintStream stream) {
        this.writer = new PrintWriter(stream);
    }

    public void printAllModes(String target, String obsid) throws IOException {
        printModes(target, obsid, new LacMode[] { LacMode.MPC1, LacMode.MPC2, LacMode.MPC3, LacMode.PC });
    }

    public void printSpectralModes(String target, String obsid) throws IOException {
        printModes(target, obsid, new LacMode[] { LacMode.MPC1, LacMode.MPC2, LacMode.MPC3 });
    }

    public void printTimingModes(String target, String obsid) throws IOException {
        printModes(target, obsid, new LacMode[] { LacMode.MPC2, LacMode.MPC3, LacMode.PC });
    }

    public void printModes(String target, String obsid, LacMode[] modes) throws IOException {
        // find observation form identifier
        ObservationDao obsDao = new ObservationDaoImpl();
        ObservationEntity obsEntity = null;
        try {
            obsEntity = obsDao.findById(Long.valueOf(obsid));
            log.info(obsid + " observation found");
        } catch (ObservationDaoException e) {
            log.error(obsid + " observation could not be found", e);
        }

        this.writer.println(String.format("%6s%10s%6s%10s%20s%20s", "OBSID", "PASS", "MODE", "BIT_RATE", "START_TIME", "END_TIME"));
        this.writer.println(String.format("%72s", "========================================================================"));

        for (int i = 0; i < modes.length; i++) {
            try {
                printMode(target, obsEntity, modes[i]);
            } catch (java.text.ParseException | LacdumpDaoException e) {
                throw new IOException(e);
            }
        }
        this.writer.flush();
        this.writer.close();
    }

    private void printMode(String target, ObservationEntity obsEntity, LacMode mode) throws java.text.ParseException, LacdumpDaoException {
        log.info("Observation " + obsEntity.getId() + "in " + mode.toString() + "mode ");
        // build and execute query
        DataReductionEnv env = GingaToolboxEnv.getInstance().getDataReductionEnv();
        LacdumpQuery query = new LacdumpQuery();
        query.setTargetName(target);
        query.setMode(mode);
        query.setStartTime(TimeUtil.DATE_FORMAT_DATABASE.format(obsEntity.getStartTime()));
        query.setEndTime(TimeUtil.DATE_FORMAT_DATABASE.format(obsEntity.getEndTime()));
        query.setMinCutOffRigidity(env.getCutOffRigidityMin());
        query.setMinElevation(env.getElevationMin());
        List<LacdumpSfEntity> sfList = new LacdumpDaoImpl().findSfList(query);
        // write results to writer
        Date startDateTime = null;
        LacdumpSfEntity lastSf = null;
        Set<String> bitRates = new HashSet<String>();
        for (LacdumpSfEntity sf : sfList) {
            log.info("\tSF " + sf.getPass() + ", " + sf.getSequenceNumber() + ", "
                    + TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, sf.getDate()));
            bitRates.add(sf.getBitRate());
            if (lastSf == null || !sf.getPass().equals(lastSf.getPass())) { // new PASS
                if (lastSf != null && lastSf.getPass() != null) {
                    this.writer.println(String.format("%6s%10s%6s%10s%20s%20s", obsEntity.getId(), obsEntity.getSequenceNumber(), mode,
                            getBitRatesAsString(bitRates), TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, startDateTime),
                            TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, getNextDate(lastSf)))); // end
                    log.info("END [NEW PASS]: " + TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, sf.getDate()));
                    // previous
                    bitRates.clear();
                }
                startDateTime = sf.getDate(); // begin
                log.info("START: " + TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, startDateTime));
            } else if (sf.getSequenceNumber() > lastSf.getSequenceNumber() + 1) {
                this.writer.println(String.format("%6s%10s%6s%10s%20s%20s", obsEntity.getId(), obsEntity.getSequenceNumber(), mode,
                        getBitRatesAsString(bitRates), TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, startDateTime),
                        TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, getNextDate(lastSf)))); // end
                log.info("END [GAP]: " + TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, sf.getDate()));
                // previous
                bitRates.clear();
                startDateTime = sf.getDate(); // begin
                log.info("START: " + TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, startDateTime));
            }
            lastSf = sf;
        }
        if (lastSf != null && lastSf.getSequenceNumber() > 0) {
            this.writer.println(String.format("%6s%10s%6s%10s%20s%20s", obsEntity.getId(), obsEntity.getSequenceNumber(), mode,
                    getBitRatesAsString(bitRates), TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, startDateTime),
                    TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, getNextDate(lastSf)))); // end
            // previous
            log.info("END [LAST]: " + TimeUtil.format(TimeUtil.DATE_FORMAT_INPUT, getNextDate(lastSf)));
        }
    }

    private Date getNextDate(LacdumpSfEntity sf) {
        switch (sf.getBitRate()) {
        case "L":
            return addSeconds(sf.getDate(), 32);
        case "M":
            return addSeconds(sf.getDate(), 8);
        case "H":
        default:
            return addSeconds(sf.getDate(), 4);
        }
    }

    private Date addSeconds(Date date, int seconds) {
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(date.getTime());
        calender.add(Calendar.SECOND, seconds);
        return calender.getTime();
    }

    private String getBitRatesAsString(Set<String> bitRates) {
        String s = "";
        for (Iterator<String> iterator = bitRates.iterator(); iterator.hasNext();) {
            s += iterator.next();
        }
        return s;
    }

    public static void main(String[] args) {
        try {
            CommandLine commandLine = new BasicParser().parse(getOptions(), args);
            // read command line argument values
            Writer writer = null;
            String target = commandLine.getOptionValue("t");
            String obsid = commandLine.getOptionValue("o");
            if (commandLine.hasOption("f")) {
                String filePath = commandLine.getOptionValue("f");
                File f = new File(filePath);
                // create parent directory if it does not exist
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                writer = new FileWriter(f);
            } else {
                writer = new PrintWriter(System.out);
            }
            if (commandLine.hasOption("i")) { // set interactive mode
                GingaToolboxEnv.getInstance().setDataReductionMode(DataReductionMode.INTERACTIVE);
            }
            // write target list
            ObservationDetailsPrinterCmd cmd = new ObservationDetailsPrinterCmd(writer);
            if (commandLine.hasOption("a")) {
                cmd.printAllModes(target, obsid);
            } else if (commandLine.hasOption("l")) {
                cmd.printSpectralModes(target, obsid);
            } else {
                cmd.printTimingModes(target, obsid);
            }
        } catch (ParseException e) {
            log.error(e.getMessage());
            printHelp();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("static-access")
    private static Options getOptions() {
        Options options = new Options();

        Option targetOption = OptionBuilder.withArgName("target").withLongOpt("target").isRequired().withDescription("Target name")
                .hasArg().create("t");

        Option observationOption = OptionBuilder.withArgName("observation").withLongOpt("observation").isRequired()
                .withDescription("Observation identifier").hasArg().create("o");

        Option fileOption = OptionBuilder.withArgName("file path").withLongOpt("file").withDescription("write observation list to file")
                .hasArg().create("f");

        Option consoleOption = new Option("c", "console", false, "write observation list to console");

        OptionGroup group1 = new OptionGroup();
        group1.setRequired(true);
        group1.addOption(fileOption);
        group1.addOption(consoleOption);

        OptionGroup group2 = new OptionGroup();
        group2.setRequired(true);
        group2.addOption(new Option("a", "all-modes", false, "list all LAC modes"));
        group2.addOption(new Option("l", "spectral-modes-only", false, "list MPC1 and MPC2 LAC modes only"));
        group2.addOption(new Option("g", "timing-modes-only", false, "list MPC3 and PC modes only"));

        OptionGroup group3 = new OptionGroup();
        group3.setRequired(true);
        group3.addOption(new Option("i", "interactive", false, "prompt for input values, e.g. LACDUMP elevation and rigidity constraints"));
        group3.addOption(new Option("s", "systematic", false,
                "use default systematic values present in configuration file gingatoolbox.properties "));

        options.addOption(targetOption);
        options.addOption(observationOption);
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        options.addOptionGroup(group3);
        return options;
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(new Comparator<Option>() {

            private static final String OPTS_ORDER = "toalgcfis"; // short option names

            @Override
            public int compare(Option o1, Option o2) {
                return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
            }
        });
        helpFormatter.printHelp("print_observation_details.sh", getOptions());
    }
}
