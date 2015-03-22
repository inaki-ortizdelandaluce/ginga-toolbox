package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.List;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacspec.LacspecInputModel;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumBackgroundPipeline {

    private Pipeline<LacModeTargetObservation, File> pipeline;
    private Pipe<LacModeTargetObservation, LacdumpQuery> queryBuilder;

    public SpectrumBackgroundPipeline() {
        this(false, true);
    }

    // public SpectrumBackgroundPipeline(final boolean sudsort) {
    public SpectrumBackgroundPipeline(boolean sudsort, boolean autoBackgroundSelection) {
        // initialize all pipes needed
        Pipe<LacModeTargetObservation, LacModeTargetObservation> modeFilter = new FilterFunctionPipe<LacModeTargetObservation>(
                new SpectrumModeFilter());
        if (autoBackgroundSelection) { // build background region file from suggested observations
            this.queryBuilder = new LacdumpQueryBgBuilder();
        } else { // build background region file from LacModeTargetObservation constraints
            this.queryBuilder = new LacdumpQueryBuilder();
        }
        if (sudsort) {
            this.pipeline = new Pipeline<LacModeTargetObservation, File>(modeFilter,
                    this.queryBuilder, new BgdspecInputBuilder(), new BgdspecRunner());
        } else {
            Pipe<LacdumpQuery, LacspecInputModel> inputBuilder = new LacspecInputBuilder() {

                @Override
                public boolean isBackground() {
                    return true;
                }

                @Override
                public BgSubtractionMethod getBgSubtractionMethod() {
                    return null;
                }

                @Override
                public String getBgFileName() {
                    return null;
                }

                @Override
                public boolean backgroundCorrection() {
                    return false;
                }

                @Override
                public boolean aspectCorrection() {
                    return false;
                }

                @Override
                public int getDataUnit() {
                    return 1; // counts/sec
                }

                @Override
                public boolean sudSort() {
                    return false;
                }
            };
            this.pipeline = new Pipeline<LacModeTargetObservation, File>(modeFilter,
                    this.queryBuilder, inputBuilder, new LacspecRunner());
        }
    }

    public void run(List<LacModeTargetObservation> obsList) {
        this.pipeline.setStarts(obsList);
    }

    public boolean hasNext() {
        return this.pipeline.hasNext();
    }

    public File next() {
        return this.pipeline.next();
    }
}
