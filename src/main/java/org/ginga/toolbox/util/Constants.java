package org.ginga.toolbox.util;

public class Constants {

    public enum LacDirection {
        SKY, NTE, DYE
    }

    public enum GingaAttitude {
        NML("NML"), SL_PLUS("SL+"), SL_MINUS("SL-"), S36("S36"), MAN("MAN"), SAF("SAF"), STB("STB"), LSP(
                "LSP");

        String value;

        GingaAttitude(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum BitRate {
        ANY, H, M, L
    }

    public enum LacMode {
        MPC1, MPC2, MPC3, PC, INIT, ACS, PCHK, MCHK, ASMP, ASMT, LNCH, NSCL
    }

    public enum BgSubtractionMethod {
        SIMPLE, SUD_SORT, HAYASHIDA
    }

    public enum LacCounterMode {
        MIDDLE, TOP, BOTH
    }

    public enum TimingBinWidth {
        ONE_SF, HALF_SF, QUARTER_SF
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