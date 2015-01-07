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
import org.ginga.toolbox.lacspec.LacspecInputModel;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public abstract class LacspecInputBuilder extends AbstractPipe<LacdumpQuery, LacspecInputModel>
        implements TransformPipe<LacdumpQuery, LacspecInputModel> {

    private final static Logger log = Logger.getLogger(LacspecInputBuilder.class);

    public LacspecInputBuilder() {
    }

    public abstract boolean isBackground();

    public abstract BgSubtractionMethod getBgSubtractionMethod();

    public abstract String getBgFileName();

    public abstract boolean sudSort();

    public abstract boolean backgroundCorrection();

    public abstract boolean aspectCorrection();

    /**
     * Returns the data unit to be applied to the correction
     * @return 0 for counts, 1 for counts/sec and 2 for counts/sec/cm2
     */
    public abstract int getDataUnit();

    /*
     * Receives a LacdumpQuery, creates a GTI/Region file and finally emits a TiminfilfitsInputModel
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
                log.debug("Generating GTI file for background data");
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
            log.info("LACDUMP query:" + query.toString());
            log.info("LACDUMP query executed successfully. " + sfList.size() + " result(s) found");

            if (sfList.size() > 0) {
                // save matching results into a GTI file
                GtiFileWriter gtiWriter = new GtiFileWriter();
                gtiWriter.writeToFile(query.getTargetName(), sfList, true, gtiFile);
                log.info("GTI file " + gtiFile.getPath() + " written successfully");

                // emit lacspec input model
                LacspecInputModel inputModel = new LacspecInputModel();
                InputParameters input = GingaToolboxEnv.getInstance().getInputParameters();
                inputModel.setHasBackground(!isBackground());
                if (!isBackground()) { // on-source with background correction
                    inputModel.setBgMethod(getBgSubtractionMethod());
                    inputModel.setBgFileName(getBgFileName());
                    inputModel.setBgSubFileNumber(input.getBgSubFileNumber());
                    inputModel.setStartTime(query.getStartTime());
                    inputModel.setPsFileName(FileUtil.nextFileName("lacspec", query.getStartTime(),
                            query.getMode(), "ps"));
                    inputModel.setSpectralFileName(FileUtil.nextFileName("SPEC",
                            query.getStartTime(), query.getMode(), "FILE"));
                    inputModel.setMonitorFileName(FileUtil.nextFileName("MONI",
                            query.getStartTime(), query.getMode(), "SPEC"));
                } else { // background
                    String prefix = query.getTargetName().replace(" ", "");
                    inputModel.setPsFileName(FileUtil.nextFileName(workingDir, prefix
                            + "_lacspec_bgd", "ps"));
                    inputModel.setSpectralFileName(FileUtil.nextFileName(workingDir, prefix
                            + "_BGD", "SPEC"));
                    inputModel.setMonitorFileName(FileUtil.nextFileName(workingDir, prefix
                            + "_MONI_BGD", "SPEC"));
                }
                inputModel.setBitRate(input.getBitRate());
                inputModel.setLacMode(query.getMode());
                inputModel.setMinElevation(input.getElevationMin());
                inputModel.setMaxElevation(input.getElevationMax());
                inputModel.setMinRigidity(input.getCutOffRigidityMin());
                inputModel.setMaxRigidity(input.getCutOffRigidityMax());
                inputModel.setBgCorrection(backgroundCorrection());
                inputModel.setAspectCorrection(aspectCorrection());
                inputModel.setDeadTimeCorrection(input.getDeadTimeCorrection());
                inputModel.setChannelToEnergy(input.getChannelToEnergyConversion());
                inputModel.setDataUnit(getDataUnit());
                inputModel.setSudSort(sudSort());
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
                inputModel.setRegionFileName(gtiFile.getName());
                return inputModel;
            }
        } catch (IOException | LacdumpDaoException e) {
            log.error("Error generating GTI file. Message= " + e.getMessage(), e);
        }
        return null;
    }
}
