package org.ginga.toolbox.environment;

import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.ginga.toolbox.util.Constants;
import org.ginga.toolbox.util.Constants.BitRate;

public class InteractiveInputParameters implements InputParameters {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(InteractiveInputParameters.class);

    private Scanner scanner;

    private Double elevationMin;
    private Double elevationMax;
    private Double cutOffRigidityMin;
    private Double cutOffRigidityMax;
    private BitRate bitRate;
    private Double transmissionMin;
    private Integer attitudeMode;
    private Boolean deadTimeCorrection;
    private Boolean delayTimeCorrection;
    private Boolean channelToEnergyConversion;
    private Integer bgSubFileNumber;
    private Integer lacCounter1;
    private Integer lacCounter2;
    private Integer lacCounter3;
    private Integer lacCounter4;
    private Integer lacCounter5;
    private Integer lacCounter6;
    private Integer lacCounter7;
    private Integer lacCounter8;
    private Boolean isLacMixedMode;
    private Double skyAnnulusInnerRadii;
    private Double skyAnnulusOuterRadii;
    private String phselLine1;
    private String phselLine2;
    private String phselLine3;
    private String phselLine4;
    private String phselLine5;
    private String phselLine6;
    private String phselLine7;
    private String phselLine8;
    private String phselLine9;
    private String phselLine10;

    protected InteractiveInputParameters() {
        this.scanner = new Scanner(System.in);
    }

    private String readEnumFromInput(String message, String[] values) {
        String s = null;
        boolean catcher = false;
        do {
            try {
                System.out.print(message);
                s = this.scanner.next();
                if (!Arrays.asList(values).contains(s)) {
                    throw new IllegalArgumentException();
                }
                catcher = true;
            } catch (Exception e) {
                System.out.println("Value is not valid, please try again");
            } finally {
                this.scanner.nextLine();
            }
        } while (!catcher);
        return s;
    }

    private Double readDoubleFromInput(String message) {
        Double d = null;
        boolean catcher = false;
        do {
            try {
                System.out.print(message);
                d = this.scanner.nextDouble();
                catcher = true;
            } catch (Exception e) {
                System.out.println("Value is not a number, please try again");
            } finally {
                this.scanner.nextLine();
            }
        } while (!catcher);
        return d;
    }

    private Integer readIntegerFromInput(String message) {
        Integer i = null;
        boolean catcher = false;
        do {
            try {
                System.out.print(message);
                i = this.scanner.nextInt();
                catcher = true;
            } catch (Exception e) {
                System.out.println("Value is not a number, please try again");
            } finally {
                this.scanner.nextLine();
            }
        } while (!catcher);
        return i;
    }

    private Boolean readBooleanFromInput(String message) {
        Boolean b = null;
        boolean catcher = false;
        do {
            try {
                System.out.print(message);
                b = this.scanner.nextBoolean();
                catcher = true;
            } catch (Exception e) {
                System.out.println("Value is not a number, please try again");
            } finally {
                this.scanner.nextLine();
            }
        } while (!catcher);
        return b;
    }

    private String readStringFromInput(String message) {
        String s = null;
        boolean catcher = false;
        do {
            try {
                System.out.print(message);
                s = this.scanner.next();
                catcher = true;
            } catch (Exception e) {
                System.out.println("Please try again");
            } finally {
                this.scanner.nextLine();
            }
        } while (!catcher);
        return s;
    }

    @Override
    public Double getElevationMin() {
        if (this.elevationMin == null) {
            this.elevationMin = readDoubleFromInput("Enter a minimum Pointing Elevation Angle in degrees:");
        }
        return this.elevationMin;
    }

    /**
     * @param elevationMin the elevationMin to set
     */
    public void setElevationMin(Double elevationMin) {
        this.elevationMin = elevationMin;
    }

