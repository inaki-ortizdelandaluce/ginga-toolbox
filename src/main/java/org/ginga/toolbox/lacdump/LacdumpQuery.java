package org.ginga.toolbox.lacdump;

import org.ginga.toolbox.util.Constants.BitRate;
import org.ginga.toolbox.util.Constants.LacMode;

public class LacdumpQuery {

    private String startTime;

    private String endTime;

    private LacMode mode;

    private String targetName;

    private Double minCutOffRigidity;

    private Double minElevation;
    
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
     * @return the minCutOffRigidity
     */
    public Double getMinCutOffRigidity() {
        return this.minCutOffRigidity;
    }

    /**
     * @param minCutOffRigidity the minCutOffRigidity to set
     */
    public void setMinCutOffRigidity(Double minCutOffRigidity) {
        this.minCutOffRigidity = minCutOffRigidity;
    }

	/**
     * @return the minElevation
     */
    public Double getMinElevation() {
        return this.minElevation;
    }

    /**
     * @param minElevation the minElevation to set
     */
    public void setMinElevation(Double minElevation) {
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
