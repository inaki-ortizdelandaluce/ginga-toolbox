package org.ginga.toolbox.observation;

import java.io.File;

import org.ginga.toolbox.util.Constants.LacMode;

public class LacModeTargetObservation {

    private long obsId;
    private String target;
    private String mode;
    private String startTime;
    private String endTime;
    private File backgroundFile;

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
        return Enum.valueOf(LacMode.class, this.mode);
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

    /**
     * @return the backgroundFile
     */
    public File getBackgroundFile() {
        return this.backgroundFile;
    }

    /**
     * @param backgroundFile the backgroundFile to set
     */
    public void setBackgroundFile(File backgroundFile) {
        this.backgroundFile = backgroundFile;
    }
}
