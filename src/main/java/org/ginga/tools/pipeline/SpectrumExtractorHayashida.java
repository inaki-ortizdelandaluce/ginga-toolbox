package org.ginga.tools.pipeline;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.ginga.tools.lacdump.LacdumpConstraints;
import org.ginga.tools.observation.ObservationEntity;
import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.spectrum.LacqrdfitsInputModel;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumExtractorHayashida {

    private List<String> targets;

    public SpectrumExtractorHayashida(List<String> targets) {
        this.targets = targets;
    }

    public SpectrumExtractorHayashida(String... targets) {
        this.targets = Arrays.asList(targets);
    }

    public void execute() {
        // initialize all pipes needed
        Pipe<String, List<ObservationEntity>> scanner = new TargetObservationScannerPipe();
        Pipe<ObservationModeDetails, ObservationModeDetails> modeFilter = new FilterFunctionPipe<ObservationModeDetails>(
                new SpectrumModeFilterPipe());
        Pipe<ObservationModeDetails, LacdumpConstraints> constraintsBuilder = new LacdumpConstraintsPipe();
        Pipe<LacdumpConstraints, LacqrdfitsInputModel> lacqrdfitsInputBuilder = new LacqrdfitsInputPipe();
        Pipe<LacqrdfitsInputModel, File> lacqrdfitsExecutor = new LacqrdfitsPipe();

        // scan observations for input targets
        scanner.setStarts(this.targets);
        while (scanner.hasNext()) {
            List<ObservationEntity> obsList = scanner.next();

            for (ObservationEntity obsEntity : obsList) {
                // extract spectrum for all relevant modes
                // run pipeline
                Pipeline<ObservationModeDetails, File> specExtractor = new Pipeline<ObservationModeDetails, File>(
                        modeFilter, constraintsBuilder, lacqrdfitsInputBuilder, lacqrdfitsExecutor);
                specExtractor.setStarts(obsEntity.getAvailableModesDetails());
                while (specExtractor.hasNext()) {
                    specExtractor.next();
                }
            }
        }
    }
}
