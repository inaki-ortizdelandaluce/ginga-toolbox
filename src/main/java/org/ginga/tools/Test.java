package org.ginga.tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LacdumpConstraints;
import org.ginga.tools.lacdump.LacdumpSfEntity;
import org.ginga.tools.lacdump.dao.LacdumpDao;
import org.ginga.tools.lacdump.dao.LacdumpDaoException;
import org.ginga.tools.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.tools.observation.ObservationEntity;
import org.ginga.tools.observation.dao.ObservationDao;
import org.ginga.tools.observation.dao.ObservationDaoException;
import org.ginga.tools.observation.dao.impl.ObservationDaoImpl;
import org.ginga.tools.pipeline.LacqrdfitsInputPipe;
import org.ginga.tools.pipeline.LacqrdfitsPipe;
import org.ginga.tools.pipeline.TargetObservationListPipe;
import org.ginga.tools.spectrum.LacqrdfitsInputModel;
import org.ginga.tools.util.Constants.LacMode;
import org.ginga.tools.util.DateUtil;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.Pipeline;

public class Test {

    private static final Logger log = Logger.getLogger(Test.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpecExtractorHayashida pipeline = new SpecExtractorHayashida();
        pipeline.execute("GS2000+25");
    }

    public static void scanObservations(String[] args) {
        Pipe<String, List<ObservationEntity>> obsPipe = new TargetObservationListPipe();
        obsPipe.setStarts(Arrays.asList("GS2000+25"));
        if (obsPipe.hasNext()) {
            List<ObservationEntity> obsSummary = obsPipe.next();
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
        LacdumpConstraints constraints = new LacdumpConstraints();
        constraints.setMode(LacMode.MPC2);
        constraints.setTargetName("GS2000+25");
        constraints.setStartTime("1988-04-30 04:40:07");
        constraints.setEndTime("1988-04-30 04:53:23");
        constraints.setMinElevation(5.0);
        constraints.setMinRigidity(10.0);

        LacqrdfitsInputPipe pipe1 = new LacqrdfitsInputPipe();
        LacqrdfitsPipe pipe2 = new LacqrdfitsPipe();
        Pipeline<LacdumpConstraints, File> specHayashidaPipeline = new Pipeline<LacdumpConstraints, File>(
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

        Pipe<LacqrdfitsInputModel, File> pipe2 = new LacqrdfitsPipe();
        log.info("Starting SpectrumHayashidaPipeFunction");
        pipe2.setStarts(Arrays.asList(model));
        while (pipe2.hasNext()) {
            pipe2.next();
        }
        log.info("SpectrumHayashidaPipeFunction completed");

        LacdumpConstraints constraints = new LacdumpConstraints();
        constraints.setMode(LacMode.MPC3);
        constraints.setTargetName("GS2000+25");
        constraints.setStartTime("1988-05-02 01:34:31");
        constraints.setEndTime("1988-05-02 01:34:31");
        constraints.setMinElevation(5.0);
        constraints.setMinRigidity(10.0);

        Pipe<LacdumpConstraints, LacqrdfitsInputModel> pipe1 = new LacqrdfitsInputPipe();
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
        SimpleDateFormat dateFmt = DateUtil.DATE_FORMAT_DATABASE;
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
