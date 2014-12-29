/**
 *
 */
package org.ginga.toolbox.pipeline;

import java.util.NoSuchElementException;

import org.ginga.toolbox.lacdump.LacdumpConstraints;
import org.ginga.toolbox.observation.TargetSingleModeObservation;
import org.ginga.toolbox.util.Constants;
import org.ginga.toolbox.util.Constants.BitRate;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacdumpConstraintsPipe extends
        AbstractPipe<TargetSingleModeObservation, LacdumpConstraints> implements
        TransformPipe<TargetSingleModeObservation, LacdumpConstraints> {

    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.AbstractPipe#processNextStart()
     */
    @Override
    protected LacdumpConstraints processNextStart() throws NoSuchElementException {
        TargetSingleModeObservation targetObservation = this.starts.next();
        LacdumpConstraints constraints = new LacdumpConstraints();
        constraints.setTargetName(targetObservation.getTarget());
        constraints.setStartTime(targetObservation.getStartTime());
        constraints.setEndTime(targetObservation.getEndTime());
        constraints.setBitRate(BitRate.H);
        constraints.setMode(targetObservation.getLacMode());
        constraints.setMinRigidity(Constants.DEFAULT_MIN_RIGIDITY);
        constraints.setMinElevation(Constants.DEFAULT_MIN_ELEVATION);
        return constraints;
    }

}
