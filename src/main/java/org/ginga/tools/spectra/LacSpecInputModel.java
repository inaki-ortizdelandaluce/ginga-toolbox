package org.ginga.tools.spectra;

public class LacSpecInputModel {

	public enum BitRateEnum { ANY, HI,  MID, LOW }
	public enum LacModeEnum { MPC1, MPC2, MPC3, PC, INIT }
	public enum BgSubtractionMethodEnum { SIMPLE, SUD_SORT, HAYASHIDA }
	public enum LacCounterModeEnum { MIDDLE, TOP, BOTH }
	
	private String psFileName;
	private String monitorFileName;
	private double minRigidity;
	private double maxRigidity = 20;
	private double minTransmission = 0;
	private BitRateEnum bitRate = BitRateEnum.ANY;
	private boolean pointingMode = true; 
	private double minElevation;
	private double maxElevation = 999.0;
	private int bgCorrection = 1;
	private int aspectCorrection = 1;
	private int deadTimeCorrection = 1;
	private int channelToEnergy = 1;
	private int dataUnit = 1;
	private boolean hasBackground;
	private int bgMethodInt;
	private String bgFileName;
	private int bgSubFileNumber = 1;
	private int channel1 = 3;
	private int channel2 = 3;
	private int channel3 = 3;
	private int channel4 = 3;
	private int channel5 = 3;
	private int channel6 = 3;
	private int channel7 = 3;
	private int channel8 = 3;
	private LacModeEnum lacMode;
	private int mixMode = 1;
	private String spectralFileName;
	private String regionFileName;
	
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
	public BitRateEnum getBitRate() {
		return bitRate;
	}
	public void setBitRate(BitRateEnum bitRate) {
		this.bitRate = bitRate;
	}
	public boolean getPointingMode() {
		return pointingMode;
	}
	public void setPointingMode(boolean pointingMode) {
		this.pointingMode = pointingMode;
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
	public int getChannelToEnergy() {
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
	public int getChannel1() {
		return channel1;
	}
	public void setChannel1(int channel1) {
		this.channel1 = channel1;
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
	public int getChannel2() {
		return channel2;
	}
	public void setChannel2(int channel2) {
		this.channel2 = channel2;
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
	public int getChannel3() {
		return channel3;
	}
	public void setChannel3(int channel3) {
		this.channel3 = channel3;
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
	public int getChannel4() {
		return channel4;
	}
	public void setChannel4(int channel4) {
		this.channel4 = channel4;
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
	public int getChannel5() {
		return channel5;
	}
	public void setChannel5(int channel5) {
		this.channel5 = channel5;
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
	public int getChannel6() {
		return channel6;
	}
	public void setChannel6(int channel6) {
		this.channel6 = channel6;
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
	public int getChannel7() {
		return channel7;
	}
	public void setChannel7(int channel7) {
		this.channel7 = channel7;
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
	public int getChannel8() {
		return channel8;
	}
	public void setChannel8(int channel8) {
		this.channel8 = channel8;
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
	public LacModeEnum getLacMode() {
		return lacMode;
	}
	public void setLacMode(LacModeEnum mode) {
		this.lacMode = mode;
	}
	public void setLacMode(String mode) {
		if(mode.equals("MPC1")) {
			this.lacMode = LacModeEnum.MPC1;
		} else if(mode.equals("MPC2")) {
			this.lacMode = LacModeEnum.MPC2;
		} else if(mode.equals("MPC3")) {
			this.lacMode = LacModeEnum.MPC3;
		} else if (mode.equals("PC")) {
			this.lacMode = LacModeEnum.PC;
		} else {
			this.lacMode = LacModeEnum.INIT;
		}
	}
	public int getMixMode() {
		return mixMode;
	}
	public void setMixMode(int mixMode) {
		this.mixMode = mixMode;
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
	public int getBgMethodInt() {
		return bgMethodInt;
	}
	public void setBgMethodInt(int bgMethodInt) {
		this.bgMethodInt = bgMethodInt;
	}
	public void setBgMethodInt(BgSubtractionMethodEnum method) {
		if(method.equals(BgSubtractionMethodEnum.SIMPLE)) {
			this.bgMethodInt = 1;
		} else if(method.equals(BgSubtractionMethodEnum.SUD_SORT)) {
			this.bgMethodInt = 2;
		} else { // HAYASHIDA
			this.bgMethodInt = 3;
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

}
