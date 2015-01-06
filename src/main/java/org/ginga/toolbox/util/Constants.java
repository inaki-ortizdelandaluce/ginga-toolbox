package org.ginga.toolbox.util;

public class Constants {

    public enum BitRate {
        ANY, H, M, L
    }

    public enum LacMode {
        MPC1, MPC2, MPC3, ACS, PCHK, INIT
    }

    public enum BgSubtractionMethod {
        SIMPLE, SUD_SORT, HAYASHIDA
    }

    public enum LacCounterMode {
        MIDDLE, TOP, BOTH
    }

    public enum TimeSamplingBin {
        ONE, ONE_OVER_TWO, ONE_OVER_FOR
    }

    public static String[] getLacModes() {
        return getEnumValues(LacMode.class);
    }

    public static String[] getBitRates() {
        return getEnumValues(BitRate.class);
    }

    private static String[] getEnumValues(Class<?> enumClass) {
        String[] sArray = new String[enumClass.getEnumConstants().length];
        for (int i = 0; i < sArray.length; i++) {
            sArray[i] = enumClass.getEnumConstants()[i].toString();
        }
        return sArray;
    }
}