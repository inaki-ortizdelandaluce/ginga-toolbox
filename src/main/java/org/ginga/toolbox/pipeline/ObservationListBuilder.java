package org.ginga.toolbox.pipeline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;
import org.ginga.toolbox.util.TimeUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class ObservationListBuilder extends
AbstractPipe<String, Map<ObservationEntity, List<SingleModeTargetObservation>>> implements
TransformPipe<String, Map<ObservationEntity, List<SingleModeTargetObservation>>> {

    private static Logger log = Logger.getLogger(ObservationListBuilder.class);

    @Override
    protected Map<ObservationEntity, List<SingleModeTargetObservation>> processNextStart()
            throws NoSuchElementException {
        String target = this.starts.next();
        Map<ObservationEntity, List<SingleModeTargetObservation>> map = new LinkedHashMap<ObservationEntity, List<SingleModeTargetObservation>>();
        // read environment
        DataReductionEnv dataReductionEnv = GingaToolboxEnv.getInstance().getDataReductionEnv();
        double minElevation = dataReductionEnv.getElevationMin();
        double minCutOffRigidity = dataReductionEnv.getCutOffRigidityMin();

        // find observation list by target
        ObservationDao obsDao = new ObservationDaoImpl();
        List<ObservationEntity> obsList = null;
        try {
            obsList = obsDao.findListByTarget(target);
            log.info(obsList.size() + " " + target + " observation(s) found");
        } catch (ObservationDaoException e) {
            log.error(target + " observation(s) could not be found", e);
        }

        // find available LAC modes and date ranges for each observation
        LacdumpDao lacdumpDao = new LacdumpDaoImpl();
        SimpleDateFormat dateFmt = TimeUtil.DATE_FORMAT_DATABASE;
        List<SingleModeTargetObservation> lacModeObsList = null;
        for (ObservationEntity obsEntity : obsList) {
            log.debug("Scanning observation " + obsEntity.getSequenceNumber() + "...");
            // find available LAC modes
            String startTime = dateFmt.format(obsEntity.getStartTime());
            String endTime = dateFmt.format(obsEntity.getEndTime());
            List<String> modes = new ArrayList<String>();
            try {
                modes = lacdumpDao.findModes(target, startTime, endTime, minElevation,
                        minCutOffRigidity);
            } catch (LacdumpDaoException e) {
                log.error("Modes for target " + target + " could not be found", e);
            }

            // find date ranges for each mode
            List<LacdumpSfEntity> sfList = new ArrayList<LacdumpSfEntity>();
            lacModeObsList = new ArrayList<SingleModeTargetObservation>();
            SingleModeTargetObservation lacModeObs = null;
            for (String mode : modes) {
                try {
                    sfList = lacdumpDao.findSfList(mode, target, startTime, endTime, minElevation,
                            minCutOffRigidity);
                } catch (LacdumpDaoException e) {
                    log.error("Modes for target " + target + " could not be found", e);
                }
                if (sfList.size() > 0) {
                    String modeStartTime = dateFmt.format(sfList.get(0).getDate());
                    String modeEndTime = dateFmt.format(sfList.get(sfList.size() - 1).getDate());
                    log.debug("[" + mode + ", " + modeStartTime + ", " + modeEndTime + "]");
                    lacModeObs = new SingleModeTargetObservation();
                    lacModeObs.setTarget(target);
                    lacModeObs.setMode(mode);
                    lacModeObs.setStartTime(modeStartTime);
                    lacModeObs.setEndTime(modeEndTime);
                    // add LAC single mode target observation to list
                    lacModeObsList.add(lacModeObs);
                }
            }
            map.put(obsEntity, lacModeObsList);
        }
        return map;
    }
}
