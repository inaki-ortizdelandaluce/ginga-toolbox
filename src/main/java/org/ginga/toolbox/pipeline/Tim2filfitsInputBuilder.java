package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.InputParameters;
import org.ginga.toolbox.gti.GingaGtiWriter;
import org.ginga.toolbox.gti.GtiWriter;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.tim2filfits.Tim2filfitsInputModel;
import org.ginga.toolbox.util.FileUtil;
import org.ginga.toolbox.util.TimeUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class Tim2filfitsInputBuilder extends AbstractPipe<LacdumpQuery, Tim2filfitsInputModel>
        implements TransformPipe<LacdumpQuery, Tim2filfitsInputModel> {

    private final static Logger log = Logger.getLogger(Tim2filfitsInputBuilder.class);

    public Tim2filfitsInputBuilder() {
    }

    /*
     * Receives a LacdumpQuery, creates a GTI/Region file and finally emits a Tim2filfitsInputModel
     * referencing such file
     */
    @Override
    protected Tim2filfitsInputModel processNextStart() throws NoSuchElementException {
        try {
            LacdumpQuery query = this.starts.next();

            // set working directory
            File workingDir = new File(GingaToolboxEnv.getInstance().getWorkingDir());
            if (!workingDir.exists()) {
                workingDir.mkdirs();
            }
            log.debug("Working directory " + workingDir.getAbsolutePath());

            // query entities matching the criteria
            LacdumpDao dao = new LacdumpDaoImpl();
            List<LacdumpSfEntity> sfList = dao.findSfList(query);
            log.info("LACDUMP query:" + query.toString());
            log.info("LACDUMP query executed successfully. " + sfList.size() + " result(s) found");

            if (sfList.size() > 0) {
                // save matching results into a Ginga GTI file
                GingaGtiWriter gtiWriter = new GingaGtiWriter();
                String gtiString = gtiWriter.writeToString(query.getTargetName(), sfList, false,
                        false);
                // save matching results into a standard GTI fits file also
                File gtiFitsFile = new File(workingDir, FileUtil.nextFileName("GTI",
                        query.getStartTime(), query.getMode(), "fits"));
                GtiWriter gtiFitsWriter = new GtiWriter();
                gtiFitsWriter.writeToFits(sfList, gtiFitsFile);
                log.debug("GTI file " + gtiFitsFile.getPath() + " written successfully");

                // emit timinfilfits input model
                Tim2filfitsInputModel inputModel = new Tim2filfitsInputModel();
                InputParameters input = GingaToolboxEnv.getInstance().getInputParameters();
                inputModel.setStartTime(query.getStartTime());
                inputModel.setBitRate(input.getBitRate());
                inputModel.setAce(input.getAttitudeMode());
                inputModel.setMinElevation(input.getElevationMin());
                inputModel.setMaxElevation(input.getElevationMax());
                inputModel.setMinRigidity(input.getCutOffRigidityMin());
                inputModel.setMaxRigidity(input.getCutOffRigidityMax());
                inputModel.setBgCorrection(false);
                inputModel.setAspectCorrection(false);
                inputModel.setDeadTimeCorrection(false);
                inputModel.setChannelToEnergy(false);
                inputModel.setDataUnit(0); // counts
                inputModel.setPcLine1(input.getPcLine1());
                inputModel.setPcLine2(input.getPcLine2());
                inputModel.setPcLine3(input.getPcLine3());
                inputModel.setPcLine4(input.getPcLine4());
                inputModel.setTimingFileName(FileUtil.nextFileName("TIMING", query.getStartTime(),
                        query.getMode(), "fits"));
                inputModel.setTimeResolution(TimeUtil.getTimeResolution(input.getBitRate(),
                        query.getMode()));
                inputModel.setGtiLines(gtiString);
                return inputModel;
            }
        } catch (IOException | LacdumpDaoException e) {
            log.error("Error generating GTI file. Message= " + e.getMessage(), e);
        }
        return null;
    }
}
