package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.Arrays;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.timinfilfits.TiminfilfitsInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimingMode1SimplePipeline {

    public TimingMode1SimplePipeline() {
    }

    public File run(SingleModeTargetObservation obs) {
        TimingBackgroundPipeline bgPipeline = new TimingBackgroundPipeline();
        bgPipeline.run(Arrays.asList(obs));
        return extractTiming(obs, bgPipeline.next());
    }

    private File extractTiming(SingleModeTargetObservation obs, final File bgSpectrumFile) {
        Pipe<SingleModeTargetObservation, SingleModeTargetObservation> modeFilter = new FilterFunctionPipe<SingleModeTargetObservation>(
                new TimingMode1Filter());
        Pipe<SingleModeTargetObservation, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, TiminfilfitsInputModel> inputBuilder = new TiminfilfitsInputBuilder() {

            @Override
            public BgSubtractionMethod getBgSubtractionMethod() {
                return BgSubtractionMethod.SIMPLE;
            }

            @Override
            public String getBgFileName() {
                return bgSpectrumFile.getName();
            }
        };
        Pipe<TiminfilfitsInputModel, File> timinfilfits = new TiminfilfitsRunner();

        Pipeline<SingleModeTargetObservation, File> extractor = new Pipeline<SingleModeTargetObservation, File>(
                modeFilter, queryBuilder, inputBuilder, timinfilfits);
        extractor.setStarts(Arrays.asList(obs));
        return extractor.next();
    }
}