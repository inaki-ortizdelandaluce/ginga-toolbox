package org.ginga.toolbox.lacqrdfits;

import org.ginga.toolbox.util.Constants.LacCounterMode;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.Constants.TimingBinWidth;

public class LacqrdfitsInputModel {

    private String psFileName;
    private double minElevation;
    private double maxElevation;
    private LacMode lacMode;
    private int bgCorrection;
    private int aspectCorrection;
    private int deadTimeCorrection;
    private int delayTimeCorrection;
    private int timingBinWidth;
    private int counter1;
    private int counter2;
    private int counter3;
    private int counter4;
    private int counter5;
    private int counter6;
    private int counter7;
    private int counter8;
    private int mixedMode;
    private String spectralFileName;
    private String timingFileName;
    private String regionFileName;
    private String startTime;
    private boolean timingHayashidaBg;

    public String getPsFileName() {
        return this.psFileName;
    }

    public void setPsFileName(String psFileName) {
        this.psFileName = psFileName;
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

    public LacMode getLacMode() {
        return this.lacMode;
    }

    public void setLacMode(LacMode lacMode) {
        this.lacMode = lacMode;
    }

    public int getBgCorrection() {
        return this.bgCorrection;
    }

    public void setBgCorrection(int bgCorrection) {
        this.bgCorrection = bgCorrection;
    }

    public int getAspectCorrection() {
        return this.aspectCorrection;
    }

    public void setAspectCorrection(int aspectCorrection) {
        this.aspectCorrection = aspectCorrection;
    }

    public int getDeadTimeCorrection() {
        return this.deadTimeCorrection;
    }

    public void setDeadTimeCorrection(int deadTimeCorrection) {
        this.deadTimeCorrection = deadTimeCorrection;
    }

    public int getDelayTimeCorrection() {
        return this.delayTimeCorrection;
    }

    public void setDelayTimeCorrection(int delayTimeCorrection) {
        this.delayTimeCorrection = delayTimeCorrection;
    }

    public int getTimingBinWidth() {
        return this.timingBinWidth;
    }

    public void setTimingBinWidth(int binWidth) {
        this.timingBinWidth = binWidth;
    }

    public void setTimingeSamplingBinSec(TimingBinWidth timingBinWidth) {
        switch (timingBinWidth) {
        case ONE_SF:
            this.timingBinWidth = -1;
            break;
        case HALF_SF:
            this.timingBinWidth = -2;
            break;
        case QUARTER_SF:
        default:
            this.timingBinWidth = -4;
            break;
        }
    }

    public int getCounter1() {
        return this.counter1;
    }

    public void setCounter1(int counter1) {
        this.counter1 = counter1;
    }

    public int getCounter2() {
        return this.counter2;
    }

    public void setCounter2(int counter2) {
        this.counter2 = counter2;
    }

    public int getCounter3() {
        return this.counter3;
    }

    public void setCounter3(int counter3) {
        this.counter3 = counter3;
    }

    public int getCounter4() {
        return this.counter4;
    }

    public void setCounter4(int counter4) {
        this.counter4 = counter4;
    }

    public int getCounter5() {
        return this.counter5;
    }

    public void setCounter5(int counter5) {
        this.counter5 = counter5;
    }

    public int getCounter6() {
        return this.counter6;
    }

    public void setCounter6(int counter6) {
        this.counter6 = counter6;
    }

    public int getCounter7() {
        return this.counter7;
    }

    public void setCounter7(int counter7) {
        this.counter7 = counter7;
    }

    public int getCounter8() {
        return this.counter8;
    }

    public void setCounter8(int counter8) {
        this.counter8 = counter8;
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

    public String getTimingFileName() {
        return this.timingFileName;
    }

    public void setTimingFileName(String timingFileName) {
        this.timingFileName = timingFileName;
    }

    public String getRegionFileName() {
        return this.regionFileName;
    }

    public void setRegionFileName(String regionFileName) {
        this.regionFileName = regionFileName;
    }

    public void setLacMode(String mode) {
        if (mode.equals("MPC1")) {
            this.lacMode = LacMode.MPC1;
        } else if (mode.equals("MPC2")) {
            this.lacMode = LacMode.MPC2;
        } else if (mode.equals("MPC3")) {
            this.lacMode = LacMode.MPC3;
        } else {
            throw new IllegalArgumentException("Accepted LAC Modes are MPC1, MPC2 and MPC3");
        }
    }

    public void setDelayTimeCorrection(boolean enable) {
        this.delayTimeCorrection = (enable) ? 1 : 0;
    }

    public void setDeadTimeCorrection(boolean enable) {
        this.deadTimeCorrection = (enable) ? 1 : 0;
    }

    public void setAspectCorrection(boolean enable) {
        this.aspectCorrection = (enable) ? 1 : 0;
    }

    public void setBgCorrection(boolean enable) {
        this.bgCorrection = (enable) ? 1 : 0;
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

    public void setCounter2(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter2 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter2 = 2;
        } else {
            this.counter2 = 3;
        }
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

    public void setCounter4(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter4 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter4 = 2;
        } else {
            this.counter4 = 3;
        }
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

    public void setCounter6(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter6 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter6 = 2;
        } else {
            this.counter6 = 3;
        }
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

    public void setCounter8(LacCounterMode mode) {
        if (mode.equals(LacCounterMode.MIDDLE)) {
            this.counter8 = 1;
        } else if (mode.equals(LacCounterMode.TOP)) {
            this.counter8 = 2;
        } else {
            this.counter8 = 3;
        }
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the timingHayashidaBg
     */
    public boolean isTimingHayashidaBg() {
        return timingHayashidaBg;
    }

    /**
     * @param timingHayashidaBg the timingHayashidaBg to set
     */
    public void setTimingHayashidaBg(boolean timingHayashidaBg) {
        this.timingHayashidaBg = timingHayashidaBg;
    }
}
