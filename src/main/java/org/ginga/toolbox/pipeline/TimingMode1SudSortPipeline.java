package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.Arrays;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.timinfilfits.TiminfilfitsInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimingMode1SudSortPipeline {

    public TimingMode1SudSortPipeline() {
    }

    public File run(PipelineInput obs) {
        TimingBackgroundPipeline bgPipeline = new TimingBackgroundPipeline(true);
        bgPipeline.run(Arrays.asList(obs));
        return extractTiming(obs, bgPipeline.next());
    }

    private File extractTiming(PipelineInput obs, final File bgMonitorFile) {
        Pipe<PipelineInput, PipelineInput> modeFilter = new FilterFunctionPipe<PipelineInput>(
                new TimingMode1Filter());
        Pipe<PipelineInput, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
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

        Pipeline<PipelineInput, File> extractor = new Pipeline<PipelineInput, File>(
                modeFilter, queryBuilder, inputBuilder, timinfilfits);
        extractor.setStarts(Arrays.asList(obs));
        return extractor.next();
    }
}