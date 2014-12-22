package org.ginga.toolbox.observation;

import org.ginga.toolbox.util.Constants.LacMode;

public class ObservationModeDetails {

    private long obsId;
    private String target;
    private String mode;
    private String startTime;
    private String endTime;

    /**
     * @return the obsId
     */
    public long getObsId() {
        return this.obsId;
    }

    /**
     * @param obsId the obsId to set
     */
    public void setObsId(long obsId) {
        this.obsId = obsId;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return this.mode;
    }

    public LacMode getLacMode() {
        if (this.mode.equals(LacMode.MPC1.toString())) {
            return LacMode.MPC1;
        } else if (this.mode.equals(LacMode.MPC2.toString())) {
            return LacMode.MPC2;
        } else if (this.mode.equals(LacMode.MPC3.toString())) {
            return LacMode.MPC3;
        } else if (this.mode.equals(LacMode.PCHK.toString())) {
            return LacMode.PCHK;
        } else if (this.mode.equals(LacMode.ACS.toString())) {
            return LacMode.ACS;
        }
        return null;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return this.startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return this.endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
