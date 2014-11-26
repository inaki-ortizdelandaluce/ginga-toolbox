package org.ginga.tools.spectrum;

import org.ginga.tools.spectrum.LacSpecInputModel.LacCounterModeEnum;
import org.ginga.tools.spectrum.LacSpecInputModel.LacModeEnum;

public class LacQrdFitsInputModel {

	public enum TimeResolutionEnum { SF1, SF1_2, SF1_4 }
	
	private String psFileName;
	private double minElevation;
	private double maxElevation = 180.0;
	private LacModeEnum lacMode;
	private int bgCorrection = 1;
	private int aspectCorrection = 1;
	private int deadTimeCorrection = 1;
	private int delayTimeCorrection = 1;
	private int timeResolutionSec = -4; // 1/4 SF
	private int channel1 = 3;
	private int channel2 = 3;
	private int channel3 = 3;
	private int channel4 = 3;
	private int channel5 = 3;
	private int channel6 = 3;
	private int channel7 = 3;
	private int channel8 = 3;
	private int mixedMode = 1;
	private String spectralFileName;
	private String timingFileName;
	private String regionFileName;

	
	public String getPsFileName() {
		return psFileName;
	}


	public void setPsFileName(String psFileName) {
		this.psFileName = psFileName;
	}


	public double getMinElevation() {
		return minElevation;
	}


	public void setMinElevation(double minElevation) {
		this.minElevation = minElevation;
	}


	public double getMaxElevation() {
		return maxElevation;
	}


	public void setMaxElevation(double maxElevation) {
		this.maxElevation = maxElevation;
	}


	public LacModeEnum getLacMode() {
		return lacMode;
	}


	public void setLacMode(LacModeEnum lacMode) {
		this.lacMode = lacMode;
	}


	public int getBgCorrection() {
		return bgCorrection;
	}


	public void setBgCorrection(int bgCorrection) {
		this.bgCorrection = bgCorrection;
	}


	public int getAspectCorrection() {
		return aspectCorrection;
	}


	public void setAspectCorrection(int aspectCorrection) {
		this.aspectCorrection = aspectCorrection;
	}


	public int getDeadTimeCorrection() {
		return deadTimeCorrection;
	}


	public void setDeadTimeCorrection(int deadTimeCorrection) {
		this.deadTimeCorrection = deadTimeCorrection;
	}


	public int getDelayTimeCorrection() {
		return delayTimeCorrection;
	}


	public void setDelayTimeCorrection(int delayTimeCorrection) {
		this.delayTimeCorrection = delayTimeCorrection;
	}


	public int getTimeResolutionSec() {
		return timeResolutionSec;
	}


	public void setTimeResolutionSec(int timeResolutionSec) {
		this.timeResolutionSec = timeResolutionSec;
	}
	
	public void setTimeResolutionSec(TimeResolutionEnum timeResolution) {
		if(timeResolution.equals(TimeResolutionEnum.SF1)) {
		    this.timeResolutionSec = -1;
		} else if(timeResolution.equals(TimeResolutionEnum.SF1_2)) {
			this.timeResolutionSec = -2;
	    } else { // SF1_4
	    	this.timeResolutionSec = -4;
	    }
	}
	

	public int getChannel1() {
		return channel1;
	}


	public void setChannel1(int channel1) {
		this.channel1 = channel1;
	}


	public int getChannel2() {
		return channel2;
	}


	public void setChannel2(int channel2) {
		this.channel2 = channel2;
	}


	public int getChannel3() {
		return channel3;
	}


	public void setChannel3(int channel3) {
		this.channel3 = channel3;
	}


	public int getChannel4() {
		return channel4;
	}


	public void setChannel4(int channel4) {
		this.channel4 = channel4;
	}


	public int getChannel5() {
		return channel5;
	}


	public void setChannel5(int channel5) {
		this.channel5 = channel5;
	}


	public int getChannel6() {
		return channel6;
	}


	public void setChannel6(int channel6) {
		this.channel6 = channel6;
	}


	public int getChannel7() {
		return channel7;
	}


	public void setChannel7(int channel7) {
		this.channel7 = channel7;
	}


	public int getChannel8() {
		return channel8;
	}


	public void setChannel8(int channel8) {
		this.channel8 = channel8;
	}


