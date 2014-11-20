package org.ginga.tools.lacdump.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LACDumpEntity;
import org.ginga.tools.lacdump.LACDumpEntityList;

public class LACDumpParser {

    private static final Logger log = Logger.getLogger(LACDumpParser.class);

    private static final String SUPERFRAME_PREFIX = "*";
    private static final String LHV_ON_DATA_PREFIX = "LHV ON DATA";

    private final int seqnoBeginIdx = 0;
    private final int seqnoLength = 5;
    private final int dateBeginIdx = 5;
    private final int dateLength = 17;
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("yyMMdd hh:mm:s");
    private final int bitRateBeginIdx = 22;
    private final int bitRateLength = 2;
    private final int modeBeginIdx = 24;
    private final int modeLength = 4;
    private final int gmuBeginIdx = 28;
    private final int gmuLength = 4;
    private final int attitudeBeginIdx = 32;
    private final int attitudeLength = 4;
    private final int directionBeginIdx = 36;
    private final int directionLength = 4;
    private final int lowCountRateBeginIdx = 40;
    private final int lowCountRateLength = 8;
    private final int highCountRateBeginIdx = 48;
    private final int highCountRateLength = 7;
    private final int sudCountRateBeginIdx = 56;
    private final int sudCountRateLength = 9;
    private final int piCountRateBeginIdx = 65;
    private final int piCountRateLength = 7;
    private final int rigidityBeginIdx = 72;
    private final int rigidityLength = 4;
    private final int elevationBeginIdx = 76;
    private final int elevationLength = 7;
    private final int raDegBeginIndex = 85;
    private final int raDegLength = 6;
    private final int decDegBeginIndex = 91;
    private final int decDegLength = 7;
    private final int targetBeginIdx = 99;
    private final int targetLength = 8;
    private final int transmissionBeginIdx = 107;
    private final int transmissionLength = 9;
    private final int spinAxisRaDegBeginIdx = 119;
    private final int spinAxisRaDegLength = 6;
    private final int spinAxisDecDegBeginIdx = 125;
    private final int spinAxisDecDegLength = 7;
    		
    public LACDumpParser() {
    }

    public LACDumpEntityList parse(File lacdumpFile) throws IOException {
        LACDumpEntityList entityList = new LACDumpEntityList();
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(lacdumpFile));
            
            String line = null;
            String lastSuperFrame = null;
            LACDumpEntity entity = null;

            int seqno;
            Date date;
            String bitRate;
            String mode;
            String gmu;
            String attitude;
            String direction;
            double lowEnergyCountRate;
            double highEnergyCountRate;
            double sudCountRate;
            double piCountRate;
            double rigidity;
            double elevation;
            double raDeg;
            double decDeg;
            String target;
            double transmission;
            double spinAxisRaDeg;
            double spinAxisDecDeg;
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
                    direction = line.substring(directionBeginIdx,
                            directionBeginIdx + directionLength - 1).trim();
                    log.debug("S/E " + direction);
                    entity.setDirection(direction);
                    
                    // low energy count rate
                    try {
                    	lowEnergyCountRate = Double.valueOf(line.substring(lowCountRateBeginIdx,
                            lowCountRateBeginIdx + lowCountRateLength - 1).trim()).doubleValue();
                    	log.debug("LAC-L " + lowEnergyCountRate);
                    	entity.setLowEnergyCountRate(lowEnergyCountRate);
                    } catch (NumberFormatException e) {
                    	log.debug("LAC-L isNaN. Value: " + line.substring(lowCountRateBeginIdx,
                                lowCountRateBeginIdx + lowCountRateLength - 1).trim());
                    }
                    
                    // higher energy count rate
                    try {
                    	highEnergyCountRate = Double.valueOf(line.substring(highCountRateBeginIdx,
                            highCountRateBeginIdx + highCountRateLength - 1).trim()).doubleValue();
                    	log.debug("LAC-H " + highEnergyCountRate);
                    	entity.setHighEnergyCountRate(highEnergyCountRate);
                    } catch (NumberFormatException e) {
                    	log.debug("LAC-H isNaN. Value: " + line.substring(highCountRateBeginIdx,
                                highCountRateBeginIdx + highCountRateLength - 1).trim());
                    }
 
                    // sud count rate
                    try {
                    	sudCountRate = Double.valueOf(line.substring(sudCountRateBeginIdx,
                            sudCountRateBeginIdx + sudCountRateLength - 1).trim()).doubleValue();
                    	log.debug("SUD " + sudCountRate);
                    	entity.setSUDCountRate(sudCountRate);
                    } catch (NumberFormatException e) {
                    	log.debug("SUD isNaN. Value: " + line.substring(sudCountRateBeginIdx,
                                sudCountRateBeginIdx + sudCountRateLength - 1).trim());
                    }
                    
