package org.ginga.tools.lacdump.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LACDumpEntity;
import org.ginga.tools.lacdump.LACDumpEntityList;

public class LACDumpParser {

    private static final Logger log = Logger.getLogger(LACDumpParser.class);

    private static final String SUPERFRAME_PREFIX = "*";
    private static final String LHV_ON_DATA_PREFIX = "LHV ON DATA";

    private enum DirectionEnum {
        SKY, NTE, DYE
    }

    private enum ModeEnum {
        MPC1, MPC2, MPC3, ACS, PCHK
    }

    private enum BitRateEnum {
        H, M, L
    }

    private enum AttitudeEnum {
        NML("NML"), SL_PLUS("SL+"), SL_MINUS("SL-"), S36("S36"), MAN("MAN");

        String value;

        AttitudeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    private final int seqnoBeginIdx = 0;
    private final int seqnoLength = 5;
    private final int dateBeginIdx = 5;
    private final int dateLength = 16;
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("yyMMdd hh:mm:s");
    private final int bitRateBeginIdx = 21;
    private final int bitRateLength = 2;
    private final int modeBeginIdx = 23;
    private final int modeLength = 5;
    private final int gmuBeginIdx = 28;
    private final int gmuLength = 4;
    private final int attitudeBeginIdx = 31;
    private final int attitudeLength = 5;
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
    private final int elevationLength = 8;
    private final int raDegBeginIndex = 84;
    private final int raDegLength = 7;
    private final int decDegBeginIndex = 91;
    private final int decDegLength = 7;
    private final int targetBeginIdx = 98;
    private final int targetLength = 13;
    private final int transmissionBeginIdx = 111;
    private final int transmissionLength = 5;
    private final int spinAxisRaDegBeginIdx = 118;
    private final int spinAxisRaDegLength = 7;
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
                            line.substring(this.seqnoBeginIdx,
                                    this.seqnoBeginIdx + this.seqnoLength - 1).trim()).intValue();
                    log.debug("SEQ_NO " + seqno);
                    entity.setSequenceNumber(seqno);

