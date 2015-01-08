package org.ginga.toolbox.lacdump;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LACDUMP")
public class LacdumpSfEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "LACDUMP_FILE", nullable = false, length = 7)
    private String lacdumpFile;

    @Column(name = "SF", nullable = false, length = 12)
    private String superFrame;

    @Column(name = "SEQ_NO", nullable = false)
    private int sequenceNumber;

    @Column(name = "DATE", nullable = false)
    private Date date;

    @Column(name = "BR", columnDefinition = "enum('H', 'M', 'L')")
    private String bitRate;

    @Column(name = "MODE", columnDefinition = "enum('MPC1', 'MPC2', 'MPC3', 'PC', 'ACS', 'PCHK', 'MCHK', 'ASMP', 'ASMT', 'NSCL', 'LNCH')")
    private String mode;

    @Column(name = "GMU", length = 3)
    private String gainAndDiscriminators;

    @Column(name = "ACM", columnDefinition = "enum('NML', 'SL+','SL-','S36','MAN','SAF','STB','LSP')")
    private String attitudeStatus;

    @Column(name = "S_E", columnDefinition = "enum('SKY','NTE','DYE')")
    private String direction;

    @Column(name = "LAC_L")
    private double lowEnergyCountRate;

    @Column(name = "LAC_H")
    private double highEnergyCountRate;

    @Column(name = "SUD")
    private double SUDCountRate;

    @Column(name = "PIMN")
    private double PIMonitorCountRate;

    @Column(name = "RIG")
    private double cutoffRigidity;

    @Column(name = "EELV")
    private double elevation;

    @Column(name = "RA_DEG_B1950")
    private double raDegB1950; // B1950

    @Column(name = "DEC_DEG_B1950")
    private double decDegB1950; // B1950

    @Column(name = "TARGET")
    private String target;

    @Column(name = "TRANSMISSION")
    private double transmission;

    @Column(name = "SPIN_AXIS_RA_DEG")
    private double spinAxisRaDeg;

    @Column(name = "SPIN_AXIS_DEC_DEG")
    private double spinAxisDecDeg;

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
     * @return the direction
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
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

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double getSpinAxisRaDeg() {
        return this.spinAxisRaDeg;
    }

    public void setSpinAxisRaDeg(double spinAxisRaDeg) {
        this.spinAxisRaDeg = spinAxisRaDeg;
    }

    public double getSpinAxisDecDeg() {
        return this.spinAxisDecDeg;
    }

    public void setSpinAxisDecDeg(double spinAxisDecDeg) {
        this.spinAxisDecDeg = spinAxisDecDeg;
    }

    /**
     * @return the id
     */
    public long getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    public String getLacdumpFile() {
        return this.lacdumpFile;
    }

    public void setLacdumpFile(String lacdumpFile) {
        this.lacdumpFile = lacdumpFile;
    }
}
