package org.ginga.toolbox.environment;

import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.ginga.toolbox.util.Constants;
import org.ginga.toolbox.util.Constants.BitRate;

public class InteractiveDataReductionEnv implements DataReductionEnv {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(InteractiveDataReductionEnv.class);

	private Double elevationMin;
	private Double elevationMax;
	private Double cutOffRigidityMin;
	private Double cutOffRigidityMax;
	private BitRate bitRate;
	private Scanner scanner;
	
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
	
}
