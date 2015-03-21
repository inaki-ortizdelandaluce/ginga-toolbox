package org.ginga.toolbox.command;

import java.io.File;
import java.util.Arrays;
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
import org.ginga.toolbox.pipeline.SpectrumBackgroundPipeline;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.TimeUtil;

public class BackgroundSpectrumExtractorCmd {

    protected final static String DATE_FORMAT_PATTERN = TimeUtil.DATE_FORMAT_INPUT.toPattern();
    private final static Logger log = Logger.getLogger(BackgroundSpectrumExtractorCmd.class);

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
                    return;
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
                    return;
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
                    return;
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
                    return;
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
            // extract spectrum
            switch (method) {
            case SIMPLE:
                extractBackgroundSpectrum(obs, false);
                break;
            case SUD_SORT:
                extractBackgroundSpectrum(obs, true);
                break;
            case HAYASHIDA:
            default:
                log.error("Hayashida background method is not applicable");
                printHelp();
                return;
            }
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
                .withDescription("[OPTIONAL] Target name").hasArg().create("t");
        Option lacModeOption = OptionBuilder.withArgName("LAC mode").withLongOpt("mode")
                .withDescription("[OPTIONAL] LAC mode. Possible values: " + getLacModes()).hasArg()
                .create("m");
        Option methodOption = OptionBuilder
                .withArgName("method")
                .withLongOpt("background-method")
                .withDescription(
                        "[OPTIONAL] Background subtraction method. Possible values: SIMPLE, SUD_SORT")
                        .hasArg().create("b");
        Option startTimeOption = OptionBuilder.withArgName("start time").withLongOpt("start-time")
                .withDescription("[OPTIONAL] Start time in " + DATE_FORMAT_PATTERN + " format")
                .hasArg().create();
        Option endTimeOption = OptionBuilder.withArgName("end time").withLongOpt("end-time")
                .withDescription("[OPTIONAL] End time in " + DATE_FORMAT_PATTERN + " format")
                .hasArg().create();

        OptionGroup dataReductionModeGroup = new OptionGroup();
        dataReductionModeGroup.setRequired(true);
        dataReductionModeGroup.addOption(new Option("i", "interactive", false,
                "prompt for input values, e.g. LACDUMP elevation and rigidity constraints"));
        dataReductionModeGroup
                .addOption(new Option("s", "systematic", false,
                        "use default systematic values present in configuration file gingatoolbox.properties "));

        options.addOption(targetOption);
        options.addOption(methodOption);
        options.addOption(lacModeOption);
        options.addOption(startTimeOption);
        options.addOption(endTimeOption);

        options.addOptionGroup(dataReductionModeGroup);

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

            private static final String OPTS_ORDER = "ofisetmb"; // short option names

            @Override
            public int compare(Option o1, Option o2) {
                String argCharOption1 = o1.getLongOpt().substring(0, 1);
                String argCharOption2 = o2.getLongOpt().substring(0, 1);
                return OPTS_ORDER.indexOf(argCharOption1) - OPTS_ORDER.indexOf(argCharOption2);
            }
        });
        helpFormatter.printHelp("extract_background_spectrum.sh", getOptions());
    }

    public static void extractBackgroundSpectrum(LacModeTargetObservation obs, boolean sudSort) {
        SpectrumBackgroundPipeline pipeline = new SpectrumBackgroundPipeline(sudSort);
        pipeline.run(Arrays.asList(obs));
        File specFile = pipeline.next();
        if (specFile != null) {
            log.info("Background spectrum file " + specFile.getName() + " created successfully");
        }
    }
}