package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.Arrays;

import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.timinfilfits.TiminfilfitsInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimingMode1HayashidaPipeline {

    public TimingMode1HayashidaPipeline() {
    }

    public File run(LacModeTargetObservation obs) {
        // create a BGD timing file using lacqrdfits
        Pipe<LacModeTargetObservation, LacModeTargetObservation> modeFilter = new FilterFunctionPipe<LacModeTargetObservation>(
                new TimingMode1Filter());
        Pipe<LacModeTargetObservation, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, LacqrdfitsInputModel> inputBuilder = new LacqrdfitsInputBuilder() {

            @Override
            public int getTimingBinWidth() {
                DataReductionEnv dataReductionEnv = GingaToolboxEnv.getInstance().getDataReductionEnv();
                // return lowest time resolution for each BR, i.e. MPC1 mode, 1SF (8x)
                switch (dataReductionEnv.getBitRate()) {
                case L:
                default:
                    return 128; // 8x16s
                case M:
                    return 32; // 8x4s
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
        Pipeline<LacModeTargetObservation, File> bgPipeline = new Pipeline<LacModeTargetObservation, File>(modeFilter, queryBuilder,
                inputBuilder, lacqrdfits);
        bgPipeline.setStarts(Arrays.asList(obs));
        // extract timing file subtracting the BGD file
        return extractTiming(obs, bgPipeline.next());
    }

    private File extractTiming(LacModeTargetObservation obs, final File bgTimingFile) {
        Pipe<LacModeTargetObservation, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, TiminfilfitsInputModel> inputBuilder = new TiminfilfitsInputBuilder() {

            @Override
            public BgSubtractionMethod getBgSubtractionMethod() {
                return BgSubtractionMethod.HAYASHIDA;
            }

            @Override
            public String getBgFileName() {
                if (bgTimingFile != null) {
                    return bgTimingFile.getName();
                } else {
                    return null;
                }
            }

            @Override
            public boolean ignoreAllCorrections() {
                return false;
            }
        };
        Pipe<TiminfilfitsInputModel, File> timinfilfits = new TiminfilfitsRunner();

        Pipeline<LacModeTargetObservation, File> extractor = new Pipeline<LacModeTargetObservation, File>(queryBuilder, inputBuilder,
                timinfilfits);
        extractor.setStarts(Arrays.asList(obs));
        return extractor.next();
    }
}