    /**
     * @return the elevationMax
     */
    @Override
    public Double getElevationMax() {
        if (this.elevationMax == null) {
            this.elevationMax = readDoubleFromInput("Enter a maximum Pointing Elevation Angle in degrees:");
        }
        return this.elevationMax;
    }

    /**
     * @param elevationMax the elevationMax to set
     */
    public void setElevationMax(Double elevationMax) {
        this.elevationMax = elevationMax;
    }

    /**
     * @return the cutOffRigidityMin
     */
    @Override
    public Double getCutOffRigidityMin() {
        if (this.cutOffRigidityMin == null) {
            this.cutOffRigidityMin = readDoubleFromInput("Enter a minimum Cutt-off Rigidity value in GeV/c:");
        }
        return this.cutOffRigidityMin;
    }

    /**
     * @param cutOffRigidityMin the cutOffRigidityMin to set
     */
    public void setCutOffRigidityMin(Double cutOffRigidityMin) {
        this.cutOffRigidityMin = cutOffRigidityMin;
    }

    /**
     * @return the cutOffRigidityMax
     */
    @Override
    public Double getCutOffRigidityMax() {
        if (this.cutOffRigidityMax == null) {
            this.cutOffRigidityMax = readDoubleFromInput("Enter a maximum Cutt-off Rigidity value in GeV/c:");
        }
        return this.cutOffRigidityMax;
    }

    /**
     * @param cutOffRigidityMax the cutOffRigidityMax to set
     */
    public void setCutOffRigidityMax(Double cutOffRigidityMax) {
        this.cutOffRigidityMax = cutOffRigidityMax;
    }

    /**
     * @return the bitRate
     */
    @Override
    public BitRate getBitRate() {
        if (this.bitRate == null) {
            this.bitRate = BitRate.valueOf(readEnumFromInput(
                    "Enter a Bit Rate ('ANY', 'H', 'M' and 'L'):", Constants.getBitRates()));
        }
        return this.bitRate;
    }

    /**
     * @param bitRate the bitRate to set
     */
    public void setBitRate(BitRate bitRate) {
        this.bitRate = bitRate;
    }

    /**
     * @return the transmissionMin
     */
    @Override
    public Double getTransmissionMin() {
        if (this.transmissionMin == null) {
            this.transmissionMin = readDoubleFromInput("Enter a minimum Transmission value for the target source:");
        }
        return this.transmissionMin;
    }

    /**
     * @param transmissionMin the transmissionMin to set
     */
    public void setTransmissionMin(Double transmissionMin) {
        this.transmissionMin = transmissionMin;
    }

    /**
     * @return the attitudeMode
     */
    @Override
    public Integer getAttitudeMode() {
        if (this.attitudeMode == null) {
            this.attitudeMode = readIntegerFromInput("Enter an Attitude Mode (1=Pointing Mode, 0=Any Mode):");
        }
        return this.attitudeMode;
    }

    /**
     * @param attitudeMode the attitudeMode to set
     */
    public void setAtitudeMode(Integer attitudeMode) {
        this.attitudeMode = attitudeMode;
    }

    /**
     * @return the deadTimeCorrection
     */
    @Override
    public Boolean getDeadTimeCorrection() {
        if (this.deadTimeCorrection == null) {
            this.deadTimeCorrection = readBooleanFromInput("Apply Deadtime Correction (true, false):");
        }
        return this.deadTimeCorrection;
    }

    /**
     * @param deadTimeCorrection the deadTimeCorrection to set
     */
    public void setDeadTimeCorrection(Boolean deadTimeCorrection) {
        this.deadTimeCorrection = deadTimeCorrection;
    }

    /**
     * @return the channelToEnergyConversion
     */
    @Override
    public Boolean getChannelToEnergyConversion() {
        if (this.channelToEnergyConversion == null) {
            this.channelToEnergyConversion = readBooleanFromInput("Apply Channel to Energy conversion (true, false):");
        }
        return this.channelToEnergyConversion;
    }

