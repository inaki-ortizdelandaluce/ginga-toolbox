package org.ginga.tools;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LacdumpConstraints;
import org.ginga.tools.observation.ObservationEntity;
import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.pipeline.Lac2xspecPipe;
import org.ginga.tools.pipeline.LacdumpConstraintsPipe;
import org.ginga.tools.pipeline.LacqrdfitsInputPipe;
import org.ginga.tools.pipeline.LacqrdfitsPipe;
import org.ginga.tools.pipeline.SpectrumModeFilterPipe;
import org.ginga.tools.pipeline.TargetObservationListPipe;
import org.ginga.tools.spectrum.LacqrdfitsInputModel;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpecExtractorHayashida {

    private final static Logger log = Logger.getLogger(SpecExtractorHayashida.class);

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage org.ginga.tools.SpecExtractorHayashida <target>");
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
