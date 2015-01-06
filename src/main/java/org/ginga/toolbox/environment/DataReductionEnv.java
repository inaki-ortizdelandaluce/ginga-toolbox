package org.ginga.toolbox.environment;

import org.ginga.toolbox.util.Constants.BitRate;

public interface DataReductionEnv {

    public Double getElevationMin();

    public Double getElevationMax();

    public Double getCutOffRigidityMin();

    public Double getCutOffRigidityMax();

    public BitRate getBitRate();

    public Double getTransmissionMin();

    public Integer getAttitudeMode();

    public Boolean getDeadTimeCorrection();

    public Boolean getDelayTimeCorrection();

    public Boolean getChannelToEnergyConversion();

    public Integer getLacCounter1();

    public Integer getLacCounter2();

    public Integer getLacCounter3();

    public Integer getLacCounter4();

    public Integer getLacCounter5();

    public Integer getLacCounter6();

    public Integer getLacCounter7();

    public Integer getLacCounter8();

    public Boolean isLacMixedMode();

    public Integer getBgSubFileNumber();

    public Double getSkyAnnulusInnerRadii();

    public Double getSkyAnnulusOuterRadii();

}
