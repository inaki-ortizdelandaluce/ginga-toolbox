package org.ginga.toolbox.command;

import java.io.File;
import java.util.Comparator;

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
import org.ginga.toolbox.pipeline.TimingMode1NoCorrectionPipeline;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.TimeUtil;

public class TimingMode1ExtractorNoCorrectionCmd {

    protected final static String DATE_FORMAT_PATTERN = TimeUtil.DATE_FORMAT_INPUT.toPattern();
    private final static Logger log = Logger.getLogger(TimingMode1ExtractorNoCorrectionCmd.class);

    public static void main(String[] args) {
        try {
            CommandLine commandLine = new BasicParser().parse(getOptions(), args);
            InputScanner scanner = new InputScanner();
            // read command line argument values
            // TARGET
            String target = null;
            if (commandLine.hasOption("t")) {
                target = commandLine.getOptionValue("t");
            } else {
                target = scanner.scanTarget();
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
            // build single mode target observation instance from arguments
            LacModeTargetObservation obs = new LacModeTargetObservation();
            obs.setTarget(target);
            obs.setMode(mode);
            obs.setStartTime(startTime);
            obs.setEndTime(endTime);
            // extract timing
            extractTimingNoCorrection(obs);
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
        Option targetOption = OptionBuilder.withArgName("target").withLongOpt("target").withDescription("[OPTIONAL] Target name").hasArg()
                .create("t");
        Option lacModeOption = OptionBuilder.withArgName("LAC mode").withLongOpt("mode")
                .withDescription("[OPTIONAL] LAC mode. Possible values: " + getLacModes()).hasArg().create("m");
        Option startTimeOption = OptionBuilder.withArgName("start time").withLongOpt("start-time")
                .withDescription("[OPTIONAL] Start time in " + DATE_FORMAT_PATTERN + " format").hasArg().create();
        Option endTimeOption = OptionBuilder.withArgName("end time").withLongOpt("end-time")
                .withDescription("[OPTIONAL] End time in " + DATE_FORMAT_PATTERN + " format").hasArg().create();
        OptionGroup group1 = new OptionGroup();
        group1.setRequired(true);
        group1.addOption(new Option("i", "interactive", false, "prompt for input values, e.g. LACDUMP elevation and rigidity constraints"));
        group1.addOption(new Option("s", "systematic", false,
                "use default systematic values present in configuration file gingatoolbox.properties "));

        options.addOption(targetOption);
        options.addOption(lacModeOption);
        options.addOption(startTimeOption);
        options.addOption(endTimeOption);

        options.addOptionGroup(group1);

        return options;
    }

    protected static String getLacModes() {
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

            private static final String OPTS_ORDER = "isetm"; // short option names

            @Override
            public int compare(Option o1, Option o2) {
                String argCharOption1 = o1.getLongOpt().substring(0, 1);
                String argCharOption2 = o2.getLongOpt().substring(0, 1);
                return OPTS_ORDER.indexOf(argCharOption1) - OPTS_ORDER.indexOf(argCharOption2);
            }
        });
        helpFormatter.printHelp("extract_timing_mode1_hayashida.sh", getOptions());
    }

    public static void extractTimingNoCorrection(LacModeTargetObservation obs) {
        TimingMode1NoCorrectionPipeline pipeline = new TimingMode1NoCorrectionPipeline();
        File timingFile = pipeline.run(obs);
        if (timingFile != null) {
            log.info("Timing file " + timingFile.getName() + " created successfully");
        }
    }
}