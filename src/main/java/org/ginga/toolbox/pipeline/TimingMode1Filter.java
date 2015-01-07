/**
 *
 */
package org.ginga.toolbox.pipeline;

import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.util.Constants.LacMode;

import com.tinkerpop.pipes.PipeFunction;

public class TimingMode1Filter implements PipeFunction<SingleModeTargetObservation, Boolean> {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(TimingMode1Filter.class);

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
        if (mode.equals(LacMode.MPC3)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}