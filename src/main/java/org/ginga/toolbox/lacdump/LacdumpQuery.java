package org.ginga.toolbox.lacdump;

import java.util.List;

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

    private SkyAnnulus skyAnnulus;

    private List<String> lacdumpFiles;

    private boolean isBackground = false;

    public class SkyAnnulus {

        private double targetRaDeg;
        private double targetDecDeg;
        private double innerRadiiDeg;
        private double outerRadiiDeg;

        public double getTargetRaDeg() {
            return this.targetRaDeg;
        }

        public void setTargetRaDeg(double targetRaDeg) {
            this.targetRaDeg = targetRaDeg;
        }

        public double getTargetDecDeg() {
            return this.targetDecDeg;
        }

        public void setTargetDecDeg(double targetDecDeg) {
            this.targetDecDeg = targetDecDeg;
        }

        public double getInnerRadiiDeg() {
            return this.innerRadiiDeg;
        }

        public void setInnerRadiiDeg(double radiiDeg) {
            this.innerRadiiDeg = radiiDeg;
        }

        public double getOuterRadiiDeg() {
            return this.outerRadiiDeg;
        }

        public void setOuterRadiiDeg(double radiiDeg) {
            this.outerRadiiDeg = radiiDeg;
        }
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

    /**
     * @return the skyAnnulus
     */
    public SkyAnnulus getSkyAnnulus() {
        return this.skyAnnulus;
    }

    /**
     * @param skyAnnulus the skyAnnulus to set
     */
    public void setSkyAnnulus(SkyAnnulus skyAnnulus) {
        this.skyAnnulus = skyAnnulus;
    }

    /**
     * 
     * @param raDeg right ascension of annulus centre in degrees (equinox 1950)
     * @param degDeg declination of annulus centre in degrees (equinox 1950)
     * @param innerRadiiDeg inner annulus radii in degrees
     * @param outerRadiiDeg outer annulus radii in degrees
     */
    public void setSkyAnnulus(double raDeg, double decDeg, double innerRadiiDeg,
            double outerRadiiDeg) {
        if (innerRadiiDeg > outerRadiiDeg) {
            throw new IllegalArgumentException(
                    "Inner radii is greater than outer radii in sky annulus");
        }
        this.skyAnnulus = new SkyAnnulus();
        this.skyAnnulus.setTargetRaDeg(raDeg);
        this.skyAnnulus.setTargetDecDeg(decDeg);
        this.skyAnnulus.setInnerRadiiDeg(innerRadiiDeg);
        this.skyAnnulus.setOuterRadiiDeg(outerRadiiDeg);
    }

    /**
     * @return the lacdumpFiles
     */
    public List<String> getLacdumpFiles() {
        return this.lacdumpFiles;
    }

    /**
     * @param lacdumpFiles the lacdumpFiles to set
     */
    public void setLacdumpFiles(List<String> lacdumpFiles) {
        this.lacdumpFiles = lacdumpFiles;
    }

    /**
     * @return the isBackground
     */
    public boolean isBackground() {
        return this.isBackground;
    }

    /**
     * @param isBackground the isBackground to set
     */
    public void setBackground(boolean isBackground) {
        this.isBackground = isBackground;
    }
}
