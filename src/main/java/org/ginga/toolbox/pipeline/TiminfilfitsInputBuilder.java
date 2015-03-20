package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.gti.GingaGtiWriter;
import org.ginga.toolbox.gti.GtiWriter;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.timinfilfits.TiminfilfitsInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.FileUtil;
import org.ginga.toolbox.util.TimeUtil;

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

    public abstract boolean sudSort();

    /*
     * Receives a LacdumpQuery, creates a GTI/Region file and finally emits a Tim2filfitsInputModel
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
            log.info("LACDUMP query:" + query.toString());
            log.info("LACDUMP query executed successfully. " + sfList.size() + " result(s) found");

            if (sfList.size() > 0) {
                // save matching results into a GTI file
                GingaGtiWriter gtiWriter = new GingaGtiWriter();
                gtiWriter.writeToFile(query.getTargetName(), sfList, false, false, gtiFile);
                log.info("GTI file " + gtiFile.getPath() + " written successfully");
                // save matching results into a standard GTI fits file also
                File gtiFitsFile = new File(workingDir, FileUtil.nextFileName("GTI",
                        query.getStartTime(), query.getMode(), "fits"));
                GtiWriter gtiFitsWriter = new GtiWriter();
                gtiFitsWriter.writeToFits(sfList, gtiFitsFile);
                log.debug("GTI file " + gtiFitsFile.getPath() + " written successfully");

                // emit timinfilfits input model
                TiminfilfitsInputModel inputModel = new TiminfilfitsInputModel();
                DataReductionEnv dataReductionEnv = GingaToolboxEnv.getInstance()
                        .getDataReductionEnv();
                inputModel.setStartTime(query.getStartTime());
                inputModel.setBitRate(dataReductionEnv.getBitRate());
                inputModel.setAce(dataReductionEnv.getAttitudeMode());
                inputModel.setMinElevation(dataReductionEnv.getElevationMin());
                inputModel.setMaxElevation(dataReductionEnv.getElevationMax());
                inputModel.setMinRigidity(dataReductionEnv.getCutOffRigidityMin());
                inputModel.setMaxRigidity(dataReductionEnv.getCutOffRigidityMax());
                inputModel.setBgCorrection(true);
                inputModel.setAspectCorrection(true);
                inputModel.setDeadTimeCorrection(dataReductionEnv.getDeadTimeCorrection());
                inputModel.setChannelToEnergy(dataReductionEnv.getChannelToEnergyConversion());
                inputModel.setDataUnit(0); // counts
                inputModel.setBgMethod(getBgSubtractionMethod());
                inputModel.setBgFileName(getBgFileName());
                inputModel.setBgSubFileNumber(dataReductionEnv.getBgSubFileNumber());
                inputModel.setSudsort(sudSort());
                inputModel.setPhsel1(dataReductionEnv.getPhselLine1());
                inputModel.setPhsel2(dataReductionEnv.getPhselLine2());
                inputModel.setPhsel3(dataReductionEnv.getPhselLine3());
                inputModel.setPhsel4(dataReductionEnv.getPhselLine4());
                inputModel.setPhsel5(dataReductionEnv.getPhselLine5());
                inputModel.setPhsel6(dataReductionEnv.getPhselLine6());
                inputModel.setPhsel7(dataReductionEnv.getPhselLine7());
                inputModel.setPhsel8(dataReductionEnv.getPhselLine8());
                inputModel.setPhsel9(dataReductionEnv.getPhselLine9());
                inputModel.setPhsel10(dataReductionEnv.getPhselLine10());
                inputModel.setTimingFileName(FileUtil.nextFileName("TIMING", query.getStartTime(),
                        query.getMode(), "fits"));
                inputModel.setLacMode(query.getMode());
                inputModel.setCounter1(dataReductionEnv.getLacCounter1());
                inputModel.setCounter2(dataReductionEnv.getLacCounter2());
                inputModel.setCounter3(dataReductionEnv.getLacCounter3());
                inputModel.setCounter4(dataReductionEnv.getLacCounter4());
                inputModel.setCounter5(dataReductionEnv.getLacCounter5());
                inputModel.setCounter6(dataReductionEnv.getLacCounter6());
                inputModel.setCounter7(dataReductionEnv.getLacCounter7());
                inputModel.setCounter8(dataReductionEnv.getLacCounter8());
                inputModel.setMixedMode(dataReductionEnv.isLacMixedMode());
                inputModel.setTimeResolution(TimeUtil.getTimeResolution(
                        dataReductionEnv.getBitRate(), query.getMode()));
                inputModel.setRegionFileName(gtiFile.getName());
                return inputModel;
            }
        } catch (IOException | LacdumpDaoException e) {
            log.error("Error generating GTI file. Message= " + e.getMessage(), e);
        }
        return null;
    }
}
