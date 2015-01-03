package org.ginga.toolbox.environment;

import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.ginga.toolbox.util.Constants;
import org.ginga.toolbox.util.Constants.BitRate;

public class InteractiveDataReductionEnv implements DataReductionEnv {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(InteractiveDataReductionEnv.class);

	private Scanner scanner;
	
	private Double elevationMin;
	private Double elevationMax;
	private Double cutOffRigidityMin;
	private Double cutOffRigidityMax;
	private BitRate bitRate;
	private Integer transmissionMin;
	private Boolean isAcePointingMode;
	private Boolean isAspectCorrection;
	private Boolean isDeadTimeCorrection;
	private Boolean isChannelToEnergyCorrection;
	private Boolean isDataUnitCorrection;
	private Integer bgSubFileNumber;
	private Integer lacCounter1;
	private Integer lacCounter2;
	private Integer lacCounter3;
	private Integer lacCounter4;
	private Integer lacCounter5;
	private Integer lacCounter6;
	private Integer lacCounter7;
	private Integer lacCounter8;
	private Boolean isLacMixedMode;
	
	protected InteractiveDataReductionEnv() {
		this.scanner = new Scanner(System.in);
	}
	
	private String readEnumFromInput(String message, String[] values) {
		String s = null;
		boolean catcher = false;
		do {
            try {
                System.out.print(message);
                s = scanner.next();
                if(!Arrays.asList(values).contains(s)) {
                	throw new IllegalArgumentException();
                }
            	catcher = true;
            } catch (Exception e) {
            	System.out.println("Value is not valid, please try again");
            } finally {
            	scanner.nextLine();
            }
        }
        while (!catcher);
		return s;
	}
	
	private Double readDoubleFromInput(String message) {
		Double d = null;
		boolean catcher = false;
        do {
            try {
                System.out.print(message);
                d = scanner.nextDouble();
                catcher = true;
            } catch (Exception e) {
            	System.out.println("Value is not a number, please try again");
            } finally {
            	scanner.nextLine();
            }
        }
        while (!catcher);
		return d;
	}
	
	@Override
	public Double getElevationMin() {
		if(this.elevationMin == null) {
			this.elevationMin = readDoubleFromInput("Enter a minimum elevation angle in degrees:");
		}
		return elevationMin;
	}
	/**
	 * @param elevationMin the elevationMin to set
	 */
	public void setElevationMin(Double elevationMin) {
		this.elevationMin = elevationMin;
	}
	/**
	 * @return the elevationMax
	 */
	@Override
	public Double getElevationMax() {
		if(this.elevationMax == null) {
			this.elevationMax = readDoubleFromInput("Enter a maximum elevation angle in degrees:");
		}
		return elevationMax;
	}
	/**
	 * @param elevationMax the elevationMax to set
	 */
	public void setElevationMax(Double elevationMax) {
		this.elevationMax = elevationMax;
	}
	/**
	 * @return the cutOffRigidityMin
	 */
	@Override
	public Double getCutOffRigidityMin() {
		if(this.cutOffRigidityMin == null) {
			this.cutOffRigidityMin = readDoubleFromInput("Enter a minimum cutt-off rigidity value in GeV/c:");
		}
		return cutOffRigidityMin;
	}
	/**
	 * @param cutOffRigidityMin the cutOffRigidityMin to set
	 */
	public void setCutOffRigidityMin(Double cutOffRigidityMin) {
		this.cutOffRigidityMin = cutOffRigidityMin;
	}
	/**
	 * @return the cutOffRigidityMax
	 */
	@Override
	public Double getCutOffRigidityMax() {
		if(this.cutOffRigidityMax == null) {
			this.cutOffRigidityMax = readDoubleFromInput("Enter a maximum cutt-off rigidity value in GeV/c:");
		}
		return cutOffRigidityMax;
	}
	/**
	 * @param cutOffRigidityMax the cutOffRigidityMax to set
	 */
	public void setCutOffRigidityMax(Double cutOffRigidityMax) {
		this.cutOffRigidityMax = cutOffRigidityMax;
	}
	/**
	 * @return the bitRate
	 */
	@Override
	public BitRate getBitRate() {
		if(this.bitRate == null) {
			this.bitRate = BitRate.valueOf(
					readEnumFromInput("Enter a bit rate ('ANY', 'H', 'M' and 'L'):", Constants.getBitRates()));
		}
		return bitRate;
	}
	
	/**
	 * @param bitRate the bitRate to set
	 */
	public void setBitRate(BitRate bitRate) {
		this.bitRate = bitRate;
	}

	/**
	 * @return the transmissionMin
	 */
	@Override
	public Integer getTransmissionMin() {
		return transmissionMin;
	}

	/**
	 * @param transmissionMin the transmissionMin to set
	 */
	public void setTransmissionMin(Integer transmissionMin) {
		this.transmissionMin = transmissionMin;
	}

	/**
	 * @return the isAcePointingMode
	 */
	@Override
	public Boolean isAcePointingMode() {
		return isAcePointingMode;
	}

	/**
	 * @param isAcePointingMode the isAcePointingMode to set
	 */
	public void setAcePointingMode(Boolean isAcePointingMode) {
		this.isAcePointingMode = isAcePointingMode;
	}

