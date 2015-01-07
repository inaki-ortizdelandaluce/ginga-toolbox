package org.ginga.toolbox.tim2filfits;

import org.ginga.toolbox.util.Constants.BitRate;

public class Tim2filfitsInputModel {

    private double minRigidity;
    private double maxRigidity;
    private double minTransmission;
    private String bitRate;
    private int ace;
    private double minElevation;
    private double maxElevation;
    private int bgCorrection;
    private int aspectCorrection;
    private int deadTimeCorrection;
    private int channelToEnergy;
    private int dataUnit;
    private String pcLine1;
    private String pcLine2;
    private String pcLine3;
    private String pcLine4;
    private String timingFileName;
    private String targetLine;
    private String dataLines;

    /**
     * @return the pcLine1
     */
    public String getPcLine1() {
        return this.pcLine1;
    }

    /**
     * @param pcLine1 the pcLine1 to set
     */
    public void setPcLine1(String pcLine1) {
        this.pcLine1 = pcLine1;
    }

    /**
     * @return the pcLine2
     */
    public String getPcLine2() {
        return this.pcLine2;
    }

    /**
     * @param pcLine2 the pcLine2 to set
     */
    public void setPcLine2(String pcLine2) {
        this.pcLine2 = pcLine2;
    }

    /**
     * @return the pcLine3
     */
    public String getPcLine3() {
        return this.pcLine3;
    }

    /**
     * @param pcLine3 the pcLine3 to set
     */
    public void setPcLine3(String pcLine3) {
        this.pcLine3 = pcLine3;
    }

    /**
     * @return the pcLine4
     */
    public String getPcLine4() {
        return this.pcLine4;
    }

    /**
     * @param pcLine4 the pcLine4 to set
     */
    public void setPcLine4(String pcLine4) {
        this.pcLine4 = pcLine4;
    }

    private double timeResolution;
    private String startTime; // carried over for next steps in pipeline

    public double getMinRigidity() {
        return this.minRigidity;
    }

    public void setMinRigidity(double minRigidity) {
        this.minRigidity = minRigidity;
    }

    public double getMaxRigidity() {
        return this.maxRigidity;
    }

    public void setMaxRigidity(double maxRigidity) {
        this.maxRigidity = maxRigidity;
    }

    public double getMinTransmission() {
        return this.minTransmission;
    }

    public void setMinTransmission(double minTransmission) {
        this.minTransmission = minTransmission;
    }

    public String getBitRate() {
        return this.bitRate;
    }

    public void setBitRate(BitRate bitRate) {
        switch (bitRate) {
        case ANY:
        default:
            this.bitRate = "ANY";
            break;
        case H:
            this.bitRate = "HI";
            break;
        case M:
            this.bitRate = "MED";
            break;
        case L:
            this.bitRate = "LOW";
            break;
        }
    }

    public int getAce() {
        return this.ace;
    }

    public void setAce(int ace) {
        this.ace = ace;
    }

    public double getMinElevation() {
        return this.minElevation;
    }

    public void setMinElevation(double minElevation) {
        this.minElevation = minElevation;
    }

    public double getMaxElevation() {
        return this.maxElevation;
    }

    public void setMaxElevation(double maxElevation) {
        this.maxElevation = maxElevation;
    }

    public int getBgCorrection() {
        return this.bgCorrection;
    }

    public void setBgCorrection(int bgCorrection) {
        this.bgCorrection = bgCorrection;
    }

    public void setBgCorrection(boolean enable) {
        this.bgCorrection = (enable) ? 1 : 0;
    }

    public int getAspectCorrection() {
        return this.aspectCorrection;
    }

    public void setAspectCorrection(int aspectCorrection) {
        this.aspectCorrection = aspectCorrection;
    }

    public void setAspectCorrection(boolean enable) {
        this.aspectCorrection = (enable) ? 1 : 0;
    }

    public int getDeadTimeCorrection() {
        return this.deadTimeCorrection;
    }

    public void setDeadTimeCorrection(int deadTimeCorrection) {
        this.deadTimeCorrection = deadTimeCorrection;
    }

    public void setDeadTimeCorrection(boolean enable) {
        this.deadTimeCorrection = (enable) ? 1 : 0;
    }

    public int getCounterToEnergy() {
        return this.channelToEnergy;
    }

    public void setChannelToEnergy(int channelToEnergy) {
        this.channelToEnergy = channelToEnergy;
    }

    public void setChannelToEnergy(boolean enable) {
        this.channelToEnergy = (enable) ? 1 : 0;
    }

    public int getDataUnit() {
        return this.dataUnit;
    }

    public void setDataUnit(int dataUnit) {
        this.dataUnit = dataUnit;
    }

    public void setDataUnit(boolean enable) {
        this.dataUnit = (enable) ? 1 : 0;
    }

    /**
     * @return the timeResolution
     */
    public double getTimeResolution() {
        return this.timeResolution;
    }

    /**
     * @param timeResolution the timeResolution to set
     */
    public void setTimeResolution(double timeResolution) {
        this.timeResolution = timeResolution;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the timingFileName
     */
    public String getTimingFileName() {
        return this.timingFileName;
    }

    /**
     * @param timingFileName the timingFileName to set
     */
    public void setTimingFileName(String timingFileName) {
        this.timingFileName = timingFileName;
    }

    /**
     * @return the targetLine
     */
    public String getTargetLine() {
        return targetLine;
    }

    /**
     * @param targetLine the targetLine to set
     */
    public void setTargetLine(String targetLine) {
        this.targetLine = targetLine;
    }

    /**
     * @return the dataLines
     */
    public String getDataLines() {
        return dataLines;
    }

    /**
     * @param dataLines the dataLines to set
     */
    public void setDataLines(String dataLines) {
        this.dataLines = dataLines;
    }
}
