package org.ginga.toolbox.timinfilfits;

import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.Constants.BitRate;
import org.ginga.toolbox.util.Constants.LacCounterMode;
import org.ginga.toolbox.util.Constants.LacMode;

public class TiminfilfitsInputModel {

	private String psFileName;
	private String monitorFileName;
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
	private boolean hasBackground;
	private int bgMethod;
	private String bgFileName;
	private int bgSubFileNumber;
	private int counter1;
	private int counter2;
	private int counter3;
	private int counter4;
	private int counter5;
	private int counter6;
	private int counter7;
	private int counter8;
	private LacMode lacMode;
	private int mixedMode;
	private String spectralFileName;
	private String regionFileName;
	private String startTime;
	
	public String getPsFileName() {
		return psFileName;
	}
	public void setPsFileName(String psFileName) {
		this.psFileName = psFileName;
	}
	public String getMonitorFileName() {
		return monitorFileName;
	}
	public void setMonitorFileName(String monitorFileName) {
		this.monitorFileName = monitorFileName;
	}
	public double getMinRigidity() {
		return minRigidity;
	}
	public void setMinRigidity(double minRigidity) {
		this.minRigidity = minRigidity;
	}
	public double getMaxRigidity() {
		return maxRigidity;
	}
	public void setMaxRigidity(double maxRigidity) {
		this.maxRigidity = maxRigidity;
	}
	public double getMinTransmission() {
		return minTransmission;
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
	
	public int getBgCorrection() {
		return bgCorrection;
	}
	public void setBgCorrection(int bgCorrection) {
		this.bgCorrection = bgCorrection;
	}
	public void setBgCorrection(boolean enable) {
		this.bgCorrection = (enable)? 1: 0;
	}
	public int getAspectCorrection() {
		return aspectCorrection;
	}
	public void setAspectCorrection(int aspectCorrection) {
		this.aspectCorrection = aspectCorrection;
	}
	public void setAspectCorrection(boolean enable) {
		this.aspectCorrection = (enable)? 1: 0;
	}
	public int getDeadTimeCorrection() {
		return deadTimeCorrection;
	}
	public void setDeadTimeCorrection(int deadTimeCorrection) {
		this.deadTimeCorrection = deadTimeCorrection;
	}
	public void setDeadTimeCorrection(boolean enable) {
		this.deadTimeCorrection = (enable)? 1: 0;
	}
	public int getCounterToEnergy() {
		return channelToEnergy;
	}
	public void setChannelToEnergy(int channelToEnergy) {
		this.channelToEnergy = channelToEnergy;
	}
	public void setChannelToEnergy(boolean enable) {
		this.channelToEnergy = (enable)? 1: 0;
	}
	public int getDataUnit() {
		return dataUnit;
	}
	public void setDataUnit(int dataUnit) {
		this.dataUnit = dataUnit;
	}
	public void setDataUnit(boolean enable) {
		this.dataUnit = (enable)? 1: 0;
	}
	public int getCounter1() {
		return counter1;
	}
	public void setCounter1(int counter1) {
		this.counter1 = counter1;
	}
	public void setCounter1(LacCounterMode mode) {
		if(mode.equals(LacCounterMode.MIDDLE)) {
			this.counter1 = 1;
		} else if (mode.equals(LacCounterMode.TOP)) {
			this.counter1 = 2;
		} else {
			this.counter1 = 3;
		}
	}
	public int getCounter2() {
		return counter2;
	}
	public void setCounter2(int counter2) {
		this.counter2 = counter2;
	}
	public void setCounter2(LacCounterMode mode) {
		if(mode.equals(LacCounterMode.MIDDLE)) {
			this.counter2 = 1;
		} else if (mode.equals(LacCounterMode.TOP)) {
			this.counter2 = 2;
		} else {
			this.counter2 = 3;
		}
	}
	public int getCounter3() {
		return counter3;
	}
	public void setCounter3(int counter3) {
		this.counter3 = counter3;
	}
	public void setCounter3(LacCounterMode mode) {
		if(mode.equals(LacCounterMode.MIDDLE)) {
			this.counter3 = 1;
		} else if (mode.equals(LacCounterMode.TOP)) {
			this.counter3 = 2;
		} else {
			this.counter3 = 3;
		}
	}
	public int getCounter4() {
		return counter4;
	}
	public void setCounter4(int counter4) {
		this.counter4 = counter4;
	}
	public void setCounter4(LacCounterMode mode) {
		if(mode.equals(LacCounterMode.MIDDLE)) {
			this.counter4 = 1;
		} else if (mode.equals(LacCounterMode.TOP)) {
			this.counter4 = 2;
		} else {
			this.counter4 = 3;
		}
	}
	public int getCounter5() {
		return counter5;
	}
	public void setCounter5(int counter5) {
		this.counter5 = counter5;
	}
	public void setCounter5(LacCounterMode mode) {
		if(mode.equals(LacCounterMode.MIDDLE)) {
			this.counter5 = 1;
		} else if (mode.equals(LacCounterMode.TOP)) {
			this.counter5 = 2;
		} else {
			this.counter5 = 3;
		}
	}
	public int getCounter6() {
		return counter6;
	}
	public void setCounter6(int counter6) {
		this.counter6 = counter6;
	}
	public void setCounter6(LacCounterMode mode) {
		if(mode.equals(LacCounterMode.MIDDLE)) {
			this.counter6 = 1;
		} else if (mode.equals(LacCounterMode.TOP)) {
			this.counter6 = 2;
		} else {
			this.counter6 = 3;
		}
	}
	public int getCounter7() {
		return counter7;
	}
	public void setCounter7(int counter7) {
		this.counter7 = counter7;
	}
	public void setCounter7(LacCounterMode mode) {
		if(mode.equals(LacCounterMode.MIDDLE)) {
			this.counter7 = 1;
		} else if (mode.equals(LacCounterMode.TOP)) {
			this.counter7 = 2;
		} else {
			this.counter7 = 3;
		}
	}
	public int getCounter8() {
		return counter8;
	}
	public void setCounter8(int counter8) {
		this.counter8 = counter8;
	}
	public void setCounter8(LacCounterMode mode) {
		if(mode.equals(LacCounterMode.MIDDLE)) {
			this.counter8 = 1;
		} else if (mode.equals(LacCounterMode.TOP)) {
			this.counter8 = 2;
		} else {
			this.counter8 = 3;
		}
	}
	public LacMode getLacMode() {
		return this.lacMode;
	}
	public void setLacMode(LacMode mode) {
		this.lacMode = mode;
	}
	public int getMixedMode() {
		return mixedMode;
	}
	public void setMixedMode(int mixedMode) {
		this.mixedMode = mixedMode;
	}
	public void setMixedMode(boolean enabled) {
		this.mixedMode = (enabled)? 1:0;
	}
	
	public String getSpectralFileName() {
		return spectralFileName;
	}
	public void setSpectralFileName(String spectralFileName) {
		this.spectralFileName = spectralFileName;
	}
	public String getRegionFileName() {
		return regionFileName;
	}
	public void setRegionFileName(String regionFileName) {
		this.regionFileName = regionFileName;
	}
	public boolean getHasBackground() {
		return hasBackground;
	}
	public void setHasBackground(boolean hasBackground) {
		this.hasBackground = hasBackground;
	}
	public int getBgMethod() {
		return bgMethod;
	}
	public void setBgMethod(BgSubtractionMethod method) {
		if(method.equals(BgSubtractionMethod.SIMPLE)) {
			this.bgMethod = 1;
		} else if(method.equals(BgSubtractionMethod.SUD_SORT)) {
			this.bgMethod = 2;
		} else { // HAYASHIDA
			this.bgMethod = 3;
		}
	}
	public String getBgFileName() {
		return bgFileName;
	}
	public void setBgFileName(String bgFileName) {
		this.bgFileName = bgFileName;
	}
	public int getBgSubFileNumber() {
		return bgSubFileNumber;
	}
	public void setBgSubFileNumber(int bgSubFileNumber) {
		this.bgSubFileNumber = bgSubFileNumber;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

}
