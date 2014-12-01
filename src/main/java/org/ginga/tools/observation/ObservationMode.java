package org.ginga.tools.observation;

public class ObservationMode {

	private long obsId;
	private String mode;
	private String startTime;
	private String endTime;
	
	/**
	 * @return the obsId
	 */
	public long getObsId() {
		return obsId;
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
		return mode;
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
		return startTime;
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
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
