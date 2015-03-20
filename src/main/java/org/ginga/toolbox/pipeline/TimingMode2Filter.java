/**
 *
 */
package org.ginga.toolbox.pipeline;

import org.apache.log4j.Logger;
import org.ginga.toolbox.util.Constants.LacMode;

import com.tinkerpop.pipes.PipeFunction;

public class TimingMode2Filter implements PipeFunction<PipelineInput, Boolean> {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(TimingMode2Filter.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.tinkerpop.pipes.PipeFunction#compute(java.lang.Object)
     */
    @Override
    public Boolean compute(PipelineInput observation) {
        LacMode mode = observation.getLacMode();
        if (mode == null)
            return Boolean.FALSE;
        if (mode.equals(LacMode.PC)) {
            // && !observation.getStartTime().equals(observation.getEndTime())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
