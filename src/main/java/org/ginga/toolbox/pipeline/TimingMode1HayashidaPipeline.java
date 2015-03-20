package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.Arrays;

import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.timinfilfits.TiminfilfitsInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimingMode1HayashidaPipeline {

    public TimingMode1HayashidaPipeline() {
    }

    public File run(PipelineInput obs) {
        // create a BGD timing file using lacqrdfits
        Pipe<PipelineInput, PipelineInput> modeFilter = new FilterFunctionPipe<PipelineInput>(
                new TimingMode1Filter());
        Pipe<PipelineInput, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, LacqrdfitsInputModel> inputBuilder = new LacqrdfitsInputBuilder() {

            @Override
            public int getTimingBinWidth() {
                DataReductionEnv dataReductionEnv = GingaToolboxEnv.getInstance()
                        .getDataReductionEnv();
                // return lowest time resolution for each BR, i.e. MPC1 mode, 1SF (8x)
                switch (dataReductionEnv.getBitRate()) {
                case L:
                default:
                    return 128; // 8x16s
                case M:
                    return 64; // 8x4s
                case ANY:
                case H:
                    return 4; // 8x500ms
                }
            }

            @Override
            public boolean backgroundCorrection() {
                return true;
            }

            @Override
            public boolean aspectCorrection() {
                return false; // background
            }

            @Override
            public boolean isTimingBackground() {
                return true;
            }

        };
        Pipe<LacqrdfitsInputModel, File> lacqrdfits = new LacqrdfitsRunner(true);
        Pipeline<PipelineInput, File> bgPipeline = new Pipeline<PipelineInput, File>(
                modeFilter, queryBuilder, inputBuilder, lacqrdfits);
        bgPipeline.setStarts(Arrays.asList(obs));
        // extract timing file subtracting the BGD file
        return extractTiming(obs, bgPipeline.next());
    }

    private File extractTiming(PipelineInput obs, final File bgTimingFile) {
        Pipe<PipelineInput, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, TiminfilfitsInputModel> inputBuilder = new TiminfilfitsInputBuilder() {

            @Override
            public BgSubtractionMethod getBgSubtractionMethod() {
                return BgSubtractionMethod.HAYASHIDA;
            }

            @Override
            public String getBgFileName() {
                return bgTimingFile.getName();
            }

            @Override
            public boolean sudSort() {
                return false;
            }
        };
        Pipe<TiminfilfitsInputModel, File> timinfilfits = new TiminfilfitsRunner();

        Pipeline<PipelineInput, File> extractor = new Pipeline<PipelineInput, File>(
                queryBuilder, inputBuilder, timinfilfits);
        extractor.setStarts(Arrays.asList(obs));
        return extractor.next();
    }
}