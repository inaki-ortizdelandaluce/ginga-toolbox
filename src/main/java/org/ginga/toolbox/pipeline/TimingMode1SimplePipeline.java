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

public class TimingMode1SimplePipeline {

    public TimingMode1SimplePipeline() {
    }

    public File run(LacModeTargetObservation obs) {
        File bgSpectrumFile = obs.getBackgroundFile();
        if (bgSpectrumFile != null) {
            return extractTiming(obs, bgSpectrumFile);
        } else {
            TimingBackgroundPipeline bgPipeline = new TimingBackgroundPipeline();
            bgPipeline.run(Arrays.asList(obs));
            return extractTiming(obs, bgPipeline.next());
        }
    }

    private File extractTiming(LacModeTargetObservation obs, final File bgSpectrumFile) {
        Pipe<LacModeTargetObservation, LacModeTargetObservation> modeFilter = new FilterFunctionPipe<LacModeTargetObservation>(
                new TimingMode1Filter());
        Pipe<LacModeTargetObservation, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, TiminfilfitsInputModel> inputBuilder = new TiminfilfitsInputBuilder() {

            @Override
            public BgSubtractionMethod getBgSubtractionMethod() {
                return BgSubtractionMethod.SIMPLE;
            }

            @Override
            public String getBgFileName() {
                return bgSpectrumFile.getName();
            }

            @Override
            public boolean sudSort() {
                return false;
            }
        };
        Pipe<TiminfilfitsInputModel, File> timinfilfits = new TiminfilfitsRunner();

        Pipeline<LacModeTargetObservation, File> extractor = new Pipeline<LacModeTargetObservation, File>(
                modeFilter, queryBuilder, inputBuilder, timinfilfits);
        extractor.setStarts(Arrays.asList(obs));
        return extractor.next();
    }
}