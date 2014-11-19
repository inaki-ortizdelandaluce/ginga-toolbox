package org.ginga.tools.lacdump;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class LACDumpParser {

    private static final Logger log = Logger.getLogger(LACDumpParser.class);

    private static final String SUPERFRAME_PREFIX = "*";
    private static final String LHV_ON_DATA_PREFIX = "LHV ON DATA";

    private File lacdumpFile;

    public LACDumpParser(File f) {
        this.lacdumpFile = f;
    }

    public LACDumpEntityList parse() throws IOException {
        LACDumpEntityList entityList = new LACDumpEntityList();
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(this.lacdumpFile));
            String line = null;
            String lastSuperFrame = null;
            LACDumpEntity entity = null;

            int seqno;
            int seqnoBeginIdx = 0;
            int seqnoLength = 5;
            Date date;
            int dateBeginIdx = 5;
            int dateLength = 17;
            SimpleDateFormat dateFmt = new SimpleDateFormat("yyMMdd hh:mm:s");
            String bitRate;
            int bitRateBeginIdx = 22;
            int bitRateLength = 2;
            String mode;
            int modeBeginIdx = 24;
            int modeLength = 4;
            String gmu;
            int gmuBeginIdx = 28;
            int gmuLength = 4;
            String attitude;
            int attitudeBeginIdx = 32;
            int attitudeLength = 4;
            String direction;
            int directionBeingIdx = 36;
            int directionLength = 4;

            while ((line = reader.readLine()) != null) {

                log.debug("Line " + reader.getLineNumber() + ": " + line);

                if (line.startsWith(SUPERFRAME_PREFIX)) {
                    if (lastSuperFrame != null) {
                        log.debug("END Super Frame " + lastSuperFrame);
                    }
                    lastSuperFrame = line.substring(1, line.length() - 1).trim();
                    log.debug("BEGIN Super Frame " + lastSuperFrame);

                } else if (line.contains(LHV_ON_DATA_PREFIX)) {
                    continue;

                } else {
                    entity = new LACDumpEntity();
                    entity.setSuperFrame(lastSuperFrame);
                    // seqno
                    seqno = Integer.valueOf(
                            line.substring(seqnoBeginIdx, seqnoBeginIdx + seqnoLength - 1).trim())
                            .intValue();
                    log.debug("SEQ_NO " + seqno);
                    entity.setSequenceNumber(seqno);

                    // datetime
                    date = dateFmt.parse(line.substring(dateBeginIdx, dateBeginIdx + dateLength)
                            .trim());
                    log.debug("DATE " + new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss").format(date));
                    entity.setDate(date);

                    // bitrate
                    bitRate = line.substring(bitRateBeginIdx, bitRateBeginIdx + bitRateLength - 1)
                            .trim();
                    log.debug("BR " + bitRate);
                    entity.setBitRate(bitRate);

                    // mode
                    mode = line.substring(modeBeginIdx, modeBeginIdx + modeLength - 1).trim();
                    log.debug("MODE " + mode);
                    entity.setMode(mode);

                    // gain and medium/upper discriminator
                    gmu = line.substring(gmuBeginIdx, gmuBeginIdx + gmuLength - 1).trim();
                    log.debug("GMU " + gmu);
                    entity.setGainAndDiscriminators(gmu);

                    // attitude status
                    attitude = line.substring(attitudeBeginIdx,
                            attitudeBeginIdx + attitudeLength - 1).trim();
                    log.debug("ACM " + attitude);
                    entity.setAttitudeStatus(attitude);

                    // direction
                    direction = line.substring(directionBeingIdx,
                            directionBeingIdx + directionLength - 1).trim();
                    log.debug("S/E " + direction);
                    entity.setDirection(direction);

                    // add entity to list
                    entityList.addEntity(entity);
                    break; // To be removed

                }
            }
        } catch (IOException | ParseException e) {
            throw new IOException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.debug("Could not close file reader");
                }
            }
        }

        return entityList;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage org.ginga.tools.lacdump.LacDumpParser <LAC dump file path>");
            System.exit(1);
        } else {
            File f = new File(args[0]);
            if (!f.exists()) {
                log.error("File " + f.getPath() + " not found");
                System.exit(1);
            }
            try {
                LACDumpParser parser = new LACDumpParser(f);
                LACDumpEntityList entityList = parser.parse();
                log.info("LACDUMP contains " + entityList.getEntityCount() + " row(s)");
            } catch (IOException e) {
                log.error("Error parsing LACDUMP " + f.getPath() + ". Message=" + e.getMessage());
            }
        }
    }
}
