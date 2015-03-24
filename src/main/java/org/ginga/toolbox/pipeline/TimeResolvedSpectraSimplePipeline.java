package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.gti.GingaGtiWriter;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.lacspec.LacspecInputModel;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.Pipeline;

public class TimeResolvedSpectraSimplePipeline {

    private static final Logger LOGGER = Logger.getLogger(TimeResolvedSpectraSimplePipeline.class);

    private double timeBin;

    public TimeResolvedSpectraSimplePipeline(double timeBin) {
        this.timeBin = timeBin;
    }

    public void run(LacModeTargetObservation obs) throws Exception {
        File inputFile = obs.getBackgroundFile();
        if (inputFile != null) {
            try {
                // copy file to working directory
                File bgSpectrumFile = new File(GingaToolboxEnv.getInstance().getWorkingDir(),
                        inputFile.getName());
                FileUtil.copy(inputFile, bgSpectrumFile);
                extractSpectra(obs, bgSpectrumFile);
            } catch (IOException e) {
                LOGGER.error("Error copying background file to working directory", e);
            }
        } else {
            SpectrumBackgroundPipeline bgPipeline = new SpectrumBackgroundPipeline();
            bgPipeline.run(Arrays.asList(obs));
            extractSpectra(obs, bgPipeline.next());
        }
    }

    private void extractSpectra(LacModeTargetObservation obs, final File bgSpectrumFile)
            throws Exception {
        // build query
        LacdumpQueryBuilder queryBuilder = new LacdumpQueryBuilder();
        queryBuilder.setStarts(Arrays.asList(obs));
        LacdumpQuery query = queryBuilder.next();

        // execute query
        LacdumpDao dao = new LacdumpDaoImpl();
        List<LacdumpSfEntity> sfList = dao.findSfList(query);
        LOGGER.info("LACDUMP query:" + query.toString());
        LOGGER.info("LACDUMP query executed successfully. " + sfList.size() + " result(s) found");

        GingaGtiWriter gtiWriter = new GingaGtiWriter();
        GingaToolboxEnv env = GingaToolboxEnv.getInstance();

        File outputDirectory = new File(env.getWorkingDir());
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
        List<File> gtiFileList = null;
        for (LacdumpSfEntity sf : sfList) {
            // write GTI files split by time bin
            LOGGER.info("Writing GTIs for Super Frame " + sf.getSequenceNumber() + " with "
                    + this.timeBin + " second(s) bin...");
            gtiFileList = gtiWriter.writeToFileSplitByFrameBin(obs.getTarget(), sf, this.timeBin,
                    false, outputDirectory);
            for (File gtiFile : gtiFileList) {
                extractSpectrum(bgSpectrumFile, gtiFile, Enum.valueOf(LacMode.class, obs.getMode()));
            }
        }
    }

    private File extractSpectrum(File bgSpectrumFile, File gtiFile, LacMode mode) {
        DataReductionEnv dataReductionEnv = GingaToolboxEnv.getInstance().getDataReductionEnv();

        String baseName = FileUtil.splitFileBaseAndExtension(gtiFile)[0];

        // build lacspec input model
        LOGGER.info("Building input file for GTI " + gtiFile.getName() + " ...");
        LacspecInputModel inputModel = new LacspecInputModel();
        inputModel.setHasBackground(true);
        inputModel.setBgMethod(BgSubtractionMethod.SIMPLE);
        inputModel.setBgFileName(bgSpectrumFile.getName());
        inputModel.setBgSubFileNumber(dataReductionEnv.getBgSubFileNumber());
        inputModel.setStartTime(baseName); // FIXME should be an identifier
        inputModel.setPsFileName("lacspec_" + baseName + "_" + mode + ".ps");
        inputModel.setSpectralFileName("SPEC_" + baseName + "_" + mode + ".FILE");
        inputModel.setMonitorFileName("MONI_" + baseName + "_" + mode + ".SPEC");
        inputModel.setBitRate(dataReductionEnv.getBitRate());
        inputModel.setLacMode(mode);
        inputModel.setMinElevation(dataReductionEnv.getElevationMin());
        inputModel.setMaxElevation(dataReductionEnv.getElevationMax());
        inputModel.setMinRigidity(dataReductionEnv.getCutOffRigidityMin());
        inputModel.setMaxRigidity(dataReductionEnv.getCutOffRigidityMax());
        inputModel.setBgCorrection(true);
        inputModel.setAspectCorrection(true);
        inputModel.setDeadTimeCorrection(dataReductionEnv.getDeadTimeCorrection());
        inputModel.setChannelToEnergy(dataReductionEnv.getChannelToEnergyConversion());
        inputModel.setDataUnit(1); // counts / second
        inputModel.setSudSort(false);
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
        inputModel.setRegionFileName(gtiFile.getName());

        Pipe<LacspecInputModel, File> lacspec = new LacspecRunner();
        Pipe<File, File> lac2xspec = new Lac2xspecRunner();

        Pipeline<LacspecInputModel, File> specExtractor = new Pipeline<LacspecInputModel, File>(
                lacspec, lac2xspec);
        specExtractor.setStarts(Arrays.asList(inputModel));
        return specExtractor.next();
    }
}