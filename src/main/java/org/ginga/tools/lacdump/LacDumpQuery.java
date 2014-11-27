package org.ginga.tools.lacdump;

import java.util.Date;

public class LacDumpQuery {

    private Date startTime;

    private Date endTime;

    private String mode;

    private String targetName;

    private double minRigidity;

    private double minElevation;

    private String bitRate;

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return this.startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return this.endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return this.mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
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
    public String getBitRate() {
        return this.bitRate;
    }

    /**
     * @param bitRate the bitRate to set
     */
    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

}
