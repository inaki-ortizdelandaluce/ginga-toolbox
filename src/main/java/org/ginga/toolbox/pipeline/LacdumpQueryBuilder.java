/**
 *
 */
package org.ginga.toolbox.pipeline;

import java.util.NoSuchElementException;

import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.InputParameters;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.observation.SingleModeTargetObservation;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacdumpQueryBuilder extends AbstractPipe<SingleModeTargetObservation, LacdumpQuery>
        implements TransformPipe<SingleModeTargetObservation, LacdumpQuery> {

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
        InputParameters input = GingaToolboxEnv.getInstance().getInputParameters();
        query.setBitRate(input.getBitRate());
        query.setMinCutOffRigidity(input.getCutOffRigidityMin());
        query.setMinElevation(input.getElevationMin());
        return query;
    }

}
