package org.ginga.toolbox.timinfilfits;

import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.Constants.BitRate;
import org.ginga.toolbox.util.Constants.LacCounterMode;
import org.ginga.toolbox.util.Constants.LacMode;

public class TiminfilfitsInputModel {

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
    private int bgMethod;
    private String bgFileName;
    private int bgSubFileNumber;
    private String phsel1;
    private String phsel2;
    private String phsel3;
    private String phsel4;
    private String phsel5;
    private String phsel6;
    private String phsel7;
    private String phsel8;
    private String phsel9;
    private String phsel10;
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
    private double timeResolution;
    private String startTime;

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

    public int getCounter1() {
        return this.counter1;
    }

    public void setCounter1(int counter1) {
        this.counter1 = counter1;
    }

    public void setCounter1(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter1 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter1 = 2;
        } else {
            this.counter1 = 3;
        }
    }

    public int getCounter2() {
        return this.counter2;
    }

    public void setCounter2(int counter2) {
        this.counter2 = counter2;
    }

    public void setCounter2(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter2 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter2 = 2;
        } else {
            this.counter2 = 3;
        }
    }

    public int getCounter3() {
        return this.counter3;
    }

    public void setCounter3(int counter3) {
        this.counter3 = counter3;
    }

    public void setCounter3(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter3 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter3 = 2;
        } else {
            this.counter3 = 3;
        }
    }

    public int getCounter4() {
        return this.counter4;
    }

    public void setCounter4(int counter4) {
        this.counter4 = counter4;
    }

    public void setCounter4(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter4 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter4 = 2;
        } else {
            this.counter4 = 3;
        }
    }

    public int getCounter5() {
        return this.counter5;
    }

    public void setCounter5(int counter5) {
        this.counter5 = counter5;
    }

    public void setCounter5(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter5 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter5 = 2;
        } else {
            this.counter5 = 3;
        }
    }

    public int getCounter6() {
        return this.counter6;
    }

    public void setCounter6(int counter6) {
        this.counter6 = counter6;
    }

    public void setCounter6(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter6 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter6 = 2;
        } else {
            this.counter6 = 3;
        }
    }

    public int getCounter7() {
        return this.counter7;
    }

    public void setCounter7(int counter7) {
        this.counter7 = counter7;
    }

    public void setCounter7(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter7 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter7 = 2;
        } else {
            this.counter7 = 3;
        }
    }

    public int getCounter8() {
        return this.counter8;
    }

    public void setCounter8(int counter8) {
        this.counter8 = counter8;
    }

    public void setCounter8(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
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
        return this.mixedMode;
    }

    public void setMixedMode(int mixedMode) {
        this.mixedMode = mixedMode;
    }

    public void setMixedMode(boolean enabled) {
        this.mixedMode = (enabled) ? 1 : 0;
    }

    public String getSpectralFileName() {
        return this.spectralFileName;
    }

    public void setSpectralFileName(String spectralFileName) {
        this.spectralFileName = spectralFileName;
    }

    public String getRegionFileName() {
        return this.regionFileName;
    }

    public void setRegionFileName(String regionFileName) {
        this.regionFileName = regionFileName;
    }

    public int getBgMethod() {
        return this.bgMethod;
    }

    public void setBgMethod(BgSubtractionMethod method) {
        if (method.equals(BgSubtractionMethod.SIMPLE)) {
            this.bgMethod = 1;
        } else if (method.equals(BgSubtractionMethod.SUD_SORT)) {
            this.bgMethod = 2;
        } else { // HAYASHIDA
            this.bgMethod = 3;
        }
    }

    public String getBgFileName() {
        return this.bgFileName;
    }

    public void setBgFileName(String bgFileName) {
        this.bgFileName = bgFileName;
    }

    public int getBgSubFileNumber() {
        return this.bgSubFileNumber;
    }

    public void setBgSubFileNumber(int bgSubFileNumber) {
        this.bgSubFileNumber = bgSubFileNumber;
    }

    /**
     * @return the phsel1
     */
    public String getPhsel1() {
        return this.phsel1;
    }

    /**
     * @param phsel1 the phsel1 to set
     */
    public void setPhsel1(String phsel1) {
        this.phsel1 = phsel1;
    }

    /**
     * @return the phsel2
     */
    public String getPhsel2() {
        return this.phsel2;
    }

    /**
     * @param phsel2 the phsel2 to set
     */
    public void setPhsel2(String phsel2) {
        this.phsel2 = phsel2;
    }

    /**
     * @return the phsel3
     */
    public String getPhsel3() {
        return this.phsel3;
    }

    /**
     * @param phsel3 the phsel3 to set
     */
    public void setPhsel3(String phsel3) {
        this.phsel3 = phsel3;
    }

    /**
     * @return the phsel4
     */
    public String getPhsel4() {
        return this.phsel4;
    }

    /**
     * @param phsel4 the phsel4 to set
     */
    public void setPhsel4(String phsel4) {
        this.phsel4 = phsel4;
    }

    /**
     * @return the phsel5
     */
    public String getPhsel5() {
        return this.phsel5;
    }

    /**
     * @param phsel5 the phsel5 to set
     */
    public void setPhsel5(String phsel5) {
        this.phsel5 = phsel5;
    }

    /**
     * @return the phsel6
     */
    public String getPhsel6() {
        return this.phsel6;
    }

    /**
     * @param phsel6 the phsel6 to set
     */
    public void setPhsel6(String phsel6) {
        this.phsel6 = phsel6;
    }

    /**
     * @return the phsel7
     */
    public String getPhsel7() {
        return this.phsel7;
    }

    /**
     * @param phsel7 the phsel7 to set
     */
    public void setPhsel7(String phsel7) {
        this.phsel7 = phsel7;
    }

    /**
     * @return the phsel8
     */
    public String getPhsel8() {
        return this.phsel8;
    }

    /**
     * @param phsel8 the phsel8 to set
     */
    public void setPhsel8(String phsel8) {
        this.phsel8 = phsel8;
    }

    /**
     * @return the phsel9
     */
    public String getPhsel9() {
        return this.phsel9;
    }

    /**
     * @param phsel9 the phsel9 to set
     */
    public void setPhsel9(String phsel9) {
        this.phsel9 = phsel9;
    }

    /**
     * @return the phsel10
     */
    public String getPhsel10() {
        return this.phsel10;
    }

    /**
     * @param phsel10 the phsel10 to set
     */
    public void setPhsel10(String phsel10) {
        this.phsel10 = phsel10;
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

}
