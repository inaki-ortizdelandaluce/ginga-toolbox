/**
 *
 */
package org.ginga.toolbox.pipeline;

import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.util.Constants.LacMode;

import com.tinkerpop.pipes.PipeFunction;

public class LacModeFilterPipe implements PipeFunction<SingleModeTargetObservation, Boolean> {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(LacModeFilterPipe.class);

    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.PipeFunction#compute(java.lang.Object)
     */
    @Override
    public Boolean compute(SingleModeTargetObservation observation) {
        LacMode mode = observation.getLacMode();
        if (mode == null)
            return Boolean.FALSE;
        if (mode.equals(LacMode.MPC1) || mode.equals(LacMode.MPC2)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