    /**
     * @param channelToEnergyConversion the channelToEnergyConversion to set
     */
    public void setChannelToEnergyConversion(Boolean channelToEnergyConversion) {
        this.channelToEnergyConversion = channelToEnergyConversion;
    }

    /**
     * @return the bgSubFileNumber
     */
    @Override
    public Integer getBgSubFileNumber() {
        if (this.bgSubFileNumber == null) {
            this.bgSubFileNumber = readIntegerFromInput("Enter Background Sub-file Number:");
        }
        return this.bgSubFileNumber;
    }

    /**
     * @param bgSubFileNumber the bgSubFileNumber to set
     */
    public void setBgSubFileNumber(Integer bgSubFileNumber) {
        this.bgSubFileNumber = bgSubFileNumber;
    }

    /**
     * @return the lacCounter1
     */
    @Override
    public Integer getLacCounter1() {
        if (this.lacCounter1 == null) {
            this.lacCounter1 = readIntegerFromInput("Enter LAC Counter 1 Mode  (1=middle-layer, 2=top-layer, 3=both):");
        }
        return this.lacCounter1;
    }

    /**
     * @param lacCounter1 the lacCounter1 to set
     */
    public void setLacCounter1(Integer lacCounter1) {
        this.lacCounter1 = lacCounter1;
    }

    /**
     * @return the lacCounter2
     */
    @Override
    public Integer getLacCounter2() {
        if (this.lacCounter2 == null) {
            this.lacCounter2 = readIntegerFromInput("Enter LAC Counter 2 Mode  (1=middle-layer, 2=top-layer, 3=both):");
        }
        return this.lacCounter2;
    }

    /**
     * @param lacCounter2 the lacCounter2 to set
     */
    public void setLacCounter2(Integer lacCounter2) {
        this.lacCounter2 = lacCounter2;
    }

    /**
     * @return the lacCounter3
     */
    @Override
    public Integer getLacCounter3() {
        if (this.lacCounter3 == null) {
            this.lacCounter3 = readIntegerFromInput("Enter LAC Counter 3 Mode  (1=middle-layer, 2=top-layer, 3=both):");
        }
        return this.lacCounter3;
    }

    /**
     * @param lacCounter3 the lacCounter3 to set
     */
    public void setLacCounter3(Integer lacCounter3) {
        this.lacCounter3 = lacCounter3;
    }

    /**
     * @return the lacCounter4
     */
    @Override
    public Integer getLacCounter4() {
        if (this.lacCounter4 == null) {
            this.lacCounter4 = readIntegerFromInput("Enter LAC Counter 4 Mode  (1=middle-layer, 2=top-layer, 3=both):");
        }
        return this.lacCounter4;
    }

    /**
     * @param lacCounter4 the lacCounter4 to set
     */
    public void setLacCounter4(Integer lacCounter4) {
        this.lacCounter4 = lacCounter4;
    }

    /**
     * @return the lacCounter5
     */
    @Override
    public Integer getLacCounter5() {
        if (this.lacCounter5 == null) {
            this.lacCounter5 = readIntegerFromInput("Enter LAC Counter 5 Mode  (1=middle-layer, 2=top-layer, 3=both):");
        }
        return this.lacCounter5;
    }

    /**
     * @param lacCounter5 the lacCounter5 to set
     */
    public void setLacCounter5(Integer lacCounter5) {
        this.lacCounter5 = lacCounter5;
    }

    /**
     * @return the lacCounter6
     */
    @Override
    public Integer getLacCounter6() {
        if (this.lacCounter6 == null) {
            this.lacCounter6 = readIntegerFromInput("Enter LAC Counter 6 Mode  (1=middle-layer, 2=top-layer, 3=both):");
        }
        return this.lacCounter6;
    }

    /**
     * @param lacCounter6 the lacCounter6 to set
     */
    public void setLacCounter6(Integer lacCounter6) {
        this.lacCounter6 = lacCounter6;
    }

