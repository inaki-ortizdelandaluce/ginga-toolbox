package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.Arrays;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.timinfilfits.TiminfilfitsInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimingMode1NoCorrectionPipeline {

    public TimingMode1NoCorrectionPipeline() {
    }

    public File run(LacModeTargetObservation obs) {
        return extractTiming(obs);
    }

    private File extractTiming(LacModeTargetObservation obs) {
        Pipe<LacModeTargetObservation, LacdumpQuery> queryBuilder = new LacdumpQueryBuilder();
        Pipe<LacdumpQuery, TiminfilfitsInputModel> inputBuilder = new TiminfilfitsInputBuilder() {

            @Override
            public BgSubtractionMethod getBgSubtractionMethod() {
                return null;
            }

            @Override
            public String getBgFileName() {
                return null;
            }

            @Override
            public boolean ignoreAllCorrections() {
                return true;
            }
        };
        Pipe<TiminfilfitsInputModel, File> timinfilfits = new TiminfilfitsRunner();

        Pipeline<LacModeTargetObservation, File> extractor = new Pipeline<LacModeTargetObservation, File>(queryBuilder, inputBuilder,
                timinfilfits);
        extractor.setStarts(Arrays.asList(obs));
        return extractor.next();
    }
}