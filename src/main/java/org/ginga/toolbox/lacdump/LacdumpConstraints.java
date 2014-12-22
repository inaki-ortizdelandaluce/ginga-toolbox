package org.ginga.toolbox.lacdump;

import org.ginga.toolbox.util.Constants.BitRate;
import org.ginga.toolbox.util.Constants.LacMode;

public class LacdumpConstraints {

    private String startTime;

    private String endTime;

    private LacMode mode;

    private String targetName;

    private double minRigidity;

    private double minElevation;

    private BitRate bitRate;

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

    /**
     * @return the mode
     */
    public LacMode getMode() {
        return this.mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(LacMode mode) {
        this.mode = mode;
    }

    /**
     * @return the targetName
     */
    public String getTargetName() {
        return this.targetName;
    }

    /**
     * @param targetName the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the minRigidity
     */
    public double getMinRigidity() {
        return this.minRigidity;
    }

    /**
     * @param minRigidity the minRigidity to set
     */
    public void setMinRigidity(double minRigidity) {
        this.minRigidity = minRigidity;
    }

    /**
     * @return the minElevation
     */
    public double getMinElevation() {
        return this.minElevation;
    }

    /**
     * @param minElevation the minElevation to set
     */
    public void setMinElevation(double minElevation) {
        this.minElevation = minElevation;
    }

    /**
     * @return the bitRate
     */
    public BitRate getBitRate() {
        return this.bitRate;
    }

    /**
     * @param bitRate the bitRate to set
     */
    public void setBitRate(BitRate bitRate) {
        this.bitRate = bitRate;
    }

}
