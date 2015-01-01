package org.ginga.toolbox.environment;

import org.ginga.toolbox.util.Constants.BitRate;

public interface DataReductionEnv {

	public Double getElevationMin();
	
	public Double getElevationMax();
	
	public Double getCutOffRigidityMin();
	
	public Double getCutOffRigidityMax();
	
	public BitRate getBitRate();
	
}
