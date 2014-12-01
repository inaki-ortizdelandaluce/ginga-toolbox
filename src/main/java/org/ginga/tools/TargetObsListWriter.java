package org.ginga.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.ginga.tools.observation.ObservationEntity;
import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.pipeline.TargetObservationListPipe;
import org.ginga.tools.runtime.GingaToolsEnvironment;
import org.ginga.tools.util.Constants.LacMode;

public class TargetObsListWriter {

	private PrintWriter writer;
	
	public TargetObsListWriter(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	
	public TargetObsListWriter(PrintStream stream) {
		this.writer = new PrintWriter(stream);
	}
	
    public void writeAllModes(String target) {
        TargetObservationListPipe pipe = new TargetObservationListPipe();
        pipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = pipe.next();

        List<ObservationModeDetails> obsModeList = null;
        for (ObservationEntity obsEntity : obsList) {
            obsModeList = obsEntity.getAvailableModesDetails();
            if (obsModeList != null) {
                for (ObservationModeDetails obsMode : obsModeList) {
                    this.writer.println(" "
                            + String.format(
                                    "%18s",
                                    obsEntity.getSequenceNumber() + " "
                                            + String.format("%8s", obsMode.getMode()) + " "
                                            + String.format("%20s", obsMode.getStartTime()) + " "
                                            + String.format("%20s", obsMode.getEndTime())));
                }
                this.writer.println("----------------------------------------------------------");
            }
        }
        this.writer.flush();
        this.writer.close();
    }

    public void printSpectralModes(String target) {
        TargetObservationListPipe pipe = new TargetObservationListPipe();
        pipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = pipe.next();

        List<ObservationModeDetails> obsModeList = null;
        for (ObservationEntity obsEntity : obsList) {
            obsModeList = obsEntity.getAvailableModesDetails();
            if (obsModeList != null) {
                for (ObservationModeDetails obsMode : obsModeList) {
                	LacMode mode = obsMode.getLacMode();
                	if(mode.equals(LacMode.MPC1) || mode.equals(LacMode.MPC2)) {
                        this.writer.println(" "
                                + String.format(
                                        "%18s",
                                        obsEntity.getSequenceNumber() + " "
                                                + String.format("%8s", obsMode.getMode()) + " "
                                                + String.format("%20s", obsMode.getStartTime()) + " "
                                                + String.format("%20s", obsMode.getEndTime())));
                	}
                }
            }
        }
        this.writer.flush();
        this.writer.close();
    }

    public void printMpc2Modes(String target) {
        TargetObservationListPipe pipe = new TargetObservationListPipe();
        pipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = pipe.next();

        List<ObservationModeDetails> obsModeList = null;
        for (ObservationEntity obsEntity : obsList) {
            obsModeList = obsEntity.getAvailableModesDetails();
            if (obsModeList != null) {
                for (ObservationModeDetails obsMode : obsModeList) {
                	LacMode mode = obsMode.getLacMode();
                	if(mode.equals(LacMode.MPC2)) {
                        this.writer.println(" "
                                + String.format(
                                        "%18s",
                                        obsEntity.getSequenceNumber() + " "
                                                + String.format("%8s", obsMode.getMode()) + " "
                                                + String.format("%20s", obsMode.getStartTime()) + " "
                                                + String.format("%20s", obsMode.getEndTime())));
                	}
                }
            }
        }
        this.writer.flush();
        this.writer.close();
    }    
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage org.ginga.tools.TargetObsListWriter <target>");
            System.exit(1);
        }
        File workingDir = new File(GingaToolsEnvironment.getInstance().getGingaWrkDir());
        if(!workingDir.exists()) {
        	workingDir.mkdirs();
        }
        File file = new File(workingDir, "observation.list");
        FileWriter writer = new FileWriter(file);
        TargetObsListWriter summary = new TargetObsListWriter(writer);
        summary.printSpectralModes(args[0]);
        //summary.printMpc2Modes(args[0]);
        //summary.printAllModes(args[0]);
    }
}
