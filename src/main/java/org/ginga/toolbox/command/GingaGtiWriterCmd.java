package org.ginga.toolbox.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Comparator;
import java.util.List;

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
import org.ginga.toolbox.gti.GingaGtiWriter;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.TimeUtil;

public class GingaGtiWriterCmd {

    protected final static String DATE_FORMAT_PATTERN = TimeUtil.DATE_FORMAT_INPUT.toPattern();
    public static final Logger log = Logger.getLogger(TargetListPrinterCmd.class);
    private PrintWriter writer;

    public GingaGtiWriterCmd(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    public GingaGtiWriterCmd(PrintStream stream) {
        this.writer = new PrintWriter(stream);
    }

    public void writeGti(String target, String lacMode, String startTime, String endTime,
            boolean isBackground) throws LacdumpDaoException, IOException {
        // build and execute query
        DataReductionEnv env = GingaToolboxEnv.getInstance().getDataReductionEnv();
        LacdumpQuery query = new LacdumpQuery();
        query.setTargetName(target);
        query.setMode(Enum.valueOf(LacMode.class, lacMode));
        query.setStartTime(startTime);
        query.setEndTime(endTime);
        query.setMinCutOffRigidity(env.getCutOffRigidityMin());
        query.setMinElevation(env.getElevationMin());
        List<LacdumpSfEntity> sfList = new LacdumpDaoImpl().findSfList(query);
        // write results into Ginga GTI format
        GingaGtiWriter gtiWriter = new GingaGtiWriter();
        gtiWriter.write(target, sfList, isBackground, false, this.writer);
    }

    public static void main(String[] args) {
        try {
            CommandLine commandLine = new BasicParser().parse(getOptions(), args);
            InputScanner scanner = new InputScanner();
            // read command line argument values
            Writer writer = null;
            // TARGET
            String target = null;
            if (commandLine.hasOption("t")) {
                target = commandLine.getOptionValue("t");
            }
            // WRITER
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
            // LAC MODE
            String mode = null;
            if (commandLine.hasOption("m")) {
                mode = commandLine.getOptionValue("m");
                try {
                    Enum.valueOf(LacMode.class, commandLine.getOptionValue("m"));
                } catch (IllegalArgumentException e) {
                    log.error("Unknown background LAC mode " + commandLine.getOptionValue("m"));
                    printHelp();
                    System.exit(1);
                }
            } else {
                mode = scanner.scanLacMode();
            }
            // START TIME
            String startTime = null;
            if (commandLine.hasOption("start-time")) {
                try {
                    startTime = commandLine.getOptionValue("start-time");
                    TimeUtil.parseInputFormat(startTime);
                } catch (java.text.ParseException e) {
                    log.error("Start time format is not valid. " + e.getMessage());
                    printHelp();
                    System.exit(1);
                }
            } else {
                startTime = scanner.scanStartTime();
            }
            startTime = startTime.replace("T", " ");
            String endTime = null;
            // END TIME
            if (commandLine.hasOption("start-time")) {
                try {
                    endTime = commandLine.getOptionValue("end-time");
                    TimeUtil.parseInputFormat(endTime);
                } catch (java.text.ParseException e) {
                    log.error("End time format is not valid. " + e.getMessage());
                    printHelp();
                    System.exit(1);
                }
            } else {
                endTime = scanner.scanEndTime();
            }
            endTime = endTime.replace("T", " ");
            scanner.close();
            if (commandLine.hasOption("i")) { // set interactive mode
                GingaToolboxEnv.getInstance().setDataReductionMode(DataReductionMode.INTERACTIVE);
            }
            // write GTI
            GingaGtiWriterCmd cmd = new GingaGtiWriterCmd(writer);
            cmd.writeGti(target, mode, startTime, endTime, commandLine.hasOption("b"));
        } catch (ParseException e) {
            log.error(e.getMessage());
            printHelp();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("static-access")
    private static Options getOptions() {
        Options options = new Options();

        Option targetOption = OptionBuilder.withArgName("target").withLongOpt("target")
                .withDescription("[OPTIONAL] Target name.").hasArg().create("t");
        Option lacModeOption = OptionBuilder.withArgName("LAC mode").withLongOpt("mode")
                .withDescription("[OPTIONAL] LAC mode. Possible values: " + getLacModes()).hasArg()
                .create("m");
        Option startTimeOption = OptionBuilder.withArgName("start time").withLongOpt("start-time")
                .withDescription("[OPTIONAL] Start time in " + DATE_FORMAT_PATTERN + " format")
                .hasArg().create("a");
        Option endTimeOption = OptionBuilder.withArgName("end time").withLongOpt("end-time")
                .withDescription("[OPTIONAL] End time in " + DATE_FORMAT_PATTERN + " format")
                .hasArg().create("n");
        Option bgOption = OptionBuilder.withArgName("background").withLongOpt("is-background")
                .withDescription("[OPTIONAL] Background GTI.").hasArg().create("b");

        Option fileOption = OptionBuilder.withArgName("file path").withLongOpt("file")
                .withDescription("write observation list to output file").hasArg().create("f");
        Option consoleOption = new Option("c", "console", false,
                "write observation list to console");

        OptionGroup group1 = new OptionGroup();
        group1.setRequired(true);
        group1.addOption(fileOption);
        group1.addOption(consoleOption);

        OptionGroup group2 = new OptionGroup();
        group2.setRequired(true);
        group2.addOption(new Option("i", "interactive", false,
                "prompt for input values, e.g. LACDUMP elevation and rigidity constraints"));
        group2.addOption(new Option("s", "systematic", false,
                "use default systematic values present in configuration file gingatoolbox.properties "));

        options.addOption(targetOption);
        options.addOption(lacModeOption);
        options.addOption(startTimeOption);
        options.addOption(endTimeOption);
        options.addOption(bgOption);
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        return options;
    }

    private static String getLacModes() {
        String s = "";
        LacMode[] modes = LacMode.values();
        for (int i = 0; i < modes.length; i++) {
            s += modes[i].toString() + ", ";
        }
        return s.substring(0, s.length() - 2);
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(new Comparator<Option>() {

            private static final String OPTS_ORDER = "fcistmban"; // short option names

            @Override
            public int compare(Option o1, Option o2) {
                return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
            }
        });
        helpFormatter.printHelp("write_ginga_gti.sh", getOptions());
    }
}
