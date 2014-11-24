package org.ginga.tools.gti;

public class TargetFk4Coordinates {

    private double raDeg;
    private double decDeg;
    private String targetName;

    /**
     * @return the raDeg
     */
    public double getRaDeg() {
        return this.raDeg;
    }

    /**
     * @param degrees the degrees to set
     */
    public void setRaDeg(double degrees) {
        this.raDeg = degrees;
    }

    /**
     * @return the decDeg
     */
    public double getDecDeg() {
        return this.decDeg;
    }

    /**
     * @param degrees the degrees to set
     */
    public void setDecDeg(double degrees) {
        this.decDeg = degrees;
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
}
