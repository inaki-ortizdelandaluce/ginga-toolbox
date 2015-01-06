package org.ginga.toolbox.environment;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.ginga.toolbox.util.Constants.BitRate;

public class SystematicInputParameters implements InputParameters {

    private static final Logger log = Logger.getLogger(SystematicInputParameters.class);

    private static SystematicInputParameters instance;
    private Properties properties;

    private SystematicInputParameters(Properties properties) {
        this.properties = properties;
    }

    protected static SystematicInputParameters getInstance(Properties properties) {
        if (instance == null) {
            instance = new SystematicInputParameters(properties);
        }
        return instance;
    }

    @Override
    public Double getElevationMin() {
        try {
            return Double.valueOf(this.properties.getProperty("systematic.ELV.min"));
        } catch (NullPointerException e) {
            log.warn("Cannot access systematic.ELV.min, using default value", e);
            return Double.valueOf("5.0");
        }
    }

    @Override
    public Double getElevationMax() {
        try {
            return Double.valueOf(this.properties.getProperty("systematic.ELV.max"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.ELV.max, using default value", e);
            return Double.valueOf("180.0");
        }
    }

    @Override
    public Double getCutOffRigidityMin() {
        try {
            return Double.valueOf(this.properties.getProperty("systematic.RIG.min", "10.0"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.RIG.min, using default value", e);
            return Double.valueOf("10.0");
        }
    }

    @Override
    public Double getCutOffRigidityMax() {
        try {
            return Double.valueOf(this.properties.getProperty("systematic.RIG.max"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.RIG.max, using default value", e);
            return Double.valueOf("20.0");
        }
    }

    @Override
    public BitRate getBitRate() {
        try {
            return BitRate.valueOf(this.properties.getProperty("systematic.BR", "ANY"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.BR, using default value", e);
            return BitRate.ANY;
        }
    }

    @Override
    public Double getTransmissionMin() {
        try {
            return Double.valueOf(this.properties.getProperty("systematic.TRN.min"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.TRN.min, using default value", e);
            return Double.valueOf("0.0");
        }
    }

    @Override
    public Integer getAttitudeMode() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.ACE"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.ACE, using default value", e);
            return Integer.valueOf("1");
        }
    }

    @Override
    public Boolean getDeadTimeCorrection() {
        try {
            return Boolean
                    .valueOf(this.properties.getProperty("systematic.COR.deadTimeCorrection"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.COR.deadTimeCorrection, using default value", e);
            return Boolean.valueOf("true");
        }
    }

    @Override
    public Boolean getChannelToEnergyConversion() {
        try {
            return Boolean.valueOf(this.properties.getProperty("systematic.COR.channelToEnergy"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.COR.channelToEnergy, using default value", e);
            return Boolean.valueOf("true");
        }
    }

    @Override
    public Integer getLacCounter1() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.LAC.counter1"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.counter1, using default value", e);
            return Integer.valueOf("3");
        }
    }

    @Override
    public Integer getLacCounter2() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.LAC.counter2"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.counter2, using default value", e);
            return Integer.valueOf("3");
        }
    }

    @Override
    public Integer getLacCounter3() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.LAC.counter3"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.counter3, using default value", e);
            return Integer.valueOf("3");
        }
    }

    @Override
    public Integer getLacCounter4() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.LAC.counter4"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.counter4, using default value", e);
            return Integer.valueOf("3");
        }
    }

    @Override
    public Integer getLacCounter5() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.LAC.counter5"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.counter5, using default value", e);
            return Integer.valueOf("3");
        }
    }

    @Override
    public Integer getLacCounter6() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.LAC.counter6"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.counter6, using default value", e);
            return Integer.valueOf("3");
        }
    }

    @Override
    public Integer getLacCounter7() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.LAC.counter7"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.counter7, using default value", e);
            return Integer.valueOf("3");
        }
    }

    @Override
    public Integer getLacCounter8() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.LAC.counter8"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.counter8, using default value", e);
            return Integer.valueOf("3");
        }
    }

    @Override
    public Boolean isLacMixedMode() {
        try {
            return Boolean.valueOf(this.properties.getProperty("systematic.LAC.mixedMode"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.mixedMode, using default value", e);
            return Boolean.valueOf("true");
        }
    }

    @Override
    public Integer getBgSubFileNumber() {
        try {
            return Integer.valueOf(this.properties.getProperty("systematic.BGD.bgSubFileNumber"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.LAC.counter8, using default value", e);
            return Integer.valueOf("1");
        }
    }

    @Override
    public Double getSkyAnnulusInnerRadii() {
        try {
            return Double.valueOf(this.properties
                    .getProperty("systematic.sky.annulus.innerRadiiDeg"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.sky.annulus.innerRadiiDeg, using default value", e);
            return Double.valueOf("2.5");
        }
    }

    @Override
    public Double getSkyAnnulusOuterRadii() {
        try {
            return Double.valueOf(this.properties
                    .getProperty("systematic.sky.annulus.outerRadiiDeg"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.sky.annulus.outerRadiiDeg, using default value", e);
            return Double.valueOf("3.5");
        }
    }

    @Override
    public Boolean getDelayTimeCorrection() {
        try {
            return Boolean.valueOf(this.properties
                    .getProperty("systematic.COR.delayTimeCorrection"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.COR.delayTimeCorrection, using default value", e);
            return Boolean.valueOf("true");
        }
    }

    @Override
    public String getPhselLine1() {
        try {
            return this.properties.getProperty("systematic.PHEL.line1");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line1, using default empty value", e);
            return null;
        }
    }

    @Override
    public String getPhselLine2() {
        try {
            return this.properties.getProperty("systematic.PHEL.line2");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line2, using default empty value", e);
            return null;
        }
    }

    @Override
    public String getPhselLine3() {
        try {
            return this.properties.getProperty("systematic.PHEL.line3");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line3, using default empty value", e);
            return null;
        }
    }

    @Override
    public String getPhselLine4() {
        try {
            return this.properties.getProperty("systematic.PHEL.line4");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line4, using default empty value", e);
            return null;
        }
    }

    @Override
    public String getPhselLine5() {
        try {
            return this.properties.getProperty("systematic.PHEL.line5");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line5, using default empty value", e);
            return null;
        }
    }

    @Override
    public String getPhselLine6() {
        try {
            return this.properties.getProperty("systematic.PHEL.line6");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line6, using default empty value", e);
            return null;
        }
    }

    @Override
    public String getPhselLine7() {
        try {
            return this.properties.getProperty("systematic.PHEL.line7");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line7, using default empty value", e);
            return null;
        }
    }

    @Override
    public String getPhselLine8() {
        try {
            return this.properties.getProperty("systematic.PHEL.line8");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line8, using default empty value", e);
            return null;
        }
    }

    @Override
    public String getPhselLine9() {
        try {
            return this.properties.getProperty("systematic.PHEL.line9");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line9, using default empty value", e);
            return null;
        }
    }

    @Override
    public String getPhselLine10() {
        try {
            return this.properties.getProperty("systematic.PHEL.line10");
        } catch (Exception e) {
            log.warn("Cannot access systematic.PHEL.line10, using default empty value", e);
            return null;
        }
    }
}
