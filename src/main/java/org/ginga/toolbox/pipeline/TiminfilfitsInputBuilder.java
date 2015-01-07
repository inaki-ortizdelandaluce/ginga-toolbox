package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.InputParameters;
import org.ginga.toolbox.gti.GtiFileWriter;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.timinfilfits.TiminfilfitsInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public abstract class TiminfilfitsInputBuilder extends
        AbstractPipe<LacdumpQuery, TiminfilfitsInputModel> implements
        TransformPipe<LacdumpQuery, TiminfilfitsInputModel> {

    private final static Logger log = Logger.getLogger(TiminfilfitsInputBuilder.class);

    public TiminfilfitsInputBuilder() {
    }

    public abstract BgSubtractionMethod getBgSubtractionMethod();

    public abstract String getBgFileName();

    /*
     * Receives a LacdumpQuery, creates a GTI/Region file and finally emits a TiminfilfitsInputModel
     * referencing such file
     */
    @Override
    protected TiminfilfitsInputModel processNextStart() throws NoSuchElementException {
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
            log.debug("Generating GTI file for on-source data");
            gtiFile = new File(workingDir, FileUtil.nextFileName("REGION", query.getStartTime(),
                    query.getMode(), "DATA"));
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

                // emit timinfilfits input model
                TiminfilfitsInputModel inputModel = new TiminfilfitsInputModel();
                InputParameters input = GingaToolboxEnv.getInstance().getInputParameters();
                inputModel.setStartTime(query.getStartTime());
                inputModel.setBitRate(input.getBitRate());
                inputModel.setAce(input.getAttitudeMode());
                inputModel.setMinElevation(input.getElevationMin());
                inputModel.setMaxElevation(input.getElevationMax());
                inputModel.setMinRigidity(input.getCutOffRigidityMin());
                inputModel.setMaxRigidity(input.getCutOffRigidityMax());
                inputModel.setBgCorrection(true);
                inputModel.setAspectCorrection(true);
                inputModel.setDeadTimeCorrection(input.getDeadTimeCorrection());
                inputModel.setChannelToEnergy(input.getChannelToEnergyConversion());
                inputModel.setDataUnit(0); // counts
                inputModel.setBgMethod(getBgSubtractionMethod());
                inputModel.setBgFileName(getBgFileName());
                inputModel.setBgSubFileNumber(input.getBgSubFileNumber());
                inputModel.setPhsel1(input.getPhselLine1());
                inputModel.setPhsel2(input.getPhselLine2());
                inputModel.setPhsel3(input.getPhselLine3());
                inputModel.setPhsel4(input.getPhselLine4());
                inputModel.setPhsel5(input.getPhselLine5());
                inputModel.setPhsel6(input.getPhselLine6());
                inputModel.setPhsel7(input.getPhselLine7());
                inputModel.setPhsel8(input.getPhselLine8());
                inputModel.setPhsel9(input.getPhselLine9());
                inputModel.setPhsel10(input.getPhselLine10());
                inputModel.setSpectralFileName(FileUtil.nextFileName("TIMING",
                        query.getStartTime(), query.getMode(), "fits"));
                inputModel.setLacMode(query.getMode());
                inputModel.setCounter1(input.getLacCounter1());
                inputModel.setCounter2(input.getLacCounter2());
                inputModel.setCounter3(input.getLacCounter3());
                inputModel.setCounter4(input.getLacCounter4());
                inputModel.setCounter5(input.getLacCounter5());
                inputModel.setCounter6(input.getLacCounter6());
                inputModel.setCounter7(input.getLacCounter7());
                inputModel.setCounter8(input.getLacCounter8());
                inputModel.setMixedMode(input.isLacMixedMode());
                inputModel.setTimeResolution(0.078); // TODO
                inputModel.setRegionFileName(gtiFile.getName());
                return inputModel;
            }
        } catch (IOException | LacdumpDaoException e) {
            log.error("Error generating GTI file. Message= " + e.getMessage(), e);
        }
        return null;
    }
}
