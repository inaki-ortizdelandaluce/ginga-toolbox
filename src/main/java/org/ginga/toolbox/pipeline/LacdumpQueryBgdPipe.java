/**
 *
 */
package org.ginga.toolbox.pipeline;

import java.util.NoSuchElementException;

import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.util.Constants.BitRate;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacdumpQueryBgdPipe extends
        AbstractPipe<SingleModeTargetObservation, LacdumpQuery> implements
        TransformPipe<SingleModeTargetObservation, LacdumpQuery> {

    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.AbstractPipe#processNextStart()
     */
    @Override
    protected LacdumpQuery processNextStart() throws NoSuchElementException {
        @SuppressWarnings("unused")
		SingleModeTargetObservation targetObservation = this.starts.next();
        
        LacdumpQuery query = new LacdumpQuery();
    	
        // apply values from observation
        // observation.getStartTime());
        // observation.getEndTime());
        // distance
        // mode?
        query.setBitRate(BitRate.ANY);
        
        // apply values from environment
    	DataReductionEnv env = GingaToolboxEnv.getInstance().getDataReductionEnv();
        query.setMinCutOffRigidity(env.getCutOffRigidityMin());
        query.setMinElevation(env.getElevationMin());
        return query;
    }

}
