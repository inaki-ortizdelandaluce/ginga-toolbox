package org.ginga.tools.spectra;

public class LacSpecInputModel {

	private boolean hasBackground;
	private int bgMethodInt;
	private String bgFileName;
	private int bgSubFileNumber;
	
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
