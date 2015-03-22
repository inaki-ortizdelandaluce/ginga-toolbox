/**
 *
 */
package org.ginga.toolbox.pipeline;

import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.LacModeTargetObservation;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacdumpQueryBuilder extends AbstractPipe<LacModeTargetObservation, LacdumpQuery>
        implements TransformPipe<LacModeTargetObservation, LacdumpQuery> {

    private static final Logger LOGGER = Logger.getLogger(LacdumpQuery.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.tinkerpop.pipes.AbstractPipe#processNextStart()
     */
    @Override
    protected LacdumpQuery processNextStart() throws NoSuchElementException {
        LOGGER.debug("Entering into LacdumpQueryBuilder.processNextStart...");
        LacModeTargetObservation targetObservation = this.starts.next();
        LacdumpQuery query = new LacdumpQuery();

        // apply values from observation
        query.setTargetName(targetObservation.getTarget());
        query.setStartTime(targetObservation.getStartTime());
        query.setEndTime(targetObservation.getEndTime());
        query.setMode(targetObservation.getLacMode());

        // apply values from environment
        DataReductionEnv dataReductionEnv = GingaToolboxEnv.getInstance().getDataReductionEnv();
        query.setBitRate(dataReductionEnv.getBitRate());
        query.setMinCutOffRigidity(dataReductionEnv.getCutOffRigidityMin());
        query.setMinElevation(dataReductionEnv.getElevationMin());
        return query;
    }

}
