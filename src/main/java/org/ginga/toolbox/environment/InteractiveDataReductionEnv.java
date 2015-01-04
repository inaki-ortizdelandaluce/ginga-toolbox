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
	private Double transmissionMin;
	private Integer attitudeMode;
	private Boolean aspectCorrection;
	private Boolean deadTimeCorrection;
	private Boolean channelToEnergyConversion;
	private Integer dataUnit;
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
	private Double skyAnnulusInnerRadii;
	private Double skyAnnulusOuterRadii;
	
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
	
	private Integer readIntegerFromInput(String message) {
		Integer i = null;
		boolean catcher = false;
        do {
            try {
                System.out.print(message);
                i = scanner.nextInt();
                catcher = true;
            } catch (Exception e) {
            	System.out.println("Value is not a number, please try again");
            } finally {
            	scanner.nextLine();
            }
        }
        while (!catcher);
		return i;
	}
	
	private Boolean readBooleanFromInput(String message) {
		Boolean b = null;
		boolean catcher = false;
        do {
            try {
                System.out.print(message);
                b = scanner.nextBoolean();
                catcher = true;
            } catch (Exception e) {
            	System.out.println("Value is not a number, please try again");
            } finally {
            	scanner.nextLine();
            }
        }
        while (!catcher);
		return b;
	}

	@Override
	public Double getElevationMin() {
		if(this.elevationMin == null) {
			this.elevationMin = readDoubleFromInput("Enter a minimum Pointing Elevation Angle in degrees:");
		}
		return this.elevationMin;
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
			this.elevationMax = readDoubleFromInput("Enter a maximum Pointing Elevation Angle in degrees:");
		}
		return this.elevationMax;
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
			this.cutOffRigidityMin = readDoubleFromInput("Enter a minimum Cutt-off Rigidity value in GeV/c:");
		}
		return this.cutOffRigidityMin;
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
			this.cutOffRigidityMax = readDoubleFromInput("Enter a maximum Cutt-off Rigidity value in GeV/c:");
		}
		return this.cutOffRigidityMax;
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
					readEnumFromInput("Enter a Bit Rate ('ANY', 'H', 'M' and 'L'):", Constants.getBitRates()));
		}
		return this.bitRate;
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
	public Double getTransmissionMin() {
		if(this.transmissionMin == null) {
			this.transmissionMin = readDoubleFromInput("Enter a minimum Transmission value for the target source:");
		}
		return this.transmissionMin;
	}

	/**
	 * @param transmissionMin the transmissionMin to set
	 */
	public void setTransmissionMin(Double transmissionMin) {
		this.transmissionMin = transmissionMin;
	}

	/**
	 * @return the attitudeMode
	 */
	@Override
	public Integer getAttitudeMode() {
		if(this.attitudeMode == null) {
			this.attitudeMode = readIntegerFromInput("Enter an Attitude Mode (1=Pointing Mode, 0=Any Mode):");
		}
		return this.attitudeMode;
	}

	/**
	 * @param attitudeMode the attitudeMode to set
	 */
	public void setAtitudeMode(Integer attitudeMode) {
		this.attitudeMode = attitudeMode;
	}

	/**
	 * @return the aspectCorrection
	 */
	@Override
	public Boolean getAspectCorrection() {
		if(this.aspectCorrection == null) {
			this.aspectCorrection = readBooleanFromInput("Apply Aspect Correction (TRUE, FALSE):");
		}
		return this.aspectCorrection;
	}

	/**
	 * @param aspectCorrection the aspectCorrection to set
	 */
	public void setAspectCorrection(Boolean aspectCorrection) {
		this.aspectCorrection = aspectCorrection;
	}

	/**
	 * @return the deadTimeCorrection
	 */
	@Override
	public Boolean getDeadTimeCorrection() {
		if(this.deadTimeCorrection == null) {
			this.deadTimeCorrection = readBooleanFromInput("Apply Deadtime Correction (true, false):");
		}
		return this.deadTimeCorrection;
	}

	/**
	 * @param deadTimeCorrection the deadTimeCorrection to set
	 */
	public void setDeadTimeCorrection(Boolean deadTimeCorrection) {
		this.deadTimeCorrection = deadTimeCorrection;
	}

	/**
	 * @return the channelToEnergyConversion
	 */
	@Override
	public Boolean getChannelToEnergyConversion() {
		if(this.channelToEnergyConversion == null) {
			this.channelToEnergyConversion = readBooleanFromInput("Apply Channel to Energy conversion (true, false):");
		}
		return this.channelToEnergyConversion;	
	}

	/**
	 * @param channelToEnergyConversion the channelToEnergyConversion to set
	 */
	public void setChannelToEnergyConversion(Boolean channelToEnergyConversion) {
		this.channelToEnergyConversion = channelToEnergyConversion;
	}

	/**
	 * @return the dataUnitConversion
	 */
	@Override
	public Integer getDataUnit() {
		if(this.dataUnit == null) {
			this.dataUnit = readIntegerFromInput("Enter Data Units (0=counts, 1=counts/sec, 2=counts/sec/cm2):");
		}
		return this.dataUnit;	
	}

	/**
	 * @param dataUnitConversion the dataUnitConversion to set
	 */
	public void setDataUnit(Integer dataUnit) {
		this.dataUnit = dataUnit;
	}

	/**
	 * @return the bgSubFileNumber
	 */
	@Override
	public Integer getBgSubFileNumber() {
		if(this.bgSubFileNumber == null) {
			this.bgSubFileNumber = readIntegerFromInput("Enter Background Sub-file Number:");
		}
		return this.bgSubFileNumber;	
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
		if(this.lacCounter1 == null) {
			this.lacCounter1 = readIntegerFromInput("Enter LAC Counter 1 Mode  (1=middle-layer, 2=top-layer, 3=both):");
		}
		return this.lacCounter1;	
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
		if(this.lacCounter2 == null) {
			this.lacCounter2 = readIntegerFromInput("Enter LAC Counter 2 Mode  (1=middle-layer, 2=top-layer, 3=both):");
		}
		return this.lacCounter2;	
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
		if(this.lacCounter3 == null) {
			this.lacCounter3 = readIntegerFromInput("Enter LAC Counter 3 Mode  (1=middle-layer, 2=top-layer, 3=both):");
		}
		return this.lacCounter3;	
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
		if(this.lacCounter4 == null) {
			this.lacCounter4 = readIntegerFromInput("Enter LAC Counter 4 Mode  (1=middle-layer, 2=top-layer, 3=both):");
		}
		return this.lacCounter4;	
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
		if(this.lacCounter5 == null) {
			this.lacCounter5 = readIntegerFromInput("Enter LAC Counter 5 Mode  (1=middle-layer, 2=top-layer, 3=both):");
		}
		return this.lacCounter5;	
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
		if(this.lacCounter6 == null) {
			this.lacCounter6 = readIntegerFromInput("Enter LAC Counter 6 Mode  (1=middle-layer, 2=top-layer, 3=both):");
		}
		return this.lacCounter6;	
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
		if(this.lacCounter7 == null) {
			this.lacCounter7 = readIntegerFromInput("Enter LAC Counter 7 Mode  (1=middle-layer, 2=top-layer, 3=both):");
		}
		return this.lacCounter7;	
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
		if(this.lacCounter8 == null) {
			this.lacCounter8 = readIntegerFromInput("Enter LAC Counter 8 Mode  (1=middle-layer, 2=top-layer, 3=both):");
		}
		return this.lacCounter8;	
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
		if(this.isLacMixedMode == null) {
			this.isLacMixedMode = readBooleanFromInput("Combine MPC-1 and MPC-2 mode data (true, false):");
		}
		return this.isLacMixedMode;	
	}

	/**
	 * @return the skyAnnulusInnerRadii
	 */
	@Override
	public Double getSkyAnnulusInnerRadii() {
		if(this.skyAnnulusInnerRadii == null) {
			this.skyAnnulusInnerRadii = readDoubleFromInput("Enter Sky annulus inner radii in degrees:");
		}
		return this.skyAnnulusInnerRadii;	
	}

	/**
	 * @param skyAnnulusInnerRadii the skyAnnulusInnerRadii to set
	 */
	public void setSkyAnnulusInnerRadii(Double skyAnnulusInnerRadii) {
		this.skyAnnulusInnerRadii = skyAnnulusInnerRadii;
	}

	/**
	 * @return the skyAnnulusOuterRadii
	 */
	@Override
	public Double getSkyAnnulusOuterRadii() {
		if(this.skyAnnulusOuterRadii == null) {
			this.skyAnnulusOuterRadii = readDoubleFromInput("Enter Sky annulus outer radii in degrees:");
		}
		return this.skyAnnulusOuterRadii;	
	}

	/**
	 * @param skyAnnulusOuterRadii the skyAnnulusOuterRadii to set
	 */
	public void setSkyAnnulusOuterRadii(Double skyAnnulusOuterRadii) {
		this.skyAnnulusOuterRadii = skyAnnulusOuterRadii;
	}

	/**
	 * @param isLacMixedMode the isLacMixedMode to set
	 */
	public void setLacMixedMode(Boolean isLacMixedMode) {
		this.isLacMixedMode = isLacMixedMode;
	}

}
