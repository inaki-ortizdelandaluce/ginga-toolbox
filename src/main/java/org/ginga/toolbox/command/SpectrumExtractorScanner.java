package org.ginga.toolbox.command;

import java.text.ParseException;
import java.util.Scanner;

import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.util.Constants.BackgroundSubtractionMethod;
import org.ginga.toolbox.util.DateUtil;
import org.ginga.toolbox.util.Constants.LacMode;

public class SpectrumExtractorScanner {

	private Scanner scanner;
	
	public SpectrumExtractorScanner() {
		
	}
	
	public void close() {
		if(this.scanner != null) {
			this.scanner.close();
		}
	}
	
	private void initialize() {
		this.scanner = new Scanner(System.in);
	}
	
	public SingleModeTargetObservation scanAll() throws IllegalArgumentException, ParseException, NumberFormatException {
		if(this.scanner == null) {
			initialize();
		}
		SingleModeTargetObservation obs = new SingleModeTargetObservation();
		obs.setObsId(scanObsId());
		obs.setTarget(scanTarget());
		obs.setMode(scanLacMode());
		obs.setStartTime(scanStartTime());
		obs.setEndTime(scanEndTime());
		return obs;
	}
	
	public long scanObsId() throws NumberFormatException {
		if(this.scanner == null) {
			initialize();
		}
		System.out.print("Enter Observation Id: ");
    	return Long.valueOf(scanner.next()).longValue();
	}
	
	public String scanTarget() {
		if(this.scanner == null) {
			initialize();
		}
		System.out.print("Enter Target Name: ");
    	return scanner.next().trim();
	}
	
	public String scanLacMode() throws IllegalArgumentException {
		if(this.scanner == null) {
			initialize();
		}
		System.out.print("Enter LAC Mode (" + SpectrumExtractorCmd.getLacModes() + "): ");
    	String mode = scanner.next();
    	// check value
    	try {
    		LacMode.valueOf(mode);
    	} catch(IllegalArgumentException e2) {
    		throw new IllegalArgumentException(mode + " is not a valid LAC mode");
    	}
    	return mode;
	}
	
	public BackgroundSubtractionMethod scanBackgroundMethod() throws IllegalArgumentException {
		if(this.scanner == null) {
			initialize();
		}
		System.out.print("Enter Background Subtraction Method (" + SpectrumExtractorCmd.getBackgroundSubtractionMethods() + "): ");
    	String method = scanner.next();
    	// check value
    	try {
    		return BackgroundSubtractionMethod.valueOf(method);
    	} catch(IllegalArgumentException e2) {
    		throw new IllegalArgumentException(method + " is not a valid background subtraction method");
    	}
	}

	public String scanStartTime() throws ParseException {
		if(this.scanner == null) {
			initialize();
		}
		System.out.print("Enter Observation Start Time (" + SpectrumExtractorCmd.DATE_FORMAT_PATTERN + "): ");
		String startTime = scanner.next();
		// check value
		DateUtil.parseInputFormat(startTime);
		return startTime;
	}
	
	public String scanEndTime() throws ParseException {
		if(this.scanner == null) {
			initialize();
		}
		System.out.print("Enter Observation End Time (" + SpectrumExtractorCmd.DATE_FORMAT_PATTERN + "): ");
		String endTime = scanner.next();
		// check value
		DateUtil.parseInputFormat(endTime);
		return endTime;
	}	
}