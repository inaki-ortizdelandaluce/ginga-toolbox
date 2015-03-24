package org.ginga.toolbox.command;

import java.io.File;
import java.util.Arrays;
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
import org.ginga.toolbox.pipeline.TimingMode2Pipeline;

public class TargetTimingMode2ExtractorCmd {

    private final static Logger log = Logger.getLogger(TargetTimingMode2ExtractorCmd.class);

    public static void extractTiming(String target) {
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
            List<LacModeTargetObservation> obsList = obsMap.get(obsEntity);
            // extract timing for all relevant modes
            if (obsList != null) {
                // run pipeline
                TimingMode2Pipeline pipeline = new TimingMode2Pipeline();
                pipeline.run(obsList);
                File timingFile = pipeline.next();
                if (timingFile != null) {
                    log.info("Timing file " + timingFile.getName() + " created successfully");
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
            // extract timing
            extractTiming(target);
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
        options.addOption(targetOption);
        return options;
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("extract_timing_mode2.sh", getOptions());
    }
}