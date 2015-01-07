package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.List;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacspec.LacspecInputModel;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimingBackgroundPipeline {

    private Pipe<SingleModeTargetObservation, SingleModeTargetObservation> modeFilter;
    private Pipe<SingleModeTargetObservation, LacdumpQuery> queryBuilder;
    private Pipe<LacdumpQuery, LacspecInputModel> inputBuilder;
    private Pipe<LacspecInputModel, File> lacspec;
    private Pipeline<SingleModeTargetObservation, File> pipeline;

    public TimingBackgroundPipeline() {
        // initialize all pipes needed
        this.modeFilter = new FilterFunctionPipe<SingleModeTargetObservation>(
                new TimingMode1Filter());
        this.queryBuilder = new LacdumpQueryBgBuilder();
        this.inputBuilder = new LacspecInputBuilder() {

            @Override
            public boolean isBackground() {
                return true;
            }

            @Override
            public BgSubtractionMethod getBgSubtractionMethod() {
                return null; // N/A
            }

            @Override
            public String getBgFileName() {
                return null; // N/A
            }

            @Override
            public boolean getBackgroundCorrection() {
                return false;
            }

            @Override
            public boolean getAspectCorrection() {
                return false;
            }

            @Override
            public int getDataUnit() {
                return 0; // counts
            }
        };
        this.lacspec = new LacspecRunner();
        this.pipeline = new Pipeline<SingleModeTargetObservation, File>(this.modeFilter,
                this.queryBuilder, this.inputBuilder, this.lacspec);
    }

    public void run(List<SingleModeTargetObservation> obsList) {
        this.pipeline.setStarts(obsList);
    }

    public boolean hasNext() {
        return this.pipeline.hasNext();
    }

    public File next() {
        return this.pipeline.next();
    }
}
