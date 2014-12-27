/**
 *
 */
package org.ginga.toolbox.pipeline;

import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.TargetObservationSingleMode;
import org.ginga.toolbox.util.Constants.LacMode;

import com.tinkerpop.pipes.PipeFunction;

public class SpectrumModeFilterPipe implements PipeFunction<TargetObservationSingleMode, Boolean> {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(SpectrumModeFilterPipe.class);

    /*
     * (non-Javadoc)
     *
     * @see com.tinkerpop.pipes.PipeFunction#compute(java.lang.Object)
     */
    @Override
    public Boolean compute(TargetObservationSingleMode obsMode) {
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
