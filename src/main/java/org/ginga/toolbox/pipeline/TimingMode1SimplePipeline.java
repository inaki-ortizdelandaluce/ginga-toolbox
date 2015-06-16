package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.timinfilfits.TiminfilfitsInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimingMode1SimplePipeline {

    private static final Logger LOGGER = Logger.getLogger(TimingMode1SimplePipeline.class);

    public TimingMode1SimplePipeline() {
    }

    public File run(LacModeTargetObservation obs) {
        File inputFile = obs.getBackgroundFile();
        if (inputFile != null) {
            try {
                // copy file to working directory
                File bgSpectrumFile = new File(GingaToolboxEnv.getInstance().getWorkingDir(), inputFile.getName());
                FileUtil.copy(inputFile, bgSpectrumFile);
                return extractTiming(obs, bgSpectrumFile);
            } catch (IOException e) {
                LOGGER.error("Error copying background file to working directory", e);
                return null;
            }
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
            public boolean ignoreAllCorrections() {
                return false;
            }
        };
        Pipe<TiminfilfitsInputModel, File> timinfilfits = new TiminfilfitsRunner();

        Pipeline<LacModeTargetObservation, File> extractor = new Pipeline<LacModeTargetObservation, File>(modeFilter, queryBuilder,
                inputBuilder, timinfilfits);
        extractor.setStarts(Arrays.asList(obs));
        return extractor.next();
    }
}