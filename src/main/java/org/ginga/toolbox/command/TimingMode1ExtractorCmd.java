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
import org.ginga.toolbox.environment.GingaToolboxEnv.InputMode;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.pipeline.TimingMode1HayashidaPipeline;
import org.ginga.toolbox.pipeline.TimingMode1SimplePipeline;
import org.ginga.toolbox.pipeline.TimingMode1SudSortPipeline;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.TimeUtil;

public class TimingMode1ExtractorCmd {

    protected final static String DATE_FORMAT_PATTERN = TimeUtil.DATE_FORMAT_INPUT.toPattern();
    private final static Logger log = Logger.getLogger(TimingMode1ExtractorCmd.class);

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
            // BACKGROUND SUBTRACTION METHOD
            BgSubtractionMethod method = null;
            if (commandLine.hasOption("b")) {
                try {
                    method = Enum.valueOf(BgSubtractionMethod.class,
                            commandLine.getOptionValue("b"));
                } catch (IllegalArgumentException e) {
                    log.error("Unknown background subtraction method "
                            + commandLine.getOptionValue("b"));
                    printHelp();
                    System.exit(1);
                }
            } else {
                method = scanner.scanBackgroundMethod();
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
            // OBSERVATION IDENTIFIER
            long obsId = 0;
            if (commandLine.hasOption("o")) {
                try {
                    obsId = Long.valueOf(commandLine.getOptionValue("o")).longValue();
                } catch (NumberFormatException e) {
                    log.error("Observation identifier " + commandLine.getOptionValue("o")
                            + " is not an integer");
                    printHelp();
                    System.exit(1);
                }
            } else {
                obsId = scanner.scanObsId();
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
                GingaToolboxEnv.getInstance().setInputParametersMode(InputMode.INTERACTIVE);
            }
            // build single mode target observation instance from arguments
            SingleModeTargetObservation obs = new SingleModeTargetObservation();
            obs.setObsId(obsId);
            obs.setTarget(target);
            obs.setMode(mode);
            obs.setStartTime(startTime);
            obs.setEndTime(endTime);
            // extract spectrum
            extractTiming(obs, method);
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
        Option obsIdOption = OptionBuilder.withArgName("observation id")
                .withLongOpt("observation-id").withDescription("[OPTIONAL] Observation identifier")
                .hasArg().create("o");
        Option targetOption = OptionBuilder.withArgName("target").withLongOpt("target")
                .withDescription("[OPTIONAL] Target name").hasArg().create("t");
        Option lacModeOption = OptionBuilder.withArgName("LAC mode").withLongOpt("mode")
                .withDescription("[OPTIONAL] LAC mode. Possible values: " + getLacModes()).hasArg()
                .create("m");
        Option methodOption = OptionBuilder
                .withArgName("method")
                .withLongOpt("background-method")
                .withDescription(
                        "[OPTIONAL] Background subtraction method. Possible values: "
                                + getBgSubtractionMethods()).hasArg().create("b");
        Option startTimeOption = OptionBuilder.withArgName("start time").withLongOpt("start-time")
                .withDescription("[OPTIONAL] Start time in " + DATE_FORMAT_PATTERN + " format")
                .hasArg().create();
        Option endTimeOption = OptionBuilder.withArgName("end time").withLongOpt("end-time")
                .withDescription("[OPTIONAL] End time in " + DATE_FORMAT_PATTERN + " format")
                .hasArg().create();
        OptionGroup group1 = new OptionGroup();
        group1.setRequired(true);
        group1.addOption(new Option("i", "interactive", false,
                "prompt for input values, e.g. LACDUMP elevation and rigidity constraints"));
        group1.addOption(new Option("s", "systematic", false,
                "use default systematic values present in configuration file gingatoolbox.properties "));

        options.addOption(targetOption);
        options.addOption(methodOption);
        options.addOption(obsIdOption);
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

    protected static String getBgSubtractionMethods() {
        String s = "";
        BgSubtractionMethod[] methods = BgSubtractionMethod.values();
        for (int i = 0; i < methods.length; i++) {
            s += methods[i].toString() + ", ";
        }
        return s.substring(0, s.length() - 2);
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(new Comparator<Option>() {

            private static final String OPTS_ORDER = "isetbom"; // short option
                                                                // names

            @Override
            public int compare(Option o1, Option o2) {
                String argCharOption1 = o1.getLongOpt().substring(0, 1);
                String argCharOption2 = o2.getLongOpt().substring(0, 1);
                return OPTS_ORDER.indexOf(argCharOption1) - OPTS_ORDER.indexOf(argCharOption2);
            }
        });
        helpFormatter.printHelp("extract_timing_mode_1.sh", getOptions());
    }

    public static void extractTiming(SingleModeTargetObservation obs, BgSubtractionMethod method) {
        switch (method) {
        case HAYASHIDA:
            extractTimingHayashida(obs);
            break;
        case SIMPLE:
            extractTimingSimple(obs);
            break;
        case SUD_SORT:
            extractTimingSudSort(obs);
            break;
        default:
            log.error(method + " background subtraction method not supported");
            System.exit(1);
        }
    }

    public static void extractTimingHayashida(SingleModeTargetObservation obs) {
        TimingMode1HayashidaPipeline pipeline = new TimingMode1HayashidaPipeline();
        File timingFile = pipeline.run(obs);
        if (timingFile != null) {
            log.info("Timing file " + timingFile.getName() + " created successfully");
        }
    }

    public static void extractTimingSimple(SingleModeTargetObservation obs) {
        TimingMode1SimplePipeline pipeline = new TimingMode1SimplePipeline();
        File timingFile = pipeline.run(obs);
        if (timingFile != null) {
            log.info("Timing file " + timingFile.getName() + " created successfully");
        }
    }

    public static void extractTimingSudSort(SingleModeTargetObservation obs) {
        TimingMode1SudSortPipeline pipeline = new TimingMode1SudSortPipeline();
        File timingFile = pipeline.run(obs);
        if (timingFile != null) {
            log.info("Timing file " + timingFile.getName() + " created successfully");
        }
    }

}