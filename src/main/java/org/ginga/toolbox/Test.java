package org.ginga.toolbox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ginga.toolbox.command.ObservationListPrinterCmd;
import org.ginga.toolbox.command.TargetSpectraExtractorCmd;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;
import org.ginga.toolbox.pipeline.LacqrdfitsInputBuilder;
import org.ginga.toolbox.pipeline.LacqrdfitsRunner;
import org.ginga.toolbox.pipeline.ObservationListBuilder;
import org.ginga.toolbox.target.SimbadTargetResolver;
import org.ginga.toolbox.target.SimbadTargetResolver.SimbadObject;
import org.ginga.toolbox.target.SimbadTargetResolver.TargetNotResolvedException;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.TimeUtil;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.Pipeline;

public class Test {

    private static final Logger log = Logger.getLogger(Test.class);

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        String target = "VELA X-1"; // "GS1124-68"; // "GS2000+25";

        SimbadObject obj;
        try {
            obj = new SimbadTargetResolver().resolve(target);
            DecimalFormat formatter = new DecimalFormat("#.####");
            log.info("RA " + formatter.format(obj.getRaDeg()));
            log.info("DEC " + formatter.format(obj.getDecDeg()));

            LacdumpQuery query = new LacdumpQuery();
            query.setLacdumpFiles(Arrays.asList("J880226", "J880224", "J880225", "J880222",
                    "J880220"));
            query.setMinCutOffRigidity(10.0);
            query.setMinElevation(5.0);
            query.setSkyAnnulus(obj.getRaDeg(), obj.getDecDeg(), 2.5, 3.5);

            LacdumpDao dao = new LacdumpDaoImpl();
            List<LacdumpSfEntity> sfList = dao.findSfList(query);
            log.info(sfList.size() + " entries found");

        } catch (TargetNotResolvedException e) {
            log.error("Could not resolve target " + target, e);
        } catch (LacdumpDaoException e) {
            log.error("Error querying LACDUMP database table " + target, e);
        }
    }

    public static void printTargetObservations() throws IOException {
        String target = "GS2000+25"; // "GS1124-68"; // "GS2000+25";

        // extract all spectra
        TargetSpectraExtractorCmd.extractSpectra(target, BgSubtractionMethod.HAYASHIDA);
        // write observation list
        File workingDir = new File(GingaToolboxEnv.getInstance().getWorkingDir());
        if (!workingDir.exists()) {
            workingDir.mkdirs();
        }
        File file = new File(workingDir, "observation.list");
        FileWriter writer = new FileWriter(file);
        ObservationListPrinterCmd printer = new ObservationListPrinterCmd(writer);
        printer.printSpectralModes(target);
    }

    public static void scanObservations(String[] args) {
        Pipe<String, Map<ObservationEntity, List<LacModeTargetObservation>>> obsPipe = new ObservationListBuilder();
        obsPipe.setStarts(Arrays.asList("GS2000+25"));
        if (obsPipe.hasNext()) {
            Map<ObservationEntity, List<LacModeTargetObservation>> obsSummary = obsPipe.next();
            log.info(obsSummary.size() + " observation(s) scanned");
        }
    }

    public static void findModes() {
        try {
            LacdumpDao dao = new LacdumpDaoImpl();
            List<String> modes;
            modes = dao.findModes("GS2000+25", "1988-04-30 04:40:07", "1988-04-30 04:53:23", 5.0,
                    10.0);
            for (String mode : modes) {
                log.info("Mode " + mode);
            }
        } catch (LacdumpDaoException e) {
            log.error(e);
        }
    }

    public static void samplePipeExec2() {
        LacdumpQuery constraints = new LacdumpQuery();
        constraints.setMode(LacMode.MPC2);
        constraints.setTargetName("GS2000+25");
        constraints.setStartTime("1988-04-30 04:40:07");
        constraints.setEndTime("1988-04-30 04:53:23");
        constraints.setMinElevation(5.0);
        constraints.setMinCutOffRigidity(10.0);

        LacqrdfitsInputBuilder pipe1 = new LacqrdfitsInputBuilder() {

            @Override
            public int getTimingBinWidth() {
                return 128;
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
        LacqrdfitsRunner pipe2 = new LacqrdfitsRunner();
        Pipeline<LacdumpQuery, File> specHayashidaPipeline = new Pipeline<LacdumpQuery, File>(
                pipe1, pipe2);
        specHayashidaPipeline.setStarts(Arrays.asList(constraints));
        File file = specHayashidaPipeline.next();
        log.info("Spectrum " + file.getPath() + " generated successfully");
    }

    public static void samplePipeExec() {
        LacqrdfitsInputModel model = new LacqrdfitsInputModel();
        model.setLacMode("MPC2");
        model.setPsFileName("gs2000+25_lacqrd.ps");
        model.setMinElevation(5.0);
        model.setRegionFileName("GS2000+25_REGION.DATA");
        model.setSpectralFileName("GS2000+25_SPEC_lacqrd.FILE");
        model.setTimingFileName("GS2000+25_TIMING.fits");

        Pipe<LacqrdfitsInputModel, File> pipe2 = new LacqrdfitsRunner();
        log.info("Starting SpectrumHayashidaPipeFunction");
        pipe2.setStarts(Arrays.asList(model));
        while (pipe2.hasNext()) {
            pipe2.next();
        }
        log.info("SpectrumHayashidaPipeFunction completed");

        LacdumpQuery constraints = new LacdumpQuery();
        constraints.setMode(LacMode.MPC3);
        constraints.setTargetName("GS2000+25");
        constraints.setStartTime("1988-05-02 01:34:31");
        constraints.setEndTime("1988-05-02 01:34:31");
        constraints.setMinElevation(5.0);
        constraints.setMinCutOffRigidity(10.0);

        Pipe<LacdumpQuery, LacqrdfitsInputModel> pipe1 = new LacqrdfitsInputBuilder() {

            @Override
            public int getTimingBinWidth() {
                return 128;
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
        log.info("Starting GoodTimeIntervalPipeFunction");
        pipe1.setStarts(Arrays.asList(constraints));
        while (pipe1.hasNext()) {
            pipe1.next();
        }
        log.info("GoodTimeIntervalPipeFunction completed");

    }

    public static void sampleSfLookup() {
        String target = "GS2000+25";
        // find observation list by target
        ObservationDao obsLogDao = new ObservationDaoImpl();
        List<ObservationEntity> obsList = null;
        try {
            obsList = obsLogDao.findListByTarget(target);
            log.info(obsList.size() + " " + target + " observation(s) found");
        } catch (ObservationDaoException e) {
            log.error(target + " observation(s) not found", e);
        }
        // find start/end time for MPC3
        LacdumpDao lacDumpDao = new LacdumpDaoImpl();
        SimpleDateFormat dateFmt = TimeUtil.DATE_FORMAT_DATABASE;
        String startTime, endTime;
        for (ObservationEntity obs : obsList) {
            startTime = dateFmt.format(obs.getStartTime());
            endTime = dateFmt.format(obs.getEndTime());
            List<LacdumpSfEntity> sfList;
            try {
                sfList = lacDumpDao.findSfList("MPC3", target, startTime, endTime, 10.0, 5.0);
                if (sfList.size() > 0) {
                    log.info("MPC3 found between " + startTime + " and " + endTime);
                    log.info("  MPC3 Start Time " + dateFmt.format(sfList.get(0).getDate()));
                    log.info("  MPC3 End Time "
                            + dateFmt.format(sfList.get(sfList.size() - 1).getDate()));
                    log.info("\n");
                }
            } catch (LacdumpDaoException e) {
                log.error("Could not find sf item(s) between " + startTime + " and " + endTime);
            }
        }

    }

}
