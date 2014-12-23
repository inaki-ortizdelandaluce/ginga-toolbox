package org.ginga.toolbox.observation;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;
import org.ginga.toolbox.util.FileUtil;

public class ObservationBgDatabaseIngestor {

    private final static Logger log = Logger.getRootLogger();
    
    public static final String LACDUMP_BGD_DIRECTORY = "lacdump_bgd";
    public static final String LACDUMP_DIRECTORY = "lacdump";
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out
                    .println("Usage org.ginga.toolbox.observation.ObservationBgDatabaseIngestor <Ginga data directory>");
            System.exit(1);

        } else {
        	// check data directory exists
            File dataRootDir = new File(args[0]);
            if (!dataRootDir.exists()) {
                log.error("Directory " + dataRootDir.getPath() + " not found");
                System.exit(1);
            } else if (!dataRootDir.isDirectory()) {
                log.error(dataRootDir.getPath() + " is not a directory");
                System.exit(1);
            }
            ObservationDao dao = new ObservationDaoImpl();
            List<ObservationEntity> obsList;
			try {
			    // find all observations stored in the database
			    obsList = dao.findAll();
			    // update background information for all observations
			    File lacdumpDir;
			    String[] lacdumpFileNames;
			    for (ObservationEntity obs: obsList) {
			        if(obs.getSequenceNumber() % 10 == 9) { // ends with 9, bgd observation
			        	lacdumpDir = new File(dataRootDir, 
		            			String.valueOf(obs.getSequenceNumber() + File.separator + LACDUMP_DIRECTORY));
			        } else { // ends with 0, normal observation
			        	lacdumpDir = new File(dataRootDir, 
		            			String.valueOf(obs.getSequenceNumber() + File.separator + LACDUMP_BGD_DIRECTORY));
			        }	
		        	lacdumpFileNames = getFileNames(lacdumpDir.listFiles(new FileUtil.LacdumpFileFilter()));
		        	log.info(lacdumpFileNames.length + " background LACDUMP file(s) found for observation " + obs.getSequenceNumber());
		        	// build set of observation background entities
		        	Set<ObservationBgEntity> obsBgSet = new HashSet<ObservationBgEntity>();
		        	ObservationBgEntity obsBgEntity = null;
		        	for (int i = 0; i < lacdumpFileNames.length; i++) {
						obsBgEntity = new ObservationBgEntity();
						obsBgEntity.setObservation(obs);
						obsBgEntity.setLacdumpFile(lacdumpFileNames[i]);
						// add to set
						obsBgSet.add(obsBgEntity);
					}
		        	obs.setObsBgSet(obsBgSet);
		        	dao.update(obs);
		        	log.info("Observation " + obs.getSequenceNumber() + " updated with background information");
			    }
	            log.info("Background updated for " + obsList.size() + " observation(s) successfully");
			} catch (ObservationDaoException e) {
				log.error("Error searching/updating Ginga observations stored in the database. Message=" + e.getMessage(), e);
                System.exit(1);
			}
        }
    }
    
    private static String[] getFileNames(File[] files) {
    	String[] fileNames = new String[files.length];
    	for (int i = 0; i < files.length; i++) {
			fileNames[i] = files[i].getName();
		}
    	return fileNames;
    }
}
