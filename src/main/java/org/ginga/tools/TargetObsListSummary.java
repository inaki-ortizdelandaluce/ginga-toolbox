package org.ginga.tools;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.ginga.tools.observation.ObservationEntity;
import org.ginga.tools.observation.ObservationModeDetails;
import org.ginga.tools.pipeline.TargetObservationListPipe;

public class TargetObsListSummary {

    public void execute(String target) {
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

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage org.ginga.tools.TargetObsListSummary <target>");
            System.exit(1);
        }
        TargetObsListSummary summary = new TargetObsListSummary();
        summary.execute(args[0]);
    }
}
