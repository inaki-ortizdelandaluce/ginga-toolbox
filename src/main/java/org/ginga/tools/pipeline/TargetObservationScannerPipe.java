package org.ginga.tools.pipeline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LacdumpSfEntity;
import org.ginga.tools.lacdump.dao.LacdumpDao;
import org.ginga.tools.lacdump.dao.LacdumpDaoException;
import org.ginga.tools.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.tools.observation.ObservationEntity;
import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.observation.dao.ObservationDao;
import org.ginga.tools.observation.dao.ObservationDaoException;
import org.ginga.tools.observation.dao.impl.ObservationDaoImpl;
import org.ginga.tools.util.Constants;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class TargetObservationScannerPipe extends AbstractPipe<String, List<ObservationEntity>>
        implements TransformPipe<String, List<ObservationEntity>> {

    private static Logger log = Logger.getLogger(TargetObservationScannerPipe.class);

    @Override
    protected List<ObservationEntity> processNextStart() throws NoSuchElementException {
        String target = this.starts.next();

        // find observation list by target
        ObservationDao obsDao = new ObservationDaoImpl();
        List<ObservationEntity> obsList = null;
        try {
            obsList = obsDao.findListByTarget(target);
            // obsList = new ArrayList<>();
            // obsList.add(obsDao.findListByTarget(target).get(1));
            log.info(obsList.size() + " " + target + " observation(s) found");
        } catch (ObservationDaoException e) {
            log.error(target + " observation(s) could not be found", e);
        }

        // find available LAC modes and date ranges for each observation
        LacdumpDao lacdumpDao = new LacdumpDaoImpl();
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ObservationEntity obsEntity : obsList) {
            log.info("Scanning observation " + obsEntity.getSequenceNumber() + "...");
            // find available LAC modes
            String startTime = dateFmt.format(obsEntity.getStartTime());
            String endTime = dateFmt.format(obsEntity.getEndTime());
            List<String> modes = new ArrayList<String>();
            try {
                modes = lacdumpDao.findModes(target, startTime, endTime,
                        Constants.DEFAULT_MIN_ELEVATION, Constants.DEFAULT_MIN_RIGIDITY);
            } catch (LacdumpDaoException e) {
                log.error("Modes for target " + target + " could not be found", e);
            }

            // find date ranges for each mode
            List<LacdumpSfEntity> sfList = new ArrayList<LacdumpSfEntity>();
            ObservationModeDetails modeDetails = null;
            for (String mode : modes) {
                try {
                    sfList = lacdumpDao.findSfList(mode, target, startTime, endTime,
                            Constants.DEFAULT_MIN_ELEVATION, Constants.DEFAULT_MIN_RIGIDITY);
                } catch (LacdumpDaoException e) {
                    log.error("Modes for target " + target + " could not be found", e);
                }
                if (sfList.size() > 0) {
                    String modeStartTime = dateFmt.format(sfList.get(0).getDate());
                    String modeEndTime = dateFmt.format(sfList.get(sfList.size() - 1).getDate());
                    log.info("[" + mode + ", " + modeStartTime + ", " + endTime + "]");
                    modeDetails = new ObservationModeDetails();
                    modeDetails.setObsId(obsEntity.getId());
                    modeDetails.setTarget(target);
                    modeDetails.setMode(mode);
                    modeDetails.setStartTime(modeStartTime);
                    modeDetails.setEndTime(modeEndTime);
                    // add observation mode to observation summary
                    obsEntity.addAvailableModeDetails(modeDetails);
                }
            }
        }
        return obsList;
    }
}
