package org.ginga.toolbox.util;

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

    public enum BackgroundSubtractionMethod {
        SIMPLE, SUD_SORT, HAYASHIDA
    }
}