package org.ginga.toolbox.command;

import java.text.ParseException;
import java.util.Scanner;

import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.TimeUtil;

public class InputScanner {

    private Scanner scanner;

    public InputScanner() {

    }

    public void close() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }

    private void initialize() {
        this.scanner = new Scanner(System.in);
    }

    public long scanObsId() throws NumberFormatException {
        if (this.scanner == null) {
            initialize();
        }
        System.out.print("Enter Observation Id: ");
        return Long.valueOf(this.scanner.next()).longValue();
    }

    public String scanTarget() {
        if (this.scanner == null) {
            initialize();
        }
        System.out.print("Enter Target Name: ");
        return this.scanner.next().trim();
    }

    public String scanLacMode() throws IllegalArgumentException {
        if (this.scanner == null) {
            initialize();
        }
        System.out.print("Enter LAC Mode (" + SpectrumExtractorCmd.getLacModes() + "): ");
        String mode = this.scanner.next();
        // check value
        try {
            LacMode.valueOf(mode);
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException(mode + " is not a valid LAC mode");
        }
        return mode;
    }

    public BgSubtractionMethod scanBackgroundMethod() throws IllegalArgumentException {
        if (this.scanner == null) {
            initialize();
        }
        System.out.print("Enter Background Subtraction Method ("
                + SpectrumExtractorCmd.getBgSubtractionMethods() + "): ");
        String method = this.scanner.next();
        // check value
        try {
            return BgSubtractionMethod.valueOf(method);
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException(method
                    + " is not a valid background subtraction method");
        }
    }

    public String scanStartTime() throws ParseException {
        if (this.scanner == null) {
            initialize();
        }
        System.out.print("Enter Observation Start Time ("
                + SpectrumExtractorCmd.DATE_FORMAT_PATTERN + "): ");
        String startTime = this.scanner.next();
        // check value
        TimeUtil.parseInputFormat(startTime);
        return startTime;
    }

    public String scanEndTime() throws ParseException {
        if (this.scanner == null) {
            initialize();
        }
        System.out.print("Enter Observation End Time (" + SpectrumExtractorCmd.DATE_FORMAT_PATTERN
                + "): ");
        String endTime = this.scanner.next();
        // check value
        TimeUtil.parseInputFormat(endTime);
        return endTime;
    }
}