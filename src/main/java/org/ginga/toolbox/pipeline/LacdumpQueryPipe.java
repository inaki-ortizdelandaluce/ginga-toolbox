/**
 *
 */
package org.ginga.toolbox.pipeline;

import java.util.NoSuchElementException;

import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.SingleModeTargetObservation;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacdumpQueryPipe extends
        AbstractPipe<SingleModeTargetObservation, LacdumpQuery> implements
        TransformPipe<SingleModeTargetObservation, LacdumpQuery> {

    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.AbstractPipe#processNextStart()
     */
    @Override
    protected LacdumpQuery processNextStart() throws NoSuchElementException {
        SingleModeTargetObservation targetObservation = this.starts.next();
        LacdumpQuery query = new LacdumpQuery();
    	
        // apply values from observation
        query.setTargetName(targetObservation.getTarget());
        query.setStartTime(targetObservation.getStartTime());
        query.setEndTime(targetObservation.getEndTime());
        query.setMode(targetObservation.getLacMode());

        // apply values from environment
    	DataReductionEnv env = GingaToolboxEnv.getInstance().getDataReductionEnv();
        query.setBitRate(env.getBitRate());
        query.setMinCutOffRigidity(env.getCutOffRigidityMin());
        query.setMinElevation(env.getElevationMin());
        return query;
    }

}
