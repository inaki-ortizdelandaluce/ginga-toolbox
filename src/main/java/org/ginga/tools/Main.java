package org.ginga.tools;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LacDumpSfEntity;
import org.ginga.tools.lacdump.dao.LacDumpDao;
import org.ginga.tools.lacdump.dao.LacDumpDaoException;
import org.ginga.tools.lacdump.dao.impl.LacDumpDaoImpl;
import org.ginga.tools.obslog.ObsLogEntity;
import org.ginga.tools.obslog.dao.ObsLogDao;
import org.ginga.tools.obslog.dao.ObsLogDaoException;
import org.ginga.tools.obslog.dao.impl.ObsLogDaoImpl;

public class Main {
	
	private static final Logger log = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String target = "GS2000+25";
		// find observation list by target
		ObsLogDao obsLogDao = new ObsLogDaoImpl();
		List<ObsLogEntity> obsList = null;
		try {
			obsList = obsLogDao.findListByTarget(target);
			log.info(obsList.size() + " " + target + " observation(s) found");
		} catch (ObsLogDaoException e) {
			log.error(target + " observation(s) not found", e);
		}
		// find start/end time for MPC3
		LacDumpDao lacDumpDao = new LacDumpDaoImpl();
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime, endTime;
		for(ObsLogEntity obs: obsList) {
			startTime = dateFmt.format(obs.getStartTime());
			endTime = dateFmt.format(obs.getEndTime());
			List<LacDumpSfEntity> sfList;
			try {
				sfList = lacDumpDao.findSfList("MPC3", target, startTime, endTime, 10.0, 5.0);
				if(sfList.size() > 0) {
				    log.info("MPC3 found between " + startTime + " and " + endTime); 
				    log.info("  MPC3 Start Time " + dateFmt.format(sfList.get(0).getDate())); 
				    log.info("  MPC3 End Time " + dateFmt.format(sfList.get(sfList.size()-1).getDate())); 
				    log.info("\n");
				}
			} catch (LacDumpDaoException e) {
                log.error("Could not find sf item(s) between " + startTime + " and " + endTime); 
			}
		}
		
	}

}
