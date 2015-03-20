package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.List;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacspec.LacspecInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumBackgroundPipeline {

    private Pipeline<PipelineInput, File> pipeline;

    public SpectrumBackgroundPipeline() {
        this(false);
    }

    public SpectrumBackgroundPipeline(final boolean sudsort) {
        // initialize all pipes needed
        Pipe<PipelineInput, PipelineInput> modeFilter = new FilterFunctionPipe<PipelineInput>(
                new SpectrumModeFilter());
        Pipe<PipelineInput, LacdumpQuery> queryBuilder = new LacdumpQueryBgBuilder();
        if (sudsort) {
            this.pipeline = new Pipeline<PipelineInput, File>(modeFilter,
                    queryBuilder, new BgdspecInputBuilder(), new BgdspecRunner());
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
            this.pipeline = new Pipeline<PipelineInput, File>(modeFilter,
                    queryBuilder, inputBuilder, new LacspecRunner());
        }
    }

    public void run(List<PipelineInput> obsList) {
        this.pipeline.setStarts(obsList);
    }

    public boolean hasNext() {
        return this.pipeline.hasNext();
    }

    public File next() {
        return this.pipeline.next();
    }
}