	/**
	 * @return the isAspectCorrection
	 */
	@Override
	public Boolean isAspectCorrection() {
		return isAspectCorrection;
	}

	/**
	 * @param isAspectCorrection the isAspectCorrection to set
	 */
	public void setAspectCorrection(Boolean isAspectCorrection) {
		this.isAspectCorrection = isAspectCorrection;
	}

	/**
	 * @return the isDeadTimeCorrection
	 */
	@Override
	public Boolean isDeadTimeCorrection() {
		return isDeadTimeCorrection;
	}

	/**
	 * @param isDeadTimeCorrection the isDeadTimeCorrection to set
	 */
	public void setDeadTimeCorrection(Boolean isDeadTimeCorrection) {
		this.isDeadTimeCorrection = isDeadTimeCorrection;
	}

	/**
	 * @return the isChannelToEnergyCorrection
	 */
	@Override
	public Boolean isChannelToEnergyCorrection() {
		return isChannelToEnergyCorrection;
	}

	/**
	 * @param isChannelToEnergyCorrection the isChannelToEnergyCorrection to set
	 */
	public void setChannelToEnergyCorrection(Boolean isChannelToEnergyCorrection) {
		this.isChannelToEnergyCorrection = isChannelToEnergyCorrection;
	}

	/**
	 * @return the isDataUnitCorrection
	 */
	@Override
	public Boolean isDataUnitCorrection() {
		return isDataUnitCorrection;
	}

	/**
	 * @param isDataUnitCorrection the isDataUnitCorrection to set
	 */
	public void setDataUnitCorrection(Boolean isDataUnitCorrection) {
		this.isDataUnitCorrection = isDataUnitCorrection;
	}

	/**
	 * @return the bgSubFileNumber
	 */
	@Override
	public Integer getBgSubFileNumber() {
		return bgSubFileNumber;
	}

	/**
	 * @param bgSubFileNumber the bgSubFileNumber to set
	 */
	public void setBgSubFileNumber(Integer bgSubFileNumber) {
		this.bgSubFileNumber = bgSubFileNumber;
	}

	/**
	 * @return the lacCounter1
	 */
	@Override
	public Integer getLacCounter1() {
		return lacCounter1;
	}

	/**
	 * @param lacCounter1 the lacCounter1 to set
	 */
	public void setLacCounter1(Integer lacCounter1) {
		this.lacCounter1 = lacCounter1;
	}

	/**
	 * @return the lacCounter2
	 */
	@Override
	public Integer getLacCounter2() {
		return lacCounter2;
	}

	/**
	 * @param lacCounter2 the lacCounter2 to set
	 */
	public void setLacCounter2(Integer lacCounter2) {
		this.lacCounter2 = lacCounter2;
	}

	/**
	 * @return the lacCounter3
	 */
	@Override
	public Integer getLacCounter3() {
		return lacCounter3;
	}

	/**
	 * @param lacCounter3 the lacCounter3 to set
	 */
	public void setLacCounter3(Integer lacCounter3) {
		this.lacCounter3 = lacCounter3;
	}

	/**
	 * @return the lacCounter4
	 */
	@Override
	public Integer getLacCounter4() {
		return lacCounter4;
	}

	/**
	 * @param lacCounter4 the lacCounter4 to set
	 */
	public void setLacCounter4(Integer lacCounter4) {
		this.lacCounter4 = lacCounter4;
	}

	/**
	 * @return the lacCounter5
	 */
	@Override
	public Integer getLacCounter5() {
		return lacCounter5;
	}

	/**
	 * @param lacCounter5 the lacCounter5 to set
	 */
	public void setLacCounter5(Integer lacCounter5) {
		this.lacCounter5 = lacCounter5;
	}

	/**
	 * @return the lacCounter6
	 */
	@Override
	public Integer getLacCounter6() {
		return lacCounter6;
	}

	/**
	 * @param lacCounter6 the lacCounter6 to set
	 */
	public void setLacCounter6(Integer lacCounter6) {
		this.lacCounter6 = lacCounter6;
	}

	/**
	 * @return the lacCounter7
	 */
	@Override
	public Integer getLacCounter7() {
		return lacCounter7;
	}

	/**
	 * @param lacCounter7 the lacCounter7 to set
	 */
	public void setLacCounter7(Integer lacCounter7) {
		this.lacCounter7 = lacCounter7;
	}

	/**
	 * @return the lacCounter8
	 */
	@Override
	public Integer getLacCounter8() {
		return lacCounter8;
	}

	/**
	 * @param lacCounter8 the lacCounter8 to set
	 */
	public void setLacCounter8(Integer lacCounter8) {
		this.lacCounter8 = lacCounter8;
	}

	/**
	 * @return the isLacMixedMode
	 */
	@Override
	public Boolean isLacMixedMode() {
		return isLacMixedMode;
	}

	/**
	 * @param isLacMixedMode the isLacMixedMode to set
	 */
	public void setLacMixedMode(Boolean isLacMixedMode) {
		this.isLacMixedMode = isLacMixedMode;
	}
	
}