    /**
     * @return the lacCounter7
     */
    @Override
    public Integer getLacCounter7() {
        if (this.lacCounter7 == null) {
            this.lacCounter7 = readIntegerFromInput("Enter LAC Counter 7 Mode  (1=middle-layer, 2=top-layer, 3=both):");
        }
        return this.lacCounter7;
    }

    /**
     * @param lacCounter7 the lacCounter7 to set
     */
    public void setLacCounter7(Integer lacCounter7) {
        this.lacCounter7 = lacCounter7;
    }

    /**
     * @return the lacCounter8
     */
    @Override
    public Integer getLacCounter8() {
        if (this.lacCounter8 == null) {
            this.lacCounter8 = readIntegerFromInput("Enter LAC Counter 8 Mode  (1=middle-layer, 2=top-layer, 3=both):");
        }
        return this.lacCounter8;
    }

    /**
     * @param lacCounter8 the lacCounter8 to set
     */
    public void setLacCounter8(Integer lacCounter8) {
        this.lacCounter8 = lacCounter8;
    }

    /**
     * @return the isLacMixedMode
     */
    @Override
    public Boolean isLacMixedMode() {
        if (this.isLacMixedMode == null) {
            this.isLacMixedMode = readBooleanFromInput("Combine MPC-1 and MPC-2 mode data (true, false):");
        }
        return this.isLacMixedMode;
    }

    /**
     * @return the skyAnnulusInnerRadii
     */
    @Override
    public Double getSkyAnnulusInnerRadii() {
        if (this.skyAnnulusInnerRadii == null) {
            this.skyAnnulusInnerRadii = readDoubleFromInput("Enter Sky annulus inner radii in degrees:");
        }
        return this.skyAnnulusInnerRadii;
    }

    /**
     * @param skyAnnulusInnerRadii the skyAnnulusInnerRadii to set
     */
    public void setSkyAnnulusInnerRadii(Double skyAnnulusInnerRadii) {
        this.skyAnnulusInnerRadii = skyAnnulusInnerRadii;
    }

    /**
     * @return the skyAnnulusOuterRadii
     */
    @Override
    public Double getSkyAnnulusOuterRadii() {
        if (this.skyAnnulusOuterRadii == null) {
            this.skyAnnulusOuterRadii = readDoubleFromInput("Enter Sky annulus outer radii in degrees:");
        }
        return this.skyAnnulusOuterRadii;
    }

    /**
     * @param skyAnnulusOuterRadii the skyAnnulusOuterRadii to set
     */
    public void setSkyAnnulusOuterRadii(Double skyAnnulusOuterRadii) {
        this.skyAnnulusOuterRadii = skyAnnulusOuterRadii;
    }

    /**
     * @return the delayTimeCorrection
     */
    @Override
    public Boolean getDelayTimeCorrection() {
        if (this.delayTimeCorrection == null) {
            this.delayTimeCorrection = readBooleanFromInput("Apply Delay Time Correction (true, false):");
        }
        return this.delayTimeCorrection;
    }

    /**
     * @return the phselLine1
     */
    @Override
    public String getPhselLine1() {
        if (this.phselLine1 == null) {
            this.phselLine1 = readStringFromInput("Enter energy channel selection for line 1:");
        }
        return this.phselLine1;
    }

    /**
     * @param phselLine1 the phselLine1 to set
     */
    public void setPhselLine1(String phselLine1) {
        this.phselLine1 = phselLine1;
    }

    /**
     * @return the phselLine2
     */
    @Override
    public String getPhselLine2() {
        if (this.phselLine2 == null) {
            this.phselLine2 = readStringFromInput("Enter energy channel selection for line 2:");
        }
        return this.phselLine2;
    }

    /**
     * @param phselLine2 the phselLine2 to set
     */
    public void setPhselLine2(String phselLine2) {
        this.phselLine2 = phselLine2;
    }

