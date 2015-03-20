package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.Arrays;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.timinfilfits.TiminfilfitsInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimingMode1SudSortPipeline {

    public TimingMode1SudSortPipeline() {
    }

    public File run(LacModeTargetObservation obs) {
        TimingBackgroundPipeline bgPipeline = new TimingBackgroundPipeline(true);
        bgPipeline.run(Arrays.asList(obs));
        return extractTiming(obs, bgPipeline.next());
    }

    private File extractTiming(LacModeTargetObservation obs, final File bgMonitorFile) {
        Pipe<LacModeTargetObservation, LacModeTargetObservation> modeFilter = new FilterFunctionPipe<LacModeTargetObservation>(
                new TimingMode1Filter());
        Pipe<LacModeTargetObservation, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, TiminfilfitsInputModel> inputBuilder = new TiminfilfitsInputBuilder() {

            @Override
            public BgSubtractionMethod getBgSubtractionMethod() {
                return BgSubtractionMethod.SUD_SORT;
            }

            @Override
            public String getBgFileName() {
                return bgMonitorFile.getName();
            }

            @Override
            public boolean sudSort() {
                return true;
            }
        };
        Pipe<TiminfilfitsInputModel, File> timinfilfits = new TiminfilfitsRunner();

        Pipeline<LacModeTargetObservation, File> extractor = new Pipeline<LacModeTargetObservation, File>(
                modeFilter, queryBuilder, inputBuilder, timinfilfits);
        extractor.setStarts(Arrays.asList(obs));
        return extractor.next();
    }
}