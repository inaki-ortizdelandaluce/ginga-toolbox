package org.ginga.toolbox.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv.DataReductionMode;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.pipeline.ObservationListBuilder;
import org.ginga.toolbox.util.Constants.LacMode;

public class ObservationListPrinterCmd {

    private PrintWriter writer;
    public static final Logger log = Logger.getLogger(TargetListPrinterCmd.class);

    public ObservationListPrinterCmd(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    public ObservationListPrinterCmd(PrintStream stream) {
        this.writer = new PrintWriter(stream);
    }

    public void printAllModes(String target) {

        ObservationListBuilder obsListBuilder = new ObservationListBuilder();
        obsListBuilder.setStarts(Arrays.asList(target));

        Map<ObservationEntity, List<LacModeTargetObservation>> obsMap = obsListBuilder.next();
        Iterator<ObservationEntity> obsIterator = obsMap.keySet().iterator();
        ObservationEntity obsEntity = null;

        this.writer.println(String.format("%6s%10s%6s%10s%20s%20s", "OBSID", "PASS", "MODE", "BIT_RATE", "FIRST_SF_TIME", "LAST_SF_TIME"));
        this.writer.println(String.format("%72s", "========================================================================"));

        while (obsIterator.hasNext()) {
            obsEntity = obsIterator.next();
            for (LacModeTargetObservation targetObservation : obsMap.get(obsEntity)) {
                this.writer.println(String.format("%6s%10s%6s%10s%20s%20s", obsEntity.getId(), obsEntity.getSequenceNumber(),
                        targetObservation.getMode(), targetObservation.getBitRatesAsString(), targetObservation.getStartTime(),
                        targetObservation.getEndTime()));
            }
            this.writer.println(String.format("%72s", "------------------------------------------------------------------------"));
        }
        this.writer.flush();
        this.writer.close();
    }

    public void printSpectralModes(String target) {
        printModes(target, new LacMode[] { LacMode.MPC1, LacMode.MPC2 });
    }

    public void printTimingModes(String target) {
        printModes(target, new LacMode[] { LacMode.MPC2, LacMode.MPC3, LacMode.PC });
    }

    public void printModes(String target, LacMode[] modes) {
        ObservationListBuilder obsListBuilder = new ObservationListBuilder();
        obsListBuilder.setStarts(Arrays.asList(target));

        Map<ObservationEntity, List<LacModeTargetObservation>> obsMap = obsListBuilder.next();
        Iterator<ObservationEntity> obsIterator = obsMap.keySet().iterator();
        ObservationEntity obsEntity = null;

        this.writer.println(String.format("%6s%10s%6s%10s%20s%20s", "OBSID", "PASS", "MODE", "BIT_RATE", "FIRST_SF_TIME", "LAST_SF_TIME"));
        this.writer.println(String.format("%72s", "========================================================================"));

        while (obsIterator.hasNext()) {
            obsEntity = obsIterator.next();
            for (LacModeTargetObservation targetObservation : obsMap.get(obsEntity)) {
                LacMode mode = targetObservation.getLacMode();
                if (Arrays.asList(modes).contains(mode)) {
                    this.writer.println(String.format("%6s%10s%6s%10s%20s%20s", obsEntity.getId(), obsEntity.getSequenceNumber(),
                            targetObservation.getMode(), targetObservation.getBitRatesAsString(), targetObservation.getStartTime(),
                            targetObservation.getEndTime()));
                }
            }
        }
        this.writer.flush();
        this.writer.close();
    }

    public void printObservations(String target) {

        ObservationListBuilder obsListBuilder = new ObservationListBuilder();
        obsListBuilder.setStarts(Arrays.asList(target));

        Map<ObservationEntity, List<LacModeTargetObservation>> obsMap = obsListBuilder.next();
        Iterator<ObservationEntity> obsIterator = obsMap.keySet().iterator();
        ObservationEntity obsEntity = null;

        this.writer.println(String.format("%6s%10s%22s%22s", "OBSID", "PASS", "START_TIME", "END_TIME"));
        this.writer.println(String.format("%60s", "============================================================"));

        while (obsIterator.hasNext()) {
            obsEntity = obsIterator.next();
            this.writer.println(String.format("%6s%10s%22s%22s", obsEntity.getId(), obsEntity.getSequenceNumber(),
                    obsEntity.getStartTime(), obsEntity.getEndTime()));
        }
        this.writer.flush();
        this.writer.close();
    }

    public static void main(String[] args) {
        try {
            CommandLine commandLine = new BasicParser().parse(getOptions(), args);
            // read command line argument values
            Writer writer = null;
            String target = commandLine.getOptionValue("t");
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
            ObservationListPrinterCmd cmd = new ObservationListPrinterCmd(writer);
            if (commandLine.hasOption("o")) {
                cmd.printObservations(target);
            } else if (commandLine.hasOption("a")) {
                cmd.printAllModes(target);
            } else if (commandLine.hasOption("l")) {
                cmd.printSpectralModes(target);
            } else {
                cmd.printTimingModes(target);
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

        Option fileOption = OptionBuilder.withArgName("file path").withLongOpt("file").withDescription("write observation list to file")
                .hasArg().create("f");

        Option consoleOption = new Option("c", "console", false, "write observation list to console");

        OptionGroup group1 = new OptionGroup();
        group1.setRequired(true);
        group1.addOption(fileOption);
        group1.addOption(consoleOption);

        OptionGroup group2 = new OptionGroup();
        group2.setRequired(true);
        group2.addOption(new Option("o", "observations-only", false, "list observations only"));
        group2.addOption(new Option("a", "all-modes", false, "list all LAC modes"));
        group2.addOption(new Option("l", "spectral-modes-only", false, "list MPC1 and MPC2 LAC modes only"));
        group2.addOption(new Option("g", "timing-modes-only", false, "list MPC3 and PC modes only"));

        OptionGroup group3 = new OptionGroup();
        group3.setRequired(true);
        group3.addOption(new Option("i", "interactive", false, "prompt for input values, e.g. LACDUMP elevation and rigidity constraints"));
        group3.addOption(new Option("s", "systematic", false,
                "use default systematic values present in configuration file gingatoolbox.properties "));

        options.addOption(targetOption);
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
        helpFormatter.printHelp("print_observation_list.sh", getOptions());
    }
}
