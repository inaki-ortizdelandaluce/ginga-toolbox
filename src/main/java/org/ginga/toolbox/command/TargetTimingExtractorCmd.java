package org.ginga.toolbox.command;

import java.io.File;
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
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.pipeline.ObservationListBuilder;
import org.ginga.toolbox.pipeline.TimingMode1HayashidaPipeline;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

public class TargetTimingExtractorCmd {

    private final static Logger log = Logger.getLogger(TargetTimingExtractorCmd.class);

    public static void extractTiming(String target, BgSubtractionMethod method) {
        switch (method) {
        case HAYASHIDA:
            extractTimingHayashida(target);
            break;
        case SIMPLE:
        case SUD_SORT:
        default:
            log.error(method
                    + " background subtraction method not yet supported for bulk processing");
            System.exit(1);
        }
    }

    public static void extractTimingHayashida(String target) {
        // find all observations for input target
        ObservationListBuilder obsListBuilder = new ObservationListBuilder();
        obsListBuilder.setStarts(Arrays.asList(target));
        Map<ObservationEntity, List<LacModeTargetObservation>> obsMap = obsListBuilder.next();

        // find available modes for each observations and extract timing
        Iterator<ObservationEntity> obsIterator = obsMap.keySet().iterator();
        ObservationEntity obsEntity = null;
        while (obsIterator.hasNext()) {
            obsEntity = obsIterator.next();
            log.info("Processing observation " + obsEntity.getSequenceNumber() + "...");
            List<LacModeTargetObservation> targetObsList = obsMap.get(obsEntity);
            // extract timing for all relevant modes
            if (targetObsList != null) {
                // run pipeline
                TimingMode1HayashidaPipeline timingHayashidaPipeline = new TimingMode1HayashidaPipeline();
                File file = null;
                for (LacModeTargetObservation obs : targetObsList) {
                    file = timingHayashidaPipeline.run(obs);
                    if (file != null) {
                        log.info("Timing file " + file.getName() + " created successfully");
                    }
                }
            }
            log.info("Observation " + obsEntity.getSequenceNumber() + " processed successfully");
        }
    }

    public static void main(String[] args) {
        try {
            CommandLine commandLine = new BasicParser().parse(getOptions(), args);
            // read command line argument values
            String target = commandLine.getOptionValue("t");
            BgSubtractionMethod method = null;
            try {
                method = Enum.valueOf(BgSubtractionMethod.class, commandLine.getOptionValue("b"));
            } catch (IllegalArgumentException e) {
                log.error("Unknown background subtraction method "
                        + commandLine.getOptionValue("b"));
                printHelp();
                System.exit(1);
            }
            // extract timing
            extractTiming(target, method);
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
                .isRequired(true).withDescription("Target Name").hasArg().create("t");
        Option methodOption = OptionBuilder
                .withArgName("method")
                .withLongOpt("background-subtraction")
                .isRequired(true)
                .withDescription(
                        "Background subtraction method. Possible values: "
                                + getBgSubtractionMethods()).hasArg().create("b");
        options.addOption(targetOption);
        options.addOption(methodOption);
        return options;
    }

    private static String getBgSubtractionMethods() {
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

            private static final String OPTS_ORDER = "tb"; // short option names

            @Override
            public int compare(Option o1, Option o2) {
                return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
            }
        });
        helpFormatter.printHelp("extract_timing.sh", getOptions());
    }
}