/**
 *
 */
package org.ginga.tools.pipeline;

import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.util.Constants;

import com.tinkerpop.pipes.PipeFunction;

public class SpectrumModeFilterPipe implements PipeFunction<ObservationModeDetails, Boolean> {

    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.PipeFunction#compute(java.lang.Object)
     */
    @Override
    public Boolean compute(ObservationModeDetails obsMode) {
        String mode = obsMode.getMode();
        if (mode == null)
            return Boolean.FALSE;
        if (mode.equals(Constants.LacMode.MPC1) || mode.equals(Constants.LacMode.MPC2)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
