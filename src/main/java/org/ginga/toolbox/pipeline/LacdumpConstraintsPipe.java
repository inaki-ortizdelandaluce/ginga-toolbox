/**
 *
 */
package org.ginga.toolbox.pipeline;

import java.util.NoSuchElementException;

import org.ginga.toolbox.lacdump.LacdumpConstraints;
import org.ginga.toolbox.observation.TargetObservationSingleMode;
import org.ginga.toolbox.util.Constants;
import org.ginga.toolbox.util.Constants.BitRate;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacdumpConstraintsPipe extends
        AbstractPipe<TargetObservationSingleMode, LacdumpConstraints> implements
        TransformPipe<TargetObservationSingleMode, LacdumpConstraints> {

    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.AbstractPipe#processNextStart()
     */
    @Override
    protected LacdumpConstraints processNextStart() throws NoSuchElementException {
        TargetObservationSingleMode modeDetails = this.starts.next();
        LacdumpConstraints constraints = new LacdumpConstraints();
        constraints.setTargetName(modeDetails.getTarget());
        constraints.setStartTime(modeDetails.getStartTime());
        constraints.setEndTime(modeDetails.getEndTime());
        constraints.setBitRate(BitRate.H);
        constraints.setMode(modeDetails.getLacMode());
        constraints.setMinRigidity(Constants.DEFAULT_MIN_RIGIDITY);
        constraints.setMinElevation(Constants.DEFAULT_MIN_ELEVATION);
        return constraints;
    }

}