    /**
     * @return the phselLine3
     */
    @Override
    public String getPhselLine3() {
        if (this.phselLine3 == null) {
            this.phselLine3 = readStringFromInput("Enter energy channel selection for line 3:");
        }
        return this.phselLine3;
    }

    /**
     * @param phselLine3 the phselLine3 to set
     */
    public void setPhselLine3(String phselLine3) {
        this.phselLine3 = phselLine3;
    }

    /**
     * @return the phselLine4
     */
    @Override
    public String getPhselLine4() {
        if (this.phselLine4 == null) {
            this.phselLine4 = readStringFromInput("Enter energy channel selection for line 4:");
        }
        return this.phselLine4;
    }

    /**
     * @param phselLine4 the phselLine4 to set
     */
    public void setPhselLine4(String phselLine4) {
        this.phselLine4 = phselLine4;
    }

    /**
     * @return the phselLine5
     */
    @Override
    public String getPhselLine5() {
        if (this.phselLine5 == null) {
            this.phselLine5 = readStringFromInput("Enter energy channel selection for line 5:");
        }
        return this.phselLine5;
    }

    /**
     * @param phselLine5 the phselLine5 to set
     */
    public void setPhselLine5(String phselLine5) {
        this.phselLine5 = phselLine5;
    }

    /**
     * @return the phselLine6
     */
    @Override
    public String getPhselLine6() {
        if (this.phselLine6 == null) {
            this.phselLine6 = readStringFromInput("Enter energy channel selection for line 6:");
        }
        return this.phselLine6;
    }

    /**
     * @param phselLine6 the phselLine6 to set
     */
    public void setPhselLine6(String phselLine6) {
        this.phselLine6 = phselLine6;
    }

    /**
     * @return the phselLine7
     */
    @Override
    public String getPhselLine7() {
        if (this.phselLine7 == null) {
            this.phselLine7 = readStringFromInput("Enter energy channel selection for line 7:");
        }
        return this.phselLine7;
    }

    /**
     * @param phselLine7 the phselLine7 to set
     */
    public void setPhselLine7(String phselLine7) {
        this.phselLine7 = phselLine7;
    }

    /**
     * @return the phselLine8
     */
    @Override
    public String getPhselLine8() {
        if (this.phselLine8 == null) {
            this.phselLine8 = readStringFromInput("Enter energy channel selection for line 8:");
        }
        return this.phselLine8;
    }

    /**
     * @param phselLine8 the phselLine8 to set
     */
    public void setPhselLine8(String phselLine8) {
        this.phselLine8 = phselLine8;
    }

    /**
     * @return the phselLine9
     */
    @Override
    public String getPhselLine9() {
        if (this.phselLine9 == null) {
            this.phselLine9 = readStringFromInput("Enter energy channel selection for line 9:");
        }
        return this.phselLine9;
    }

    /**
     * @param phselLine9 the phselLine9 to set
     */
    public void setPhselLine9(String phselLine9) {
        this.phselLine9 = phselLine9;
    }

    /**
     * @return the phselLine10
     */
    @Override
    public String getPhselLine10() {
        if (this.phselLine10 == null) {
            this.phselLine10 = readStringFromInput("Enter energy channel selection for line 10:");
        }
        return this.phselLine10;
    }

    /**
     * @param phselLine10 the phselLine10 to set
     */
    public void setPhselLine10(String phselLine10) {
        this.phselLine10 = phselLine10;
    }

    /**
     * @param delayTimeCorrection the delayTimeCorrection to set
     */
    public void setDelayTimeCorrection(Boolean delayTimeCorrection) {
        this.delayTimeCorrection = delayTimeCorrection;
    }

    /**
     * @param isLacMixedMode the isLacMixedMode to set
     */
    public void setLacMixedMode(Boolean isLacMixedMode) {
        this.isLacMixedMode = isLacMixedMode;
    }

}
