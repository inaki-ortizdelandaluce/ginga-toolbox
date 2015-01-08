package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.List;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.tim2filfits.Tim2filfitsInputModel;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimingMode2Pipeline {

    private Pipeline<SingleModeTargetObservation, File> pipeline;

    public TimingMode2Pipeline() {
    }

    public void run(List<SingleModeTargetObservation> obsList) {
        Pipe<SingleModeTargetObservation, SingleModeTargetObservation> modeFilter = new FilterFunctionPipe<SingleModeTargetObservation>(
                new TimingMode2Filter());
        Pipe<SingleModeTargetObservation, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, Tim2filfitsInputModel> inputBuilder = new Tim2filfitsInputBuilder();
        Pipe<Tim2filfitsInputModel, File> tim2filfits = new Tim2filfitsRunner();

        this.pipeline = new Pipeline<SingleModeTargetObservation, File>(modeFilter, queryBuilder,
                inputBuilder, tim2filfits);
        this.pipeline.setStarts(obsList);
    }

    public boolean hasNext() {
        return this.pipeline.hasNext();
    }

    public File next() {
        return this.pipeline.next();
    }
}