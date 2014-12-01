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
import org.ginga.tools.observation.ObservationMode;
import org.ginga.tools.observation.ObservationEntity;
import org.ginga.tools.observation.dao.ObservationDao;
import org.ginga.tools.observation.dao.ObservationDaoException;
import org.ginga.tools.observation.dao.impl.ObservationDaoImpl;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class ObservationScannerPipe extends AbstractPipe<String, List<ObservationEntity>> 
	implements TransformPipe<String, List<ObservationEntity>> {

	private static Logger log = Logger.getLogger(ObservationScannerPipe.class);
	
	@Override
	protected List<ObservationEntity> processNextStart()
			throws NoSuchElementException {
		if(this.starts.hasNext()) {
			String target = this.starts.next();
			
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
	        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        for (ObservationEntity obsEntity : obsList) {
	        	log.info("Scanning observation " + obsEntity.getSequenceNumber() + "...");
				// find available LAC modes
				String startTime = dateFmt.format(obsEntity.getStartTime());
				String endTime = dateFmt.format(obsEntity.getEndTime());
				List<String> modes = new ArrayList<String>();
				try {
					modes = lacdumpDao.findModes(target,
							startTime, 
							endTime, 
							5.0, 10.0); // EELV > 5.0, RIG > 10.0
				} catch (LacdumpDaoException e) {
					log.error("Modes for target " + target + " could not be found", e);
				} 
				
				// find date ranges for each mode
				List<LacdumpSfEntity> sfList = new ArrayList<LacdumpSfEntity>();
				ObservationMode obsMode = null;
				for(String mode: modes) {
					log.info(mode + " found between " + startTime + " and " + endTime);
					try {
						sfList = lacdumpDao.findSfList(mode, target, 
								startTime, endTime, 
								5.0, 10.0); // EELV > 5.0, RIG > 10.0
					} catch (LacdumpDaoException e) {
						log.error("Modes for target " + target + " could not be found", e);
					} 
	                if (sfList.size() > 0) {
	                	String modeStartTime = dateFmt.format(sfList.get(0).getDate());
	                	String modeEndTime = dateFmt.format(sfList.get(sfList.size() - 1).getDate());
	                    log.info("  " + mode+ " Start Time " + modeStartTime);
	                    log.info("  " + mode+ " End Time " + modeEndTime);
	                    obsMode = new ObservationMode();
	                    obsMode.setObsId(obsEntity.getId());
	                    obsMode.setMode(mode);
	                    obsMode.setStartTime(modeStartTime);
	                    obsMode.setEndTime(modeEndTime);
	                    // add observation mode to observation summary
	                    obsEntity.addAvailableMode(obsMode);
	                }
				}
				log.info("\n");
			}
	        return obsList;
		} else {
			throw new NoSuchElementException();
		}
	}

}
