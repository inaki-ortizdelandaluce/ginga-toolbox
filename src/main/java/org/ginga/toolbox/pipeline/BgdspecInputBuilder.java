package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.bgdspec.BgdspecInputModel;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.InputParameters;
import org.ginga.toolbox.gti.GtiFileWriter;
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
            log.info("LACDUMP query executed successfully. " + sfList.size() + " result(s) found");

            if (sfList.size() > 0) {
                // save matching results into a GTI file
                GtiFileWriter gtiWriter = new GtiFileWriter();
                gtiWriter.writeToFile(query.getTargetName(), sfList, true, gtiFile);
                log.info("GTI file " + gtiFile.getPath() + " written successfully");

                // emit lacspec input model
                BgdspecInputModel inputModel = new BgdspecInputModel();
                InputParameters input = GingaToolboxEnv.getInstance().getInputParameters();
                inputModel.setPsFileName(FileUtil.nextFileName(workingDir, prefix + "_bgspec_bgd",
                        "ps"));
                inputModel.setMonitorFileName(FileUtil.nextFileName(workingDir, prefix
                        + "_MONI_BGD", "SPEC"));
                inputModel.setBitRate(input.getBitRate());
                inputModel.setLacMode(query.getMode());
                inputModel.setMinElevation(input.getElevationMin());
                inputModel.setMaxElevation(input.getElevationMax());
                inputModel.setMinRigidity(input.getCutOffRigidityMin());
                inputModel.setMaxRigidity(input.getCutOffRigidityMax());
                inputModel.setDeadTimeCorrection(input.getDeadTimeCorrection());
                inputModel.setChannelToEnergy(input.getChannelToEnergyConversion());
                inputModel.setAce(input.getAttitudeMode());
                inputModel.setCounter1(input.getLacCounter1());
                inputModel.setCounter2(input.getLacCounter2());
                inputModel.setCounter3(input.getLacCounter3());
                inputModel.setCounter4(input.getLacCounter4());
                inputModel.setCounter5(input.getLacCounter5());
                inputModel.setCounter6(input.getLacCounter6());
                inputModel.setCounter7(input.getLacCounter7());
                inputModel.setCounter8(input.getLacCounter8());
                inputModel.setMixedMode(input.isLacMixedMode());
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
