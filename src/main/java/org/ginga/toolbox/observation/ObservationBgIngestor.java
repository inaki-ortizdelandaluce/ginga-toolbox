package org.ginga.toolbox.observation;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;

public class ObservationBgIngestor {

    private final static Logger log = Logger.getRootLogger();
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out
                    .println("Usage org.ginga.toolbox.observation.ObservationBgIngestor <Ginga data directory>");
            System.exit(1);

        } else {

            File f = new File(args[0]);
            if (!f.exists()) {
                log.error("Directory " + f.getPath() + " not found");
                System.exit(1);
            } else if (!f.isDirectory()) {
                log.error(f.getPath() + " is not a directory");
                System.exit(1);
            }

            ObservationDao dao = new ObservationDaoImpl();
            List<ObservationEntity> obsList;
			try {
				obsList = dao.findAll();
	            for (ObservationEntity obs: obsList) {
	                log.info("Scanning sub-directory " + obs.getSequenceNumber());
	            }
	            log.info(obsList.size() + " observation(s) processed successfully");
			} catch (ObservationDaoException e) {
				log.error("Error searching for all Ginga observations stored in the database. Message=" + e.getMessage(), e);
                System.exit(1);
			}
        }
    }
}
