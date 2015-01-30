package org.ginga.toolbox.gti;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.target.TargetEntity;
import org.ginga.toolbox.target.dao.TargetDaoException;
import org.ginga.toolbox.target.dao.impl.TargetDaoImpl;

public class GingaGtiWriter {

    private final static Logger log = Logger.getLogger(GingaGtiWriter.class);

    public static void main(String[] args) {
        String target = "GS2000+25";
        File f = new File("/tmp/" + target + "_REGION.DATA");
        try {
            // query entities matching the criteria
            LacdumpDao dao = new LacdumpDaoImpl();
            // List<LacDumpSfEntity> sfList = dao.findSfList("H", "MPC2", target,
            // "1988-04-30 04:41:00", "1988-04-30 04:47:00", 5.0, 10.0);
            List<LacdumpSfEntity> sfList = dao.findSfList("MPC3", target, "1988-05-02 01:34:31",
                    "1988-05-02 03:09:27", 5.0, 10.0);
            log.info("Query executed successfully. " + sfList.size() + " result(s) found");
            // save matching results into a GTI file
            GingaGtiWriter gtiWriter = new GingaGtiWriter();
            gtiWriter.writeToFile(target, sfList, false, true, f);
            log.debug("GTI file " + f.getPath() + " written successfully");
        } catch (Exception e) {
            log.error("Error generating GTI file " + f.getPath(), e);
        }
    }

    public String writeToString(String target, List<LacdumpSfEntity> sfList, boolean isBackground,
            boolean dataBlocks) throws IOException {
        StringWriter writer = new StringWriter();
        write(target, sfList, isBackground, dataBlocks, writer);
        return writer.toString();
    }

    public void writeToFile(String target, List<LacdumpSfEntity> sfList, boolean isBackground,
            boolean dataBlocks, File file) throws IOException {
        write(target, sfList, isBackground, dataBlocks, new FileWriter(file));
    }

    public void write(String target, List<LacdumpSfEntity> sfList, boolean isBackground,
            boolean dataBlocks, Writer writer) throws IOException {
        try {
            DecimalFormat df = new DecimalFormat("#.0000");
            // add TGT line with target resolved into B1950 coordinates
            if (!isBackground) {
                String targetLine = null;
                try {
                    TargetEntity targetEntity = new TargetDaoImpl().findByName(target);
                    if (targetEntity != null) {
                        if (targetEntity.getRaDegB1950() == 0 && targetEntity.getDecDegB1950() == 0) {
                            throw new TargetDaoException(
                                    "Target found in database but coordinates not resolved");
                        }
                        targetLine = "'TGT' " + df.format(targetEntity.getRaDegB1950()) + " "
                                + df.format(targetEntity.getDecDegB1950()) + "  '"
                                + targetEntity.getTargetName() + "'\n";
                    }
                } catch (TargetDaoException e) {
                    log.warn("Could not resolve target. Message= " + e.getMessage());
                    Scanner scanner = new Scanner(System.in);
                    targetLine = "'TGT' ";
                    boolean catcher = false;
                    do {
                        try {
                            System.out.println("Enter coordinates for " + target
                                    + " (epoch=B1950, unit=degrees)");

                            System.out.println("[e.g. 300.1786, 25.0954]:");
                            String[] coordinates = scanner.next().split(",");
                            targetLine += Double.valueOf(coordinates[0]).toString() + " ";
                            targetLine += Double.valueOf(coordinates[1]).toString() + "  ";
                            targetLine += "'" + target + "'\n";
                            catcher = true;
                        } catch (Exception e2) {
                            System.out
                                    .println("Coordinates input format is not correct, please try again");
                            System.out.println("[e.g. 300.1786, 25.0954]");
                        } finally {
                            scanner.nextLine();
                        }
                    } while (!catcher);
                    scanner.close();
                } finally {
                    writer.write(targetLine);
                }
            }

            if (!dataBlocks) { // add DATA line
                writer.write("'DATA' \n");
            }

            // add Super Frame and Sequence Numbers
            String lastSuperFrame = null;
            int lastSeqNo = -1;
            for (LacdumpSfEntity sf : sfList) {
                if (!sf.getSuperFrame().equals(lastSuperFrame)) { // new SF
                    if (lastSuperFrame != null) {
                        writer.write("'E' " + lastSeqNo + " 63  63/\n"); // end previous
                        if (dataBlocks) { // close DATA
                            writer.write("'END'\n");
                        }
                    }
                    if (dataBlocks) { // add DATA line
                        writer.write("'DATA' \n");
                    }
                    writer.write("'PASS' '" + sf.getSuperFrame() + "' / \n");
                    writer.write("'B' " + sf.getSequenceNumber() + "  0   0/\n"); // begin
                } else if (sf.getSequenceNumber() > lastSeqNo + 1) {
                    writer.write("'E' " + lastSeqNo + " 63  63 /\n"); // end previous
                    writer.write("'B' " + sf.getSequenceNumber() + "  0   0/\n"); // begin
                }
                lastSeqNo = sf.getSequenceNumber();
                lastSuperFrame = sf.getSuperFrame();
            }
            if (lastSeqNo > 0) {
                writer.write("'E' " + lastSeqNo + " 63  63/\n"); // end previous
                if (dataBlocks) { // close DATA
                    writer.write("'END'\n");
                }
            }
            if (!dataBlocks) { // close DATA
                writer.write("'END'\n");
            }
            writer.flush();
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                log.warn("Error closing writer stream");
            }
        }
    }
}
