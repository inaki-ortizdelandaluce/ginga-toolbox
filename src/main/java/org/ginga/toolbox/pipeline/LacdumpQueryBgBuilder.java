package org.ginga.toolbox.pipeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.InputParameters;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.ObservationBgEntity;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;
import org.ginga.toolbox.target.TargetEntity;
import org.ginga.toolbox.target.dao.TargetDaoException;
import org.ginga.toolbox.target.dao.impl.TargetDaoImpl;
import org.ginga.toolbox.util.Constants.LacMode;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacdumpQueryBgBuilder extends AbstractPipe<SingleModeTargetObservation, LacdumpQuery>
        implements TransformPipe<SingleModeTargetObservation, LacdumpQuery> {

    private final static Logger log = Logger.getLogger(LacdumpQueryBgBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.tinkerpop.pipes.AbstractPipe#processNextStart()
     */
    @Override
    protected LacdumpQuery processNextStart() throws NoSuchElementException {
        LacdumpQuery query = new LacdumpQuery();
        InputParameters input = GingaToolboxEnv.getInstance().getInputParameters();
        SingleModeTargetObservation targetObservation = this.starts.next();

        try {
            // set target and flag as background
            query.setTargetName(targetObservation.getTarget());
            query.setBackground(true);
            query.setMode(LacMode.MPC1); // MPC1 for accurate background subtraction

            // get suggested background for this target observation
            Set<ObservationBgEntity> bgSet = null;
            ObservationDao dao = new ObservationDaoImpl();
            ObservationEntity observation = dao.findById(targetObservation.getObsId());
            bgSet = observation.getObsBgSet();
            // set LACDUMP files for suggested background
            List<String> lacdumpFileList = new ArrayList<>();
            if (bgSet != null) {
                Iterator<ObservationBgEntity> it = bgSet.iterator();
                while (it.hasNext()) {
                    lacdumpFileList.add(it.next().getLacdumpFile());
                }
            }
            query.setLacdumpFiles(lacdumpFileList);

            // set sky region for suggested background
            String target = targetObservation.getTarget();
            TargetEntity targetEntity = new TargetDaoImpl().findByName(target);
            if (targetEntity != null) {
                if (targetEntity.getRaDegB1950() == 0 && targetEntity.getDecDegB1950() == 0) {
                    throw new TargetDaoException(
                            "Target found in database but coordinates not resolved");
                }
                query.setSkyAnnulus(targetEntity.getRaDegB1950(), targetEntity.getDecDegB1950(),
                        input.getSkyAnnulusInnerRadii(), input.getSkyAnnulusOuterRadii());
            }
            // rigidity and elevation
            query.setMinCutOffRigidity(input.getCutOffRigidityMin());
            query.setMinElevation(input.getElevationMin());

        } catch (ObservationDaoException e) {
            log.error("Error generating LACDUMP query for background region file", e);
            return null;
        } catch (TargetDaoException e) {
            log.error("Error resolving target name to define background sky annulus", e);
            return null;
        }
        return query;
    }

}
