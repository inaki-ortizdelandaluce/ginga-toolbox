package org.ginga.toolbox;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.toolbox.lacdump.LacdumpConstraints;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.ObservationModeDetails;
import org.ginga.toolbox.pipeline.Lac2xspecPipe;
import org.ginga.toolbox.pipeline.LacdumpConstraintsPipe;
import org.ginga.toolbox.pipeline.LacqrdfitsInputPipe;
import org.ginga.toolbox.pipeline.LacqrdfitsPipe;
import org.ginga.toolbox.pipeline.SpectrumModeFilterPipe;
import org.ginga.toolbox.pipeline.TargetObservationListPipe;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpecExtractorHayashida {

    private final static Logger log = Logger.getLogger(SpecExtractorHayashida.class);

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage org.ginga.toolbox.SpecExtractorHayashida <target>");
            System.exit(1);
        }
        SpecExtractorHayashida extractor = new SpecExtractorHayashida();
        extractor.execute(args[0]);
    }

    public void execute(String target) {
        // initialize all pipes needed
        Pipe<ObservationModeDetails, ObservationModeDetails> modeFilter = new FilterFunctionPipe<ObservationModeDetails>(
                new SpectrumModeFilterPipe());
        Pipe<ObservationModeDetails, LacdumpConstraints> constraintsBuilder = new LacdumpConstraintsPipe();
        Pipe<LacdumpConstraints, LacqrdfitsInputModel> lacqrdfitsInputBuilder = new LacqrdfitsInputPipe();
        Pipe<LacqrdfitsInputModel, File> lacqrdfits = new LacqrdfitsPipe();
        Pipe<File, File> lac2xspec = new Lac2xspecPipe();

        // scan observations for input target
        Pipe<String, List<ObservationEntity>> scanner = new TargetObservationListPipe();
        scanner.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = scanner.next();

        Pipeline<ObservationModeDetails, File> specExtractor = null;
        List<ObservationModeDetails> obsModeDetails = null;
        for (ObservationEntity obsEntity : obsList) {
            log.info("Processing observation " + obsEntity.getSequenceNumber() + "...");
            obsModeDetails = obsEntity.getAvailableModesDetails();
            // extract spectrum for all relevant modes
            if (obsModeDetails != null) {
                // run pipeline
                specExtractor = new Pipeline<ObservationModeDetails, File>(modeFilter,
                        constraintsBuilder, lacqrdfitsInputBuilder, lacqrdfits, lac2xspec);
                specExtractor.setStarts(obsEntity.getAvailableModesDetails());
                File file = null;
                while (specExtractor.hasNext()) {
                    file = specExtractor.next();
                    if (file != null) {
                        log.info("Spectrum file " + file.getName() + " created successfully");
                    }
                }
            }
            log.info("Observation " + obsEntity.getSequenceNumber() + " processed successfully");
        }
    }
}
