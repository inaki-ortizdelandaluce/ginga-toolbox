package org.ginga.tools.lacdump;

import java.util.Date;

public class LACDumpRow {

    private String superFrame;
    private int sequenceNumber;
    private Date date;
    private String bitRate; // H, M, L
    private String mode; // MPC1, MPC2, MPC3, ACS, PCHK
    private String gainAndDiscriminators;
    private String attitudeStatus; // NML, SL+, SL-, S36, MAN
    private String LACDirection; // SKY, NTE, DYE
    private double lowEnergyCountRate;
    private double highEnergyCountRate;
    private double SUDCountRate;
    private double PIMonitorCountRate;
    private double cutoffRigidity;
    private double elevation;
    private double raDegB1950; // B1950
    private double decDegB1950; // B1950
    private double transmission;
    private double spinAxis;

    /**
     * @return the superFrame
     */
    public String getSuperFrame() {
        return this.superFrame;
    }

    /**
     * @param superFrame the superFrame to set
     */
    public void setSuperFrame(String superFrame) {
        this.superFrame = superFrame;
    }

    /**
     * @return the sequenceNumber
     */
    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the bitRate
     */
    public String getBitRate() {
        return this.bitRate;
    }

    /**
     * @param bitRate the bitRate to set
     */
    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return this.mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the gainAndDiscriminators
     */
    public String getGainAndDiscriminators() {
        return this.gainAndDiscriminators;
    }

    /**
     * @param gainAndDiscriminators the gainAndDiscriminators to set
     */
    public void setGainAndDiscriminators(String gainAndDiscriminators) {
        this.gainAndDiscriminators = gainAndDiscriminators;
    }

    /**
     * @return the attitudeStatus
     */
    public String getAttitudeStatus() {
        return this.attitudeStatus;
    }

    /**
     * @param attitudeStatus the attitudeStatus to set
     */
    public void setAttitudeStatus(String attitudeStatus) {
        this.attitudeStatus = attitudeStatus;
    }

    /**
     * @return the lACDirection
     */
    public String getLACDirection() {
        return this.LACDirection;
    }

    /**
     * @param lACDirection the lACDirection to set
     */
    public void setLACDirection(String lACDirection) {
        this.LACDirection = lACDirection;
    }

    /**
     * @return the lowEnergyCountRate
     */
    public double getLowEnergyCountRate() {
        return this.lowEnergyCountRate;
    }

    /**
     * @param lowEnergyCountRate the lowEnergyCountRate to set
     */
    public void setLowEnergyCountRate(double lowEnergyCountRate) {
        this.lowEnergyCountRate = lowEnergyCountRate;
    }

    /**
     * @return the highEnergyCountRate
     */
    public double getHighEnergyCountRate() {
        return this.highEnergyCountRate;
    }

    /**
     * @param highEnergyCountRate the highEnergyCountRate to set
     */
    public void setHighEnergyCountRate(double highEnergyCountRate) {
        this.highEnergyCountRate = highEnergyCountRate;
    }

    /**
     * @return the sUDCountRate
     */
    public double getSUDCountRate() {
        return this.SUDCountRate;
    }

    /**
     * @param sUDCountRate the sUDCountRate to set
     */
    public void setSUDCountRate(double sUDCountRate) {
        this.SUDCountRate = sUDCountRate;
    }

    /**
     * @return the pIMonitorCountRate
     */
    public double getPIMonitorCountRate() {
        return this.PIMonitorCountRate;
    }

    /**
     * @param pIMonitorCountRate the pIMonitorCountRate to set
     */
    public void setPIMonitorCountRate(double pIMonitorCountRate) {
        this.PIMonitorCountRate = pIMonitorCountRate;
    }

    /**
     * @return the cutoffRigidity
     */
    public double getCutoffRigidity() {
        return this.cutoffRigidity;
    }

    /**
     * @param cutoffRigidity the cutoffRigidity to set
     */
    public void setCutoffRigidity(double cutoffRigidity) {
        this.cutoffRigidity = cutoffRigidity;
    }

    /**
     * @return the elevation
     */
    public double getElevation() {
        return this.elevation;
    }

    /**
     * @param elevation the elevation to set
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    /**
     * @return the raDegB1950
     */
    public double getRaDegB1950() {
        return this.raDegB1950;
    }

    /**
     * @param raDegB1950 the raDegB1950 to set
     */
    public void setRaDegB1950(double raDegB1950) {
        this.raDegB1950 = raDegB1950;
    }

    /**
     * @return the decDegB1950
     */
    public double getDecDegB1950() {
        return this.decDegB1950;
    }

    /**
     * @param decDegB1950 the decDegB1950 to set
     */
    public void setDecDegB1950(double decDegB1950) {
        this.decDegB1950 = decDegB1950;
    }

    /**
     * @return the transmission
     */
    public double getTransmission() {
        return this.transmission;
    }

    /**
     * @param transmission the transmission to set
     */
    public void setTransmission(double transmission) {
        this.transmission = transmission;
    }

    /**
     * @return the spinAxis
     */
    public double getSpinAxis() {
        return this.spinAxis;
    }

    /**
     * @param spinAxis the spinAxis to set
     */
    public void setSpinAxis(double spinAxis) {
        this.spinAxis = spinAxis;
    }
}
