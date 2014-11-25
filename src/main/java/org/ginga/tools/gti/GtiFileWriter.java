package org.ginga.tools.gti;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LacDumpSfEntity;
import org.ginga.tools.lacdump.dao.LacDumpDao;
import org.ginga.tools.lacdump.dao.impl.LacDumpDaoImpl;

public class GtiFileWriter {

    private final static Logger log = Logger.getLogger(GtiFileWriter.class);

    public static void main(String[] args) {
        String target = "GS2000+25";
        File f = new File("/tmp/" + target + "_REGION.DATA");
        try {
            // query entities matching the criteria
            LacDumpDao dao = new LacDumpDaoImpl();
            List<LacDumpSfEntity> sfList = dao.findSfList("H", "MPC2", target,
                    "1988-04-30 04:41:00", "1988-04-30 04:47:00", 5.0, 10.0);
            log.info("Query executed successfully. " + sfList.size() + " result(s) found");
            // save matching results into a GTI file
            GtiFileWriter gtiWriter = new GtiFileWriter();
            gtiWriter.write(target, sfList, false, new FileWriter(f));
            log.debug("GTI file " + f.getPath() + " written successfully");
        } catch (Exception e) {
            log.error("Error generating GTI file " + f.getPath(), e);
        }
    }

    public void write(String target, List<LacDumpSfEntity> sfList, boolean isBackground,
            FileWriter writer) throws IOException {
        try {
            // add TGT line with target resolved into B1950 coordinates
            if (!isBackground) {
                try {
                    SimbadTargetResolver resolver = new SimbadTargetResolver();
                    TargetFk4Coordinates coords = resolver.resolve(target);

                    DecimalFormat df = new DecimalFormat("#.0000");
                    writer.write("'TGT' " + df.format(coords.getRaDeg()) + " "
                            + df.format(coords.getDecDeg()) + "  '" + coords.getTargetName()
                            + "'\n");
                } catch (TargetNotResolvedException e) {
                    log.warn("Could not resolve target. Message= " + e.getMessage());
                    log.warn("Do not forget to add the TGT keyword followed by the coordinates "
                            + "of the source (in B1950 and degrees)");
                    log.debug(e.getMessage());
                }
            }
            // add DATA line
            writer.write("'DATA' \n");

            // add Super Frame and Sequence Numbers
            String lastSuperFrame = null;
            int lastSeqNo = -1;
            for (LacDumpSfEntity sf : sfList) {
                if (!sf.getSuperFrame().equals(lastSuperFrame)) { // new SF
                    if (lastSuperFrame != null) {
                        writer.write("'E' " + lastSeqNo + " 63  63 \n"); // end previous
                    }
                    writer.write("'PASS' " + sf.getSuperFrame() + "\n");
                    writer.write("'B' " + sf.getSequenceNumber() + "  0   0\n"); // begin
                } else if (sf.getSequenceNumber() > lastSeqNo + 1) {
                    writer.write("'E' " + lastSeqNo + " 63  63 \n"); // end previous
                    writer.write("'B' " + sf.getSequenceNumber() + "  0   0\n"); // begin
                }
                lastSeqNo = sf.getSequenceNumber();
                lastSuperFrame = sf.getSuperFrame();
            }
            if (lastSeqNo > 0) {
                writer.write("'E' " + lastSeqNo + " 63  63 \n"); // end previous
            }
            // close DATA
            writer.write("'END'\n");
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