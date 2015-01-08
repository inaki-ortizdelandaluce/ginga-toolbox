package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.InputParameters;
import org.ginga.toolbox.gti.GtiWriter;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public abstract class LacqrdfitsInputBuilder extends
        AbstractPipe<LacdumpQuery, LacqrdfitsInputModel> implements
        TransformPipe<LacdumpQuery, LacqrdfitsInputModel> {

    private final static Logger log = Logger.getLogger(LacqrdfitsInputBuilder.class);

    /**
     * Returns the time bin width in seconds.
     * 
     * <pre>
     * Example:
     *   For 1/4 SF 1s,  8s and  32s for HI, MED and LOW bit rates respectively
     *   For 1/2 SF 2s, 16s and  64s for HI, MED and LOW bit rates respectively
     *   For 1 SF   4s, 64s and 128s for HI, MED and LOW bit rates respectively
     * </pre>
     * @return time bin width in seconds
     */
    public abstract int getTimingBinWidth();

    public abstract boolean backgroundCorrection();

    public abstract boolean aspectCorrection();

    public abstract boolean isTimingBackground();

    /*
     * Receives a LacdumpQuery, creates a GTI/Region file and finally emits a LacqrdfitsInputModel
     * referencing such file
     */
    @Override
    protected LacqrdfitsInputModel processNextStart() throws NoSuchElementException {
        try {
            LacdumpQuery query = this.starts.next();

            // set working directory
            File workingDir = new File(GingaToolboxEnv.getInstance().getWorkingDir());
            if (!workingDir.exists()) {
                workingDir.mkdirs();
            }
            log.info("Working directory " + workingDir.getAbsolutePath());

            // build empty GTI file
            File gtiFile = new File(workingDir, FileUtil.nextFileName("REGION",
                    query.getStartTime(), query.getMode(), "DATA"));

            // query entities matching the criteria
            LacdumpDao dao = new LacdumpDaoImpl();
            List<LacdumpSfEntity> sfList = dao.findSfList(query);
            log.info("LACDUMP query:" + query.toString());
            log.info("Query executed successfully. " + sfList.size() + " result(s) found");

            if (sfList.size() > 0) {
                // save matching results into a GTI file
                GtiWriter gtiWriter = new GtiWriter();
                gtiWriter.writeToFile(query.getTargetName(), sfList, false, false, gtiFile);
                log.debug("GTI file " + gtiFile.getPath() + " written successfully");

                // emit lacqrdfits input model
                LacqrdfitsInputModel inputModel = new LacqrdfitsInputModel();
                InputParameters input = GingaToolboxEnv.getInstance().getInputParameters();
                inputModel.setStartTime(query.getStartTime());
                inputModel.setPsFileName(FileUtil.nextFileName("lacqrd", query.getStartTime(),
                        query.getMode(), "ps"));
                inputModel.setMinElevation(input.getElevationMin());
                inputModel.setMaxElevation(input.getElevationMax());
                if (!isTimingBackground()) {
                    inputModel.setTimingFileName(FileUtil.nextFileName("TIMING",
                            query.getStartTime(), query.getMode(), "fits"));
                    inputModel.setSpectralFileName(FileUtil.nextFileName("SPEC",
                            query.getStartTime(), query.getMode(), "FILE"));
                } else {
                    inputModel.setTimingFileName(FileUtil.nextFileName("TIMING_BGD",
                            query.getStartTime(), query.getMode(), "fits"));
                    inputModel.setSpectralFileName(FileUtil.nextFileName("SPEC_BGD",
                            query.getStartTime(), query.getMode(), "FILE"));
                }
                inputModel.setRegionFileName(gtiFile.getName());
                inputModel.setBgCorrection(backgroundCorrection());
                inputModel.setAspectCorrection(aspectCorrection());
                inputModel.setDeadTimeCorrection(input.getDeadTimeCorrection());
                inputModel.setDelayTimeCorrection(input.getDelayTimeCorrection());
                inputModel.setCounter1(input.getLacCounter1());
                inputModel.setCounter2(input.getLacCounter2());
                inputModel.setCounter3(input.getLacCounter3());
                inputModel.setCounter4(input.getLacCounter4());
                inputModel.setCounter5(input.getLacCounter5());
                inputModel.setCounter6(input.getLacCounter6());
                inputModel.setCounter7(input.getLacCounter7());
                inputModel.setCounter8(input.getLacCounter8());
                inputModel.setMixedMode(input.isLacMixedMode());
                inputModel.setLacMode(query.getMode());
                inputModel.setTimingBinWidth(getTimingBinWidth());

                return inputModel;
            }
        } catch (IOException | LacdumpDaoException e) {
            log.error("Error generating GTI file. Message= " + e.getMessage(), e);
        }
        return null;
    }
}
