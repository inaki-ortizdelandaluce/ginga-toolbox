package org.ginga.toolbox.environment;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.ginga.toolbox.util.Constants.BitRate;

public class SystematicDataReductionEnv  implements DataReductionEnv {

	private static final Logger log = Logger.getLogger(SystematicDataReductionEnv.class);

	private static SystematicDataReductionEnv instance;
	private Properties properties;
	
	private SystematicDataReductionEnv(Properties properties) {
		this.properties = properties;
	}
	
	protected static SystematicDataReductionEnv getInstance(Properties properties) {
		if(instance == null) {
			instance = new SystematicDataReductionEnv(properties);
		}
		return instance;
	}
	
	@Override
    public Double getElevationMin() {
        try {
            return Double.valueOf(this.properties.getProperty("systematic.EELV.min"));
        } catch (NullPointerException e) {
            log.warn("Cannot access systematic.EELV.min, using default value", e);
            return Double.valueOf("5.0");
        }
    }

	@Override
    public Double getElevationMax() {
        try {
            return Double.valueOf(this.properties.getProperty("systematic.EELV.max"));
        } catch (Exception e) {
            log.warn("Cannot access systematic.EELV.max, using default null value", e);
            return null;
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
            log.warn("Cannot access systematic.RIG.max, using default null value", e);
            return null;
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
}
