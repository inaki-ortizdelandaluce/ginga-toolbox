package org.ginga.toolbox.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ginga.toolbox.util.Constants.BitRate;
import org.ginga.toolbox.util.Constants.LacMode;

public class TimeUtil {

    public static final SimpleDateFormat DATE_FORMAT_DATABASE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_LACDUMP = new SimpleDateFormat("yyMMdd HH:mm:s");
    public static final SimpleDateFormat DATE_FORMAT_FILE = new SimpleDateFormat("yyMMddHHmmss");
    public static final SimpleDateFormat DATE_FORMAT_INPUT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final double[] TIME_RESOLUTION_MPC1 = new double[] { 0.5, 4, 16 };
    private static final double[] TIME_RESOLUTION_MPC2 = new double[] { 0.0625, 0.5, 2 };
    private static final double[] TIME_RESOLUTION_MPC3 = new double[] { 0.0078, 0.0625, 0.250 };
    @SuppressWarnings("unused")
    private static final double[] TIME_RESOLUTION_PCH = new double[] { 0.0019, 0.0156, 0.0625 };
    private static final double[] TIME_RESOLUTION_PCL = new double[] { 0.00098, 0.0078, 0.0313 };

    public static String format(SimpleDateFormat format, Date date) throws ParseException {
        return format(format.toPattern(), date);
    }

    public static String format(String pattern, Date date) throws ParseException {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date parse(String pattern, String date) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    public static String convertDatabaseToFileFormat(String date) throws ParseException {
        Date d = DATE_FORMAT_DATABASE.parse(date);
        return DATE_FORMAT_FILE.format(d);
    }

    public static String formatInputDate(Date date) throws ParseException {
        return format(DATE_FORMAT_INPUT, date);
    }

    public static Date parseInputFormat(String date) throws ParseException {
        return DATE_FORMAT_INPUT.parse(date);
    }

    public static Date parseDatabaseFormat(String date) throws ParseException {
        return DATE_FORMAT_DATABASE.parse(date);
    }

    public static Date parseLacdumpFormat(String date) throws ParseException {
        return DATE_FORMAT_LACDUMP.parse(date);
    }

    /**
     * Returns the optimal time resolution (seconds/bin) given a bit rate and a LAC mode according
     * to the following table:
     *
     * <pre>
     * Mode     High  Medium     Low
     *
     * MPC-1   500ms      4s     16s
     * MPC-2  62.5ms   500ms      2s
     * MPC-3   7.8ms  62.5ms   250ms
     * PC-H    1.9ms  15.6ms  62.5ms
     * PC-L   0.98ms   7.8ms  31.3ms
     * </pre>
     *
     * For ANY bit rate, the lowest time resolution will be returned. For PCHK mode, the PC-L
     * resolution will be returned. For ACS and INIT modes, an IllegalArgumentException will be
     * thrown.
     *
     * @param bitRate the bit rate (ANY, HI, MED, LOW)
     * @param mode the LAC mode. (MPC1
     * @return time resolution in seconds/bin
     */
    public static double getTimeResolution(BitRate bitRate, LacMode mode) {
        // identify bit rate index, 0=HI, 1=MED, 2=LOW
        int idx = 0;
        switch (bitRate) {
        case H:
            idx = 0;
            break;
        case M:
            idx = 1;
            break;
        case L:
            idx = 2;
        case ANY: // lowest time resolution
        default:
            idx = 2;
            break;
        }
        // get time resolution for given index and
        switch (mode) {
        case MPC1:
            return TIME_RESOLUTION_MPC1[idx];
        case MPC2:
            return TIME_RESOLUTION_MPC2[idx];
        case MPC3:
            return TIME_RESOLUTION_MPC3[idx];
        case PC:
            return TIME_RESOLUTION_PCL[idx]; // lowest PC mode resolution
        case INIT:
        default:
            throw new IllegalStateException("Could not identify time resolution for LAC mode" + mode.toString());
        }
    }
}
