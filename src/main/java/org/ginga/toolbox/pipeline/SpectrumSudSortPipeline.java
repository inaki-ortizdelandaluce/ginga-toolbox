package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.Arrays;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacspec.LacspecInputModel;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumSudSortPipeline {

    public SpectrumSudSortPipeline() {
    }

    public File run(LacModeTargetObservation obs) {
        SpectrumBackgroundPipeline bgPipeline = new SpectrumBackgroundPipeline(true);
        bgPipeline.run(Arrays.asList(obs));
        return extractSpecWithBg(obs, bgPipeline.next());
    }

    private File extractSpecWithBg(LacModeTargetObservation obs, final File bgSpectrumFile) {
        Pipe<LacModeTargetObservation, LacModeTargetObservation> modeFilter = new FilterFunctionPipe<LacModeTargetObservation>(
                new SpectrumModeFilter());
        Pipe<LacModeTargetObservation, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, LacspecInputModel> lacspecInputBuilder = new LacspecInputBuilder() {

            @Override
            public boolean isBackground() {
                return false;
            }

            @Override
            public BgSubtractionMethod getBgSubtractionMethod() {
                return BgSubtractionMethod.SUD_SORT;
            }

            @Override
            public String getBgFileName() {
                return bgSpectrumFile.getName();
            }

            @Override
            public boolean backgroundCorrection() {
                return true;
            }

            @Override
            public boolean aspectCorrection() {
                return true;
            }

            @Override
            public int getDataUnit() {
                return 1; // counts/sec
            }

            @Override
            public boolean sudSort() {
                return true;
            }
        };
        Pipe<LacspecInputModel, File> lacspec = new LacspecRunner();
        Pipe<File, File> lac2xspec = new Lac2xspecRunner();

        Pipeline<LacModeTargetObservation, File> specExtractor = new Pipeline<LacModeTargetObservation, File>(
                modeFilter, queryBuilder, lacspecInputBuilder, lacspec, lac2xspec);
        specExtractor.setStarts(Arrays.asList(obs));
        return specExtractor.next();
    }
}