                    // PI count rate
                    try {
                    	piCountRate = Double.valueOf(line.substring(piCountRateBeginIdx,
                            piCountRateBeginIdx + piCountRateLength - 1).trim()).doubleValue();
                    	log.debug("PIMN " + piCountRate);
                    	entity.setPIMonitorCountRate(piCountRate);
                    } catch (NumberFormatException e) {
                    	log.debug("PIMN isNaN. Value: " + line.substring(piCountRateBeginIdx,
                                piCountRateBeginIdx + piCountRateLength - 1).trim());
                    }

                    // rigidity
                    try {
                    	rigidity = Double.valueOf(line.substring(rigidityBeginIdx,
                            rigidityBeginIdx + rigidityLength - 1).trim()).doubleValue();
                    	log.debug("RIG " + rigidity);
                    	entity.setCutoffRigidity(rigidity);
                    } catch (NumberFormatException e) {
                    	log.debug("RIG isNaN. Value: " + line.substring(rigidityBeginIdx,
                                rigidityBeginIdx + rigidityLength - 1).trim());
                    }

                    // elevation
                    try {
                    	elevation = Double.valueOf(line.substring(elevationBeginIdx,
                            elevationBeginIdx + elevationLength - 1).trim()).doubleValue();
                    	log.debug("EELV " + elevation);
                    	entity.setElevation(elevation);
                    } catch (NumberFormatException e) {
                    	log.debug("EELV isNaN. Value: " + line.substring(elevationBeginIdx,
                                elevationBeginIdx + elevationLength - 1).trim());
                    }
                    
                    // ra
                    try {
                    	raDeg = Double.valueOf(line.substring(raDegBeginIndex,
                            raDegBeginIndex + raDegLength - 1).trim()).doubleValue();
                    	log.debug("RA " + raDeg);
                    	entity.setRaDegB1950(raDeg);
                    } catch (NumberFormatException e) {
                    	log.debug("RA isNaN. Value: " + line.substring(raDegBeginIndex,
                                raDegBeginIndex + raDegLength - 1).trim());
                    }

                    // dec
                    try {
                    	decDeg = Double.valueOf(line.substring(decDegBeginIndex,
                            decDegBeginIndex + decDegLength - 1).trim()).doubleValue();
                    	log.debug("DEC " + decDeg);
                    	entity.setDecDegB1950(decDeg);
                    } catch (NumberFormatException e) {
                    	log.debug("DEC isNaN. Value: " + line.substring(decDegBeginIndex,
                                decDegBeginIndex + decDegLength - 1).trim());
                    }

                    // target
                    target = line.substring(targetBeginIdx,
                           targetBeginIdx + targetLength - 1).trim();
                    log.debug("TARGET " + target);
                    entity.setTarget(target);

                    // transmission
                    try {
                    	transmission = Double.valueOf(line.substring(transmissionBeginIdx,
                            transmissionBeginIdx + transmissionLength - 1).trim()).doubleValue();
                    	log.debug("TRANSMISSION " + transmission);
                    	entity.setTransmission(transmission);
                    } catch (NumberFormatException e) {
                    	log.debug("TRANSMISSION isNaN. Value: " + line.substring(transmissionBeginIdx,
                                transmissionBeginIdx + transmissionLength - 1).trim());
                    }

                    //  spin axis ra
                    try {
                    	spinAxisRaDeg = Double.valueOf(line.substring(spinAxisRaDegBeginIdx,
                            spinAxisRaDegBeginIdx + spinAxisRaDegLength - 1).trim()).doubleValue();
                    	log.debug("SPIN AXIS RA " + spinAxisRaDeg);
                    	entity.setSpinAxisRaDeg(spinAxisRaDeg);
                    } catch (NumberFormatException e) {
                    	log.debug("SPIN AXIS RA isNaN. Value: " + line.substring(spinAxisRaDegBeginIdx,
                                spinAxisRaDegBeginIdx + spinAxisRaDegLength - 1).trim());
                    }

                    // spin axis dec
                    try {
                    	spinAxisDecDeg = Double.valueOf(line.substring(spinAxisDecDegBeginIdx,
                            spinAxisDecDegBeginIdx + spinAxisDecDegLength - 1).trim()).doubleValue();
                    	log.debug("SPIN AXIS DEC " + spinAxisDecDeg);
                    	entity.setSpinAxisDecDeg(spinAxisDecDeg);
                    } catch (NumberFormatException e) {
                    	log.debug("SPIN AXIS DEC isNaN. Value: " + line.substring(spinAxisDecDegBeginIdx,
                                spinAxisDecDegBeginIdx + spinAxisDecDegLength - 1).trim());
                    }
                    // add entity to list
                    entityList.addEntity(entity);
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
                LACDumpParser parser = new LACDumpParser();
                LACDumpEntityList entityList = parser.parse(f);
                log.info("LACDUMP contains " + entityList.getEntityCount() + " row(s)");
            } catch (IOException e) {
                log.error("Error parsing LACDUMP " + f.getPath() + ". Message=" + e.getMessage());
            }
        }
    }
}
