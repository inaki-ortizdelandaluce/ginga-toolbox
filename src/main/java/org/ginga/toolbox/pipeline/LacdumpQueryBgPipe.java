package org.ginga.toolbox.pipeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.ObservationBgEntity;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;
import org.ginga.toolbox.target.SimbadTargetResolver;
import org.ginga.toolbox.target.TargetCoordinates;
import org.ginga.toolbox.target.TargetNotResolvedException;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacdumpQueryBgPipe extends
        AbstractPipe<SingleModeTargetObservation, LacdumpQuery> implements
        TransformPipe<SingleModeTargetObservation, LacdumpQuery> {

	private final static Logger log = Logger.getLogger(LacdumpQueryBgPipe.class);
	
    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.AbstractPipe#processNextStart()
     */
    @Override
    protected LacdumpQuery processNextStart() throws NoSuchElementException {
    	LacdumpQuery query = new LacdumpQuery();
    	DataReductionEnv env = GingaToolboxEnv.getInstance().getDataReductionEnv();
        SingleModeTargetObservation targetObservation = this.starts.next();
    	
    	try {
    		// get suggested background for this target observation
    		Set<ObservationBgEntity> bgSet = null;
    		ObservationDao dao = new ObservationDaoImpl();
        	ObservationEntity observation = dao.findById(targetObservation.getObsId());
        	bgSet = observation.getObsBgSet();
        	
        	// set LACDUMP files for suggested background
        	List<String> lacdumpFileList = new ArrayList<>();
        	if(bgSet != null) {
        		Iterator<ObservationBgEntity> it = bgSet.iterator();
        		while (it.hasNext()) {
        			lacdumpFileList.add(it.next().getLacdumpFile());
        		}
			}
        	query.setLacdumpFiles(lacdumpFileList);
            
        	// set sky region for suggested background 
        	String target = targetObservation.getTarget();
        	TargetCoordinates coords = new SimbadTargetResolver().resolve(target);
        	query.setSkyAnnulus(coords.getRaDeg(), coords.getDecDeg(), 
        			env.getSkyAnnulusInnerRadii(), 
        			env.getSkyAnnulusOuterRadii());
            
            // set rigidity and elevation
        	query.setMinCutOffRigidity(env.getCutOffRigidityMin());
            query.setMinElevation(env.getElevationMin());
            
		} catch (ObservationDaoException e) {
			log.error("Error generating LACDUMP query for background region file", e);
			return null;
		} catch (TargetNotResolvedException e) {
			log.error("Error resolving target name to define background sky annulus", e);
			return null;
		}
        return query;
    }

}
