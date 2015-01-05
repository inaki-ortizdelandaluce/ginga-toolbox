package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.gti.GtiFileWriter;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.lacspec.LacspecInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public abstract class LacspecInputPipe extends AbstractPipe<LacdumpQuery, LacspecInputModel>
        implements TransformPipe<LacdumpQuery, LacspecInputModel> {

    private final static Logger log = Logger.getLogger(LacspecInputPipe.class);

    public LacspecInputPipe() {
    }

    public abstract boolean isBackground();

    public abstract BgSubtractionMethod getBgSubtractionMethod();

    public abstract String getBgFileName();

    /*
     * Receives a LacdumpQuery, creates a GTI/Region file and finally emits a LacspecInputModel
     * referencing such file
     */
    @Override
    protected LacspecInputModel processNextStart() throws NoSuchElementException {
        try {
            LacdumpQuery query = this.starts.next();

            // set working directory
            File workingDir = new File(GingaToolboxEnv.getInstance().getWorkingDir());
            if (!workingDir.exists()) {
                workingDir.mkdirs();
            }
            log.debug("Working directory " + workingDir.getAbsolutePath());

            // build empty GTI file
            File gtiFile = null;
            if (isBackground()) {
                log.debug("Generating GTI file for on-source data");
                String prefix = query.getTargetName().replace(" ", "") + "_BGD";
                gtiFile = new File(workingDir, FileUtil.nextFileName(workingDir, prefix, "DATA"));
            } else {
                log.debug("Generating GTI file for on-source data");
                gtiFile = new File(workingDir, FileUtil.nextFileName("REGION",
                        query.getStartTime(), query.getMode(), "DATA"));
            }

            log.debug("GTI file " + gtiFile.getPath());

            // query entities matching the criteria
            LacdumpDao dao = new LacdumpDaoImpl();
            List<LacdumpSfEntity> sfList = dao.findSfList(query);
            log.info("LACDUMP query executed successfully. " + sfList.size() + " result(s) found");

            if (sfList.size() > 0) {
                // save matching results into a GTI file
                GtiFileWriter gtiWriter = new GtiFileWriter();
                gtiWriter.writeToFile(query.getTargetName(), sfList, true, gtiFile);
                log.info("GTI file " + gtiFile.getPath() + " written successfully");

                // emit lacspec input model
                LacspecInputModel inputModel = new LacspecInputModel();
                DataReductionEnv env = GingaToolboxEnv.getInstance().getDataReductionEnv();
                inputModel.setHasBackground(!isBackground());
                if (!isBackground()) {
                    inputModel.setBgMethod(getBgSubtractionMethod());
                    inputModel.setBgFileName(getBgFileName());
                    inputModel.setBgSubFileNumber(env.getBgSubFileNumber());
                    inputModel.setLacMode(query.getMode());
                    inputModel.setPsFileName(FileUtil.nextFileName("lacspec", query.getStartTime(),
                            query.getMode(), "ps"));
                    inputModel.setBgCorrection(1);
                    inputModel.setStartTime(query.getStartTime());
                    inputModel.setSpectralFileName(FileUtil.nextFileName("SPEC",
                            query.getStartTime(), query.getMode(), "FILE"));
                    inputModel.setMonitorFileName(FileUtil.nextFileName("MONI",
                            query.getStartTime(), query.getMode(), "SPEC"));
                } else {
                    inputModel.setLacMode(LacMode.INIT);
                    String prefix = query.getTargetName().replace(" ", "");
                    inputModel.setPsFileName(FileUtil.nextFileName(workingDir, prefix
                            + "_lacspec_bgd", "ps"));
                    inputModel.setBgCorrection(0);
                    inputModel.setSpectralFileName(FileUtil.nextFileName(workingDir, prefix
                            + "_SPEC_BGD", "FILE"));
                    inputModel.setMonitorFileName(FileUtil.nextFileName(workingDir, prefix
                            + "_MONI_BGD", "SPEC"));
                }
                inputModel.setBitRate(env.getBitRate());
                inputModel.setMinElevation(env.getElevationMin());
                inputModel.setMaxElevation(env.getElevationMax());
                inputModel.setMinRigidity(env.getCutOffRigidityMin());
                inputModel.setMaxRigidity(env.getCutOffRigidityMax());
                inputModel.setAspectCorrection(env.getAspectCorrection());
                inputModel.setDeadTimeCorrection(env.getDeadTimeCorrection());
                inputModel.setChannelToEnergy(env.getChannelToEnergyConversion());
                inputModel.setDataUnit(env.getDataUnit());
                inputModel.setAce(env.getAttitudeMode());
                inputModel.setCounter1(env.getLacCounter1());
                inputModel.setCounter2(env.getLacCounter2());
                inputModel.setCounter3(env.getLacCounter3());
                inputModel.setCounter4(env.getLacCounter4());
                inputModel.setCounter5(env.getLacCounter5());
                inputModel.setCounter6(env.getLacCounter6());
                inputModel.setCounter7(env.getLacCounter7());
                inputModel.setCounter8(env.getLacCounter8());
                inputModel.setMixedMode(env.isLacMixedMode());
                inputModel.setRegionFileName(gtiFile.getName());
                inputModel.setMonitorFileName("MONI.SPEC");
                return inputModel;
            }
        } catch (IOException | LacdumpDaoException e) {
            log.error("Error generating GTI file. Message= " + e.getMessage(), e);
        }
        return null;
    }
}
