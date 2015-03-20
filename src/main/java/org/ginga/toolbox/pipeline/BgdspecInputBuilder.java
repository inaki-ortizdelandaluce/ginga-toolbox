package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.bgdspec.BgdspecInputModel;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.gti.GingaGtiWriter;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class BgdspecInputBuilder extends AbstractPipe<LacdumpQuery, BgdspecInputModel> implements
TransformPipe<LacdumpQuery, BgdspecInputModel> {

    private final static Logger log = Logger.getLogger(BgdspecInputBuilder.class);

    public BgdspecInputBuilder() {
    }

    /*
     * Receives a LacdumpQuery, creates a GTI/Region file and finally emits a BgdspecInputModel
     * referencing such file
     */
    @Override
    protected BgdspecInputModel processNextStart() throws NoSuchElementException {
        try {
            LacdumpQuery query = this.starts.next();

            // set working directory
            File workingDir = new File(GingaToolboxEnv.getInstance().getWorkingDir());
            if (!workingDir.exists()) {
                workingDir.mkdirs();
            }
            log.debug("Working directory " + workingDir.getAbsolutePath());

            String prefix = query.getTargetName().replace(" ", "");

            // build empty GTI file
            log.debug("Generating GTI file for background data");
            File gtiFile = new File(workingDir, FileUtil.nextFileName(workingDir, prefix + "_BGD",
                    "DATA"));
            log.debug("GTI file " + gtiFile.getPath());

            // query entities matching the criteria
            LacdumpDao dao = new LacdumpDaoImpl();
            List<LacdumpSfEntity> sfList = dao.findSfList(query);
            log.info("LACDUMP query:" + query.toString());
            log.info("LACDUMP query executed successfully. " + sfList.size() + " result(s) found");

            if (sfList.size() > 0) {
                // save matching results into a GTI file
                GingaGtiWriter gtiWriter = new GingaGtiWriter();
                gtiWriter.writeToFile(query.getTargetName(), sfList, true, false, gtiFile);
                log.info("GTI file " + gtiFile.getPath() + " written successfully");

                // emit lacspec input model
                BgdspecInputModel inputModel = new BgdspecInputModel();
                DataReductionEnv dataReductionEnv = GingaToolboxEnv.getInstance()
                        .getDataReductionEnv();
                inputModel.setPsFileName(FileUtil.nextFileName(workingDir, prefix + "_bgdspec_bgd",
                        "ps"));
                inputModel.setMonitorFileName(FileUtil.nextFileName(workingDir, prefix
                        + "_MONI_BGD", "SPEC"));
                inputModel.setBitRate(dataReductionEnv.getBitRate());
                inputModel.setLacMode(query.getMode());
                inputModel.setMinElevation(dataReductionEnv.getElevationMin());
                inputModel.setMaxElevation(dataReductionEnv.getElevationMax());
                inputModel.setMinRigidity(dataReductionEnv.getCutOffRigidityMin());
                inputModel.setMaxRigidity(dataReductionEnv.getCutOffRigidityMax());
                inputModel.setDeadTimeCorrection(dataReductionEnv.getDeadTimeCorrection());
                inputModel.setChannelToEnergy(dataReductionEnv.getChannelToEnergyConversion());
                inputModel.setAce(dataReductionEnv.getAttitudeMode());
                inputModel.setCounter1(dataReductionEnv.getLacCounter1());
                inputModel.setCounter2(dataReductionEnv.getLacCounter2());
                inputModel.setCounter3(dataReductionEnv.getLacCounter3());
                inputModel.setCounter4(dataReductionEnv.getLacCounter4());
                inputModel.setCounter5(dataReductionEnv.getLacCounter5());
                inputModel.setCounter6(dataReductionEnv.getLacCounter6());
                inputModel.setCounter7(dataReductionEnv.getLacCounter7());
                inputModel.setCounter8(dataReductionEnv.getLacCounter8());
                inputModel.setMixedMode(dataReductionEnv.isLacMixedMode());
                inputModel.setSpectralFileName(FileUtil.nextFileName(workingDir, prefix + "_BGD",
                        "SPEC"));
                inputModel.setRegionFileName(gtiFile.getName());
                return inputModel;
            }
        } catch (IOException | LacdumpDaoException e) {
            log.error("Error generating GTI file. Message= " + e.getMessage(), e);
        }
        return null;
    }
}
