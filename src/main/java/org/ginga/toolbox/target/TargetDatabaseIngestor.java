package org.ginga.toolbox.target;

import java.util.Set;

import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;
import org.ginga.toolbox.target.SimbadTargetResolver.SimbadObject;
import org.ginga.toolbox.target.SimbadTargetResolver.TargetNotResolvedException;
import org.ginga.toolbox.target.dao.TargetDao;
import org.ginga.toolbox.target.dao.TargetDaoException;
import org.ginga.toolbox.target.dao.impl.TargetDaoImpl;

public class TargetDatabaseIngestor {

    private final static Logger log = Logger.getLogger(TargetDatabaseIngestor.class);

    public TargetDatabaseIngestor() {

    }

    public void ingest() {
        ObservationDao obsDao = new ObservationDaoImpl();
        TargetDao targetDao = new TargetDaoImpl();
        SimbadTargetResolver targetResolver = new SimbadTargetResolver();
        Set<String> targetList;
        try {
            // find all targets in observations
            targetList = obsDao.findAllTargets();
            TargetEntity targetEntity;
            // save each target
            for (String target : targetList) {
                log.info("Target " + target);
                targetEntity = new TargetEntity();
                targetEntity.setTargetName(target);
                // resolve target name into coordinates
                try {
                    SimbadObject obj = targetResolver.resolve(target);
                    targetEntity.setRaDegB1950(obj.getRaDeg());
                    targetEntity.setDecDegB1950(obj.getDecDeg());
                    targetEntity.setObjectType(obj.getObjectType());
                } catch (TargetNotResolvedException e) {
                    log.warn("Could not resolve target " + target);
                }
                // save
                targetDao.save(targetEntity);
            }
        } catch (ObservationDaoException e) {
            log.error("Error retrieving the list of targets available", e);
        } catch (TargetDaoException e) {
            log.error("Error saving target into database", e);
        }
    }
}