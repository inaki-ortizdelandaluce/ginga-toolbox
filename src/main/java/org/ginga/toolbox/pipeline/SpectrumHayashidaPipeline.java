package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.observation.LacModeTargetObservation;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumHayashidaPipeline {

    private static final Logger LOGGER = Logger.getLogger(SpectrumHayashidaPipeline.class);

    private Pipe<LacModeTargetObservation, LacModeTargetObservation> modeFilter;
    private Pipe<LacModeTargetObservation, LacdumpQuery> queryBuilder;
    private Pipe<LacdumpQuery, LacqrdfitsInputModel> lacqrdfitsInputBuilder;
    private Pipe<LacqrdfitsInputModel, File> lacqrdfits;
    private Pipe<File, File> lac2xspec;
    private Pipeline<LacModeTargetObservation, File> pipeline;

    public SpectrumHayashidaPipeline() {
        // initialize all pipes needed
        this.modeFilter = new FilterFunctionPipe<LacModeTargetObservation>(new SpectrumModeFilter());
        this.queryBuilder = new LacdumpQueryBuilder();
        this.lacqrdfitsInputBuilder = new LacqrdfitsInputBuilder() {

            @Override
            public int getTimingBinWidth() {
                DataReductionEnv dataReductionEnv = GingaToolboxEnv.getInstance()
                        .getDataReductionEnv();
                // return lowest time resolution for each BR, i.e. MPC1 mode, 1SF (8x)
                switch (dataReductionEnv.getBitRate()) {
                case ANY:
                case L:
                default:
                    return 128; // 8x16s
                case M:
                    return 64; // 8x4s
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
                return true;
            }

            @Override
            public boolean isTimingBackground() {
                return false;
            }
        };
        this.lacqrdfits = new LacqrdfitsRunner();
        this.lac2xspec = new Lac2xspecRunner();
        this.pipeline = new Pipeline<LacModeTargetObservation, File>(this.modeFilter,
                this.queryBuilder, this.lacqrdfitsInputBuilder, this.lacqrdfits, this.lac2xspec);
    }

    public void run(List<LacModeTargetObservation> obsList) {
        LOGGER.debug("Entering into SpectrumHayashidaPipeline.run...");
        this.pipeline.setStarts(obsList);
    }

    public boolean hasNext() {
        return this.pipeline.hasNext();
    }

    public File next() {
        return this.pipeline.next();
    }
}
