/**
 *
 */
package org.ginga.tools.pipeline;

import org.apache.log4j.Logger;
import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.util.Constants.LacMode;

import com.tinkerpop.pipes.PipeFunction;

public class SpectrumModeFilterPipe implements PipeFunction<ObservationModeDetails, Boolean> {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(SpectrumModeFilterPipe.class);

    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.PipeFunction#compute(java.lang.Object)
     */
    @Override
    public Boolean compute(ObservationModeDetails obsMode) {
        LacMode mode = obsMode.getLacMode();
        if (mode == null)
            return Boolean.FALSE;
        if (mode.equals(LacMode.MPC1) || mode.equals(LacMode.MPC2)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
