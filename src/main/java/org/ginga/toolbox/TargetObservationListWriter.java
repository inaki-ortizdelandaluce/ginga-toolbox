package org.ginga.toolbox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.ginga.toolbox.environment.GingaToolboxEnvironment;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.ObservationModeDetails;
import org.ginga.toolbox.pipeline.TargetObservationListPipe;
import org.ginga.toolbox.util.Constants.LacMode;

public class TargetObservationListWriter {

	private PrintWriter writer;
	
	public TargetObservationListWriter(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	
	public TargetObservationListWriter(PrintStream stream) {
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

    public void writeSpectralModes(String target) {
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

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage org.ginga.toolbox.TargetObservationListWriter <target>");
            System.exit(1);
        }
        File workingDir = new File(GingaToolboxEnvironment.getInstance().getGingaWrkDir());
        if(!workingDir.exists()) {
        	workingDir.mkdirs();
        }
        File file = new File(workingDir, "observation.list");
        FileWriter writer = new FileWriter(file);
        TargetObservationListWriter summary = new TargetObservationListWriter(writer);
        summary.writeSpectralModes(args[0]);
    }
}
