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
    
    private SkyAnnulus skyAnnulus;
    
    public class SkyAnnulus {
    	private double raB1950Deg;
    	private double decB1950Deg;
    	private double innerRadiiDeg;
    	private double outerRadiiDeg;
    	
		public double getRaB1950Deg() {
			return raB1950Deg;
		}
		public void setRaB1950Deg(double raB1950Deg) {
			this.raB1950Deg = raB1950Deg;
		}
		public double getDecB1950Deg() {
			return decB1950Deg;
		}
		public void setDecB1950Deg(double decB1950Deg) {
			this.decB1950Deg = decB1950Deg;
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
		return skyAnnulus;
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
	public void setSkyAnnulus(double raDeg, double decDeg, double innerRadiiDeg, double outerRadiiDeg) {
		if(innerRadiiDeg > outerRadiiDeg) {
			throw new IllegalArgumentException("Inner radii is greater than outer radii in sky annulus");
		}
		this.skyAnnulus = new SkyAnnulus();
		this.skyAnnulus.setRaB1950Deg(raDeg);
		this.skyAnnulus.setDecB1950Deg(decDeg);
		this.skyAnnulus.setInnerRadiiDeg(innerRadiiDeg);
		this.skyAnnulus.setOuterRadiiDeg(outerRadiiDeg);
	}
}
