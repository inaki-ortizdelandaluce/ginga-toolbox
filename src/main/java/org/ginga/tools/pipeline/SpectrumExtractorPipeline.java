package org.ginga.tools.pipeline;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LacdumpConstraints;
import org.ginga.tools.observation.ObservationEntity;
import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.spectrum.LacqrdfitsInputModel;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumExtractorPipeline {

    private final static Logger log = Logger.getLogger(SpectrumExtractorPipeline.class);

    public void executeWithHayashidaSubtraction(String target) {
        // initialize all pipes needed
        Pipe<ObservationModeDetails, ObservationModeDetails> modeFilter = new FilterFunctionPipe<ObservationModeDetails>(
                new SpectrumModeFilterPipe());
        Pipe<ObservationModeDetails, LacdumpConstraints> constraintsBuilder = new LacdumpConstraintsPipe();
        Pipe<LacdumpConstraints, LacqrdfitsInputModel> lacqrdfitsInputBuilder = new LacqrdfitsInputPipe();
        Pipe<LacqrdfitsInputModel, File> lacqrdfitsExecutor = new LacqrdfitsPipe();

        // scan observations for input target
        Pipe<String, List<ObservationEntity>> scanner = new TargetObservationScannerPipe();
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
                        constraintsBuilder, lacqrdfitsInputBuilder, lacqrdfitsExecutor);
                specExtractor.setStarts(obsEntity.getAvailableModesDetails());
                File file = null;
                while (specExtractor.hasNext()) {
                    file = specExtractor.next();
                    if (file != null) {
                        log.info("Spectrum file " + file.getName() + " created successfully");
                        log.info("Pipeline chain " + specExtractor.getCurrentPath());
                        return;
                    }
                }
            }
            log.info("Observation " + obsEntity.getSequenceNumber() + " processed successfully");
        }
    }
}
