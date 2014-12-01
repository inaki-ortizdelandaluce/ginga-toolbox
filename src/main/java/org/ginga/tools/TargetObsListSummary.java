package org.ginga.tools;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.ginga.tools.observation.ObservationEntity;
import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.pipeline.TargetObservationListPipe;
import org.ginga.tools.util.Constants.LacMode;

public class TargetObsListSummary {

    public void printAllModes(String target) {
        TargetObservationListPipe pipe = new TargetObservationListPipe();
        pipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = pipe.next();

        PrintWriter writer = new PrintWriter(System.out);
        List<ObservationModeDetails> obsModeList = null;
        writer.println("----------------------------------------------------------");
        writer.println("----------------------------------------------------------");
        for (ObservationEntity obsEntity : obsList) {
            obsModeList = obsEntity.getAvailableModesDetails();
            if (obsModeList != null) {
                for (ObservationModeDetails obsMode : obsModeList) {
                    writer.println(" "
                            + String.format(
                                    "%18s",
                                    obsEntity.getSequenceNumber() + " "
                                            + String.format("%8s", obsMode.getMode()) + " "
                                            + String.format("%20s", obsMode.getStartTime()) + " "
                                            + String.format("%20s", obsMode.getEndTime())));
                }
                writer.println("----------------------------------------------------------");
            }
        }
        writer.println("----------------------------------------------------------");
        writer.flush();
        writer.close();
    }

    public void printSpectralModes(String target) {
        TargetObservationListPipe pipe = new TargetObservationListPipe();
        pipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = pipe.next();

        PrintWriter writer = new PrintWriter(System.out);
        List<ObservationModeDetails> obsModeList = null;
        writer.println("----------------------------------------------------------");
        writer.println("----------------------------------------------------------");
        for (ObservationEntity obsEntity : obsList) {
            obsModeList = obsEntity.getAvailableModesDetails();
            if (obsModeList != null) {
                for (ObservationModeDetails obsMode : obsModeList) {
                	LacMode mode = obsMode.getLacMode();
                	if(mode.equals(LacMode.MPC1) || mode.equals(LacMode.MPC2)) {
                        writer.println(" "
                                + String.format(
                                        "%18s",
                                        obsEntity.getSequenceNumber() + " "
                                                + String.format("%8s", obsMode.getMode()) + " "
                                                + String.format("%20s", obsMode.getStartTime()) + " "
                                                + String.format("%20s", obsMode.getEndTime())));
                	}
                }
                writer.println("----------------------------------------------------------");
            }
        }
        writer.println("----------------------------------------------------------");
        writer.flush();
        writer.close();
    }

    public void printMpc2Modes(String target) {
        TargetObservationListPipe pipe = new TargetObservationListPipe();
        pipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = pipe.next();

        PrintWriter writer = new PrintWriter(System.out);
        List<ObservationModeDetails> obsModeList = null;
        writer.println("----------------------------------------------------------");
        writer.println("----------------------------------------------------------");
        for (ObservationEntity obsEntity : obsList) {
            obsModeList = obsEntity.getAvailableModesDetails();
            if (obsModeList != null) {
                for (ObservationModeDetails obsMode : obsModeList) {
                	LacMode mode = obsMode.getLacMode();
                	if(mode.equals(LacMode.MPC2)) {
                        writer.println(" "
                                + String.format(
                                        "%18s",
                                        obsEntity.getSequenceNumber() + " "
                                                + String.format("%8s", obsMode.getMode()) + " "
                                                + String.format("%20s", obsMode.getStartTime()) + " "
                                                + String.format("%20s", obsMode.getEndTime())));
                	}
                }
                writer.println("----------------------------------------------------------");
            }
        }
        writer.println("----------------------------------------------------------");
        writer.flush();
        writer.close();
    }    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage org.ginga.tools.TargetObsListSummary <target>");
            System.exit(1);
        }
        TargetObsListSummary summary = new TargetObsListSummary();
        summary.printMpc2Modes(args[0]);
        // summary.printSpectralModes(args[0]);
        //summary.printAllModes(args[0]);
    }
}