	public int getMixedMode() {
		return mixedMode;
	}


	public void setMixMode(int mixedMode) {
		this.mixedMode = mixedMode;
	}


	public String getSpectralFileName() {
		return spectralFileName;
	}


	public void setSpectralFileName(String spectralFileName) {
		this.spectralFileName = spectralFileName;
	}


	public String getTimingFileName() {
		return timingFileName;
	}


	public void setTimingFileName(String timingFileName) {
		this.timingFileName = timingFileName;
	}


	public String getRegionFileName() {
		return regionFileName;
	}


	public void setRegionFileName(String regionFileName) {
		this.regionFileName = regionFileName;
	}


	public void setLacMode(String mode) {
		if(mode.equals("MPC1")) {
			this.lacMode = LacModeEnum.MPC1;
		} else if(mode.equals("MPC2")) {
			this.lacMode = LacModeEnum.MPC2;
		} else if(mode.equals("MPC3")) {
			this.lacMode = LacModeEnum.MPC3;
		} else {
			throw new IllegalArgumentException("Accepted LAC Modes are MPC1, MPC2 and MPC3");
		}
	}
	
	public void setDelayTimeCorrection(boolean enable) {
		this.delayTimeCorrection = (enable)? 1: 0;
	}

	public void setDeadTimeCorrection(boolean enable) {
		this.deadTimeCorrection = (enable)? 1: 0;
	}
	
	public void setAspectCorrection(boolean enable) {
		this.aspectCorrection = (enable)? 1: 0;
	}
	public void setBgCorrection(boolean enable) {
		this.bgCorrection = (enable)? 1: 0;
	}

	public void setChannel1(LacCounterModeEnum mode) {
		if(mode.equals(LacCounterModeEnum.MIDDLE)) {
			this.channel1 = 1;
		} else if (mode.equals(LacCounterModeEnum.TOP)) {
			this.channel1 = 2;
		} else {
			this.channel1 = 3;
		}
	}
	
	public void setChannel2(LacCounterModeEnum mode) {
		if(mode.equals(LacCounterModeEnum.MIDDLE)) {
			this.channel2 = 1;
		} else if (mode.equals(LacCounterModeEnum.TOP)) {
			this.channel2 = 2;
		} else {
			this.channel2 = 3;
		}
	}
	
	public void setChannel3(LacCounterModeEnum mode) {
		if(mode.equals(LacCounterModeEnum.MIDDLE)) {
			this.channel3 = 1;
		} else if (mode.equals(LacCounterModeEnum.TOP)) {
			this.channel3 = 2;
		} else {
			this.channel3 = 3;
		}
	}

	public void setChannel4(LacCounterModeEnum mode) {
		if(mode.equals(LacCounterModeEnum.MIDDLE)) {
			this.channel4 = 1;
		} else if (mode.equals(LacCounterModeEnum.TOP)) {
			this.channel4 = 2;
		} else {
			this.channel4 = 3;
		}
	}

	public void setChannel5(LacCounterModeEnum mode) {
		if(mode.equals(LacCounterModeEnum.MIDDLE)) {
			this.channel5 = 1;
		} else if (mode.equals(LacCounterModeEnum.TOP)) {
			this.channel5 = 2;
		} else {
			this.channel5 = 3;
		}
	}

	public void setChannel6(LacCounterModeEnum mode) {
		if(mode.equals(LacCounterModeEnum.MIDDLE)) {
			this.channel6 = 1;
		} else if (mode.equals(LacCounterModeEnum.TOP)) {
			this.channel6 = 2;
		} else {
			this.channel6 = 3;
		}
	}

	public void setChannel7(LacCounterModeEnum mode) {
		if(mode.equals(LacCounterModeEnum.MIDDLE)) {
			this.channel7 = 1;
		} else if (mode.equals(LacCounterModeEnum.TOP)) {
			this.channel7 = 2;
		} else {
			this.channel7 = 3;
		}
	}

	public void setChannel8(LacCounterModeEnum mode) {
		if(mode.equals(LacCounterModeEnum.MIDDLE)) {
			this.channel8 = 1;
		} else if (mode.equals(LacCounterModeEnum.TOP)) {
			this.channel8 = 2;
		} else {
			this.channel8 = 3;
		}
	}
}
