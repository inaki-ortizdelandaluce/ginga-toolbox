package org.ginga.toolbox.environment;

import org.ginga.toolbox.util.Constants.BitRate;

public class InteractiveDataReductionEnv implements DataReductionEnv {

	private Double elevationMin;
	private Double elevationMax;
	private Double cutOffRigidityMin;
	private Double cutOffRigidityMax;
	private BitRate bitRate;
	
	private static InteractiveDataReductionEnv instance;
	
	private InteractiveDataReductionEnv() {
	}
	
	protected static InteractiveDataReductionEnv getInstance() {
		if(instance == null) {
			instance = new InteractiveDataReductionEnv();
		}
		return instance;
	}
	
	@Override
	public Double getElevationMin() {
		return elevationMin;
	}
	/**
	 * @param elevationMin the elevationMin to set
	 */
	public void setElevationMin(Double elevationMin) {
		this.elevationMin = elevationMin;
	}
	/**
	 * @return the elevationMax
	 */
	@Override
	public Double getElevationMax() {
		return elevationMax;
	}
	/**
	 * @param elevationMax the elevationMax to set
	 */
	public void setElevationMax(Double elevationMax) {
		this.elevationMax = elevationMax;
	}
	/**
	 * @return the cutOffRigidityMin
	 */
	@Override
	public Double getCutOffRigidityMin() {
		return cutOffRigidityMin;
	}
	/**
	 * @param cutOffRigidityMin the cutOffRigidityMin to set
	 */
	public void setCutOffRigidityMin(Double cutOffRigidityMin) {
		this.cutOffRigidityMin = cutOffRigidityMin;
	}
	/**
	 * @return the cutOffRigidityMax
	 */
	@Override
	public Double getCutOffRigidityMax() {
		return cutOffRigidityMax;
	}
	/**
	 * @param cutOffRigidityMax the cutOffRigidityMax to set
	 */
	public void setCutOffRigidityMax(Double cutOffRigidityMax) {
		this.cutOffRigidityMax = cutOffRigidityMax;
	}
	/**
	 * @return the bitRate
	 */
	@Override
	public BitRate getBitRate() {
		return bitRate;
	}
	/**
	 * @param bitRate the bitRate to set
	 */
	public void setBitRate(BitRate bitRate) {
		this.bitRate = bitRate;
	}
	
}