                    // datetime
                    date = this.dateFmt.parse(line.substring(this.dateBeginIdx,
                            this.dateBeginIdx + this.dateLength).trim());
                    log.debug("DATE " + new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss").format(date));
                    entity.setDate(date);

                    // bitrate
                    bitRate = line.substring(this.bitRateBeginIdx,
                            this.bitRateBeginIdx + this.bitRateLength - 1).trim();
                    log.debug("BR " + bitRate);
                    if (EnumUtils.isValidEnum(BitRateEnum.class, bitRate)) {
                        entity.setBitRate(bitRate);
                    }
                    // mode
                    mode = line.substring(this.modeBeginIdx,
                            this.modeBeginIdx + this.modeLength - 1).trim();
                    log.debug("MODE " + mode);
                    if (EnumUtils.isValidEnum(ModeEnum.class, mode)) {
                        entity.setMode(mode);
                    }

                    // gain and medium/upper discriminator
                    gmu = line.substring(this.gmuBeginIdx, this.gmuBeginIdx + this.gmuLength - 1)
                            .trim();
                    log.debug("GMU " + gmu);
                    entity.setGainAndDiscriminators(gmu);

                    // attitude status
                    attitude = line.substring(this.attitudeBeginIdx,
                            this.attitudeBeginIdx + this.attitudeLength - 1).trim();
                    log.debug("ACM " + attitude);
                    if (isValidAttitudeValue(attitude)) { // use ad-hoc validation method (forbidden
                                                          // chars in the String)
                        entity.setAttitudeStatus(attitude);
                    }
                    // direction
                    direction = line.substring(this.directionBeginIdx,
                            this.directionBeginIdx + this.directionLength - 1).trim();
                    log.debug("S/E " + direction);
                    if (EnumUtils.isValidEnum(DirectionEnum.class, direction)) {
                        entity.setDirection(direction);
                    }

                    // low energy count rate
                    try {
                        lowEnergyCountRate = Double.valueOf(
                                line.substring(this.lowCountRateBeginIdx,
                                        this.lowCountRateBeginIdx + this.lowCountRateLength - 1)
                                        .trim()).doubleValue();
                        log.debug("LAC-L " + lowEnergyCountRate);
                        entity.setLowEnergyCountRate(lowEnergyCountRate);
                    } catch (NumberFormatException e) {
                        log.debug("LAC-L isNaN. Value: "
                                + line.substring(this.lowCountRateBeginIdx,
                                        this.lowCountRateBeginIdx + this.lowCountRateLength - 1)
                                        .trim());
                    }

                    // higher energy count rate
                    try {
                        highEnergyCountRate = Double.valueOf(
                                line.substring(this.highCountRateBeginIdx,
                                        this.highCountRateBeginIdx + this.highCountRateLength - 1)
                                        .trim()).doubleValue();
                        log.debug("LAC-H " + highEnergyCountRate);
                        entity.setHighEnergyCountRate(highEnergyCountRate);
                    } catch (NumberFormatException e) {
                        log.debug("LAC-H isNaN. Value: "
                                + line.substring(this.highCountRateBeginIdx,
                                        this.highCountRateBeginIdx + this.highCountRateLength - 1)
                                        .trim());
                    }

                    // sud count rate
                    try {
                        sudCountRate = Double.valueOf(
                                line.substring(this.sudCountRateBeginIdx,
                                        this.sudCountRateBeginIdx + this.sudCountRateLength - 1)
                                        .trim()).doubleValue();
                        log.debug("SUD " + sudCountRate);
                        entity.setSUDCountRate(sudCountRate);
                    } catch (NumberFormatException e) {
                        log.debug("SUD isNaN. Value: "
                                + line.substring(this.sudCountRateBeginIdx,
                                        this.sudCountRateBeginIdx + this.sudCountRateLength - 1)
                                        .trim());
                    }

                    // PI count rate
                    try {
                        piCountRate = Double.valueOf(
                                line.substring(this.piCountRateBeginIdx,
                                        this.piCountRateBeginIdx + this.piCountRateLength - 1)
                                        .trim()).doubleValue();
                        log.debug("PIMN " + piCountRate);
                        entity.setPIMonitorCountRate(piCountRate);
                    } catch (NumberFormatException e) {
                        log.debug("PIMN isNaN. Value: "
                                + line.substring(this.piCountRateBeginIdx,
                                        this.piCountRateBeginIdx + this.piCountRateLength - 1)
                                        .trim());
                    }

                    // rigidity
                    try {
                        rigidity = Double.valueOf(
                                line.substring(this.rigidityBeginIdx,
                                        this.rigidityBeginIdx + this.rigidityLength - 1).trim())
                                        .doubleValue();
                        log.debug("RIG " + rigidity);
                        entity.setCutoffRigidity(rigidity);
                    } catch (NumberFormatException e) {
                        log.debug("RIG isNaN. Value: "
                                + line.substring(this.rigidityBeginIdx,
                                        this.rigidityBeginIdx + this.rigidityLength - 1).trim());
                    }

                    // elevation
                    try {
                        elevation = Double.valueOf(
                                line.substring(this.elevationBeginIdx,
                                        this.elevationBeginIdx + this.elevationLength - 1).trim())
                                        .doubleValue();
                        log.debug("EELV " + elevation);
                        entity.setElevation(elevation);
                    } catch (NumberFormatException e) {
                        log.debug("EELV isNaN. Value: "
                                + line.substring(this.elevationBeginIdx,
                                        this.elevationBeginIdx + this.elevationLength - 1).trim());
                    }

                    // ra
                    try {
                        raDeg = Double.valueOf(
                                line.substring(this.raDegBeginIndex,
                                        this.raDegBeginIndex + this.raDegLength - 1).trim())
                                        .doubleValue();
                        log.debug("RA " + raDeg);
                        entity.setRaDegB1950(raDeg);
                    } catch (NumberFormatException e) {
                        log.debug("RA isNaN. Value: "
                                + line.substring(this.raDegBeginIndex,
                                        this.raDegBeginIndex + this.raDegLength - 1).trim());
                    }

                    // dec
                    try {
                        decDeg = Double.valueOf(
                                line.substring(this.decDegBeginIndex,
                                        this.decDegBeginIndex + this.decDegLength - 1).trim())
                                        .doubleValue();
                        log.debug("DEC " + decDeg);
                        entity.setDecDegB1950(decDeg);
                    } catch (NumberFormatException e) {
                        log.debug("DEC isNaN. Value: "
                                + line.substring(this.decDegBeginIndex,
                                        this.decDegBeginIndex + this.decDegLength - 1).trim());
                    }

                    // target
                    target = line.substring(this.targetBeginIdx,
                            this.targetBeginIdx + this.targetLength - 1).trim();
                    log.debug("TARGET " + target);
                    entity.setTarget(target);

                    // transmission
                    try {
                        transmission = Double.valueOf(
                                line.substring(this.transmissionBeginIdx,
                                        this.transmissionBeginIdx + this.transmissionLength - 1)
                                        .trim()).doubleValue();
                        log.debug("TRANSMISSION " + transmission);
                        entity.setTransmission(transmission);
                    } catch (NumberFormatException e) {
                        log.debug("TRANSMISSION isNaN. Value: "
                                + line.substring(this.transmissionBeginIdx,
                                        this.transmissionBeginIdx + this.transmissionLength - 1)
                                        .trim());
                    }

                    // spin axis ra
                    try {
                        spinAxisRaDeg = Double.valueOf(
                                line.substring(this.spinAxisRaDegBeginIdx,
                                        this.spinAxisRaDegBeginIdx + this.spinAxisRaDegLength - 1)
                                        .trim()).doubleValue();
                        log.debug("SPIN AXIS RA " + spinAxisRaDeg);
                        entity.setSpinAxisRaDeg(spinAxisRaDeg);
                    } catch (NumberFormatException e) {
                        log.debug("SPIN AXIS RA isNaN. Value: "
                                + line.substring(this.spinAxisRaDegBeginIdx,
                                        this.spinAxisRaDegBeginIdx + this.spinAxisRaDegLength - 1)
                                        .trim());
                    }

                    // spin axis dec
                    try {
                        spinAxisDecDeg = Double
                                .valueOf(
                                        line.substring(
                                                this.spinAxisDecDegBeginIdx,
                                                this.spinAxisDecDegBeginIdx
                                                + this.spinAxisDecDegLength - 1).trim())
                                                .doubleValue();
                        log.debug("SPIN AXIS DEC " + spinAxisDecDeg);
                        entity.setSpinAxisDecDeg(spinAxisDecDeg);
                    } catch (NumberFormatException e) {
                        log.debug("SPIN AXIS DEC isNaN. Value: "
                                + line.substring(this.spinAxisDecDegBeginIdx,
                                        this.spinAxisDecDegBeginIdx + this.spinAxisDecDegLength - 1)
                                        .trim());
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

    public static boolean isValidAttitudeValue(String value) {
        AttitudeEnum[] attitudeList = AttitudeEnum.values();
        for (int i = 0; i < attitudeList.length; i++) {
            if (attitudeList[i].getValue().equals(value)) {
                return true;
            }
        }
        return false;
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
