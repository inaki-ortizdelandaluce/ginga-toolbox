/**
 *
 */
package org.ginga.toolbox.pipeline;

import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.LacModeTargetObservation;
import org.ginga.toolbox.util.Constants.LacMode;

import com.tinkerpop.pipes.PipeFunction;

public class SpectrumModeFilter implements PipeFunction<LacModeTargetObservation, Boolean> {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(SpectrumModeFilter.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.tinkerpop.pipes.PipeFunction#compute(java.lang.Object)
     */
    @Override
    public Boolean compute(LacModeTargetObservation observation) {
        LacMode mode = observation.getLacMode();
        if (mode == null)
            return Boolean.FALSE;
        if (mode.equals(LacMode.MPC1) || mode.equals(LacMode.MPC2) || mode.equals(LacMode.MPC3)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
