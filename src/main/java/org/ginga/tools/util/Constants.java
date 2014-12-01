package org.ginga.tools.util;

public class Constants {

    public enum BitRate {
        ANY, H, M, L
    }

    public enum LacMode {
        MPC1, MPC2, MPC3, ACS, PCHK, PC, INIT
    }

    public enum BgSubtractionMethod {
        SIMPLE, SUD_SORT, HAYASHIDA
    }

    public enum LacCounterMode {
        MIDDLE, TOP, BOTH
    }

    public enum TimeResolution {
        ONE, ONE_OVER_TWO, ONE_OVER_FOR
    }

    public static final double DEFAULT_MIN_ELEVATION = 5.0;
    public static final double DEFAULT_MAX_ELEVATION = 180.0;
    public static final double DEFAULT_MIN_RIGIDITY = 10.0;
    public static final double DEFAULT_MAX_RIGIDITY = 20.0;
    public static final BitRate DEFAULT_BIT_RATE = BitRate.H;

}
