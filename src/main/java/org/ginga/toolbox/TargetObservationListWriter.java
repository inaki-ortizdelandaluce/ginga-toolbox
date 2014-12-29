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
import org.ginga.toolbox.observation.TargetSingleModeObservation;
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

        List<TargetSingleModeObservation> singleModeObsList = null;
        for (ObservationEntity obsEntity : obsList) {
        	singleModeObsList = obsEntity.getSingleModeObsList();
            if (singleModeObsList != null) {
                for (TargetSingleModeObservation singleModeObs : singleModeObsList) {
                    this.writer.println(" "
                            + String.format(
                                    "%18s",
                                    obsEntity.getId() + " " 
                                    + obsEntity.getSequenceNumber() + " "
                                            + String.format("%8s", singleModeObs.getMode()) + " "
                                            + String.format("%20s", singleModeObs.getStartTime()) + " "
                                            + String.format("%20s", singleModeObs.getEndTime())));
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

        List<TargetSingleModeObservation> singleModeObsList = null;
        for (ObservationEntity obsEntity : obsList) {
        	singleModeObsList = obsEntity.getSingleModeObsList();
            if (singleModeObsList != null) {
                for (TargetSingleModeObservation singleModeObs : singleModeObsList) {
                	LacMode mode = singleModeObs.getLacMode();
                	if(mode.equals(LacMode.MPC1) || mode.equals(LacMode.MPC2)) {
                        this.writer.println(" "
                                + String.format(
                                        "%18s",
                                        obsEntity.getId() + " " 
                                        + obsEntity.getSequenceNumber() + " "
                                                + String.format("%8s", singleModeObs.getMode()) + " "
                                                + String.format("%20s", singleModeObs.getStartTime()) + " "
                                                + String.format("%20s", singleModeObs.getEndTime())));
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
        String target = args[0];
        File workingDir = new File(GingaToolboxEnvironment.getInstance().getGingaWrkDir());
        if(!workingDir.exists()) {
        	workingDir.mkdirs();
        }
        File file = new File(workingDir, target.replace(" ", "_")+ "-observations.txt");
        FileWriter writer = new FileWriter(file);
        TargetObservationListWriter summary = new TargetObservationListWriter(writer);
        summary.writeSpectralModes(target);
    }
}
