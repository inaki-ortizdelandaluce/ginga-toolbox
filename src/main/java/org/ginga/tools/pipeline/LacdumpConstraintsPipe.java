/**
 *
 */
package org.ginga.tools.pipeline;

import java.util.NoSuchElementException;

import org.ginga.tools.lacdump.LacdumpConstraints;
import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.util.Constants;
import org.ginga.tools.util.Constants.BitRate;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacdumpConstraintsPipe extends
        AbstractPipe<ObservationModeDetails, LacdumpConstraints> implements
        TransformPipe<ObservationModeDetails, LacdumpConstraints> {

    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.AbstractPipe#processNextStart()
     */
    @Override
    protected LacdumpConstraints processNextStart() throws NoSuchElementException {
        ObservationModeDetails modeDetails = this.starts.next();
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
