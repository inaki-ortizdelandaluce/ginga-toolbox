package org.ginga.tools.lacdump;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lacdump")
public class LACDumpEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "sf", nullable = false, length = 12)
    private String superFrame;

    @Column(name = "seq_no", nullable = false)
    private int sequenceNumber;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "br", columnDefinition = "enum('H', 'M', 'L')")
    private String bitRate;

    @Column(name = "mode", columnDefinition = "enum('MPC1', 'MPC2', 'MPC3', 'ACS', 'PCHK')")
    private String mode;

    @Column(name = "gmu", length = 3)
    private String gainAndDiscriminators;

    @Column(name = "acm", columnDefinition = "enum(,'NML', 'SL+','SL-','S36','MAN')")
    private String attitudeStatus;

    @Column(name = "s_e", columnDefinition = "enum('SKY','NTE','DYE')")
    private String direction;

    @Column(name = "lac_l")
    private double lowEnergyCountRate;

    @Column(name = "lac_h")
    private double highEnergyCountRate;

    @Column(name = "suf")
    private double SUDCountRate;

    @Column(name = "pimn")
    private double PIMonitorCountRate;

    @Column(name = "rig")
    private double cutoffRigidity;

    @Column(name = "eelv")
    private double elevation;

    @Column(name = "ra_deg_b1950")
    private double raDegB1950; // B1950

    @Column(name = "dec_deg_b1950")
    private double decDegB1950; // B1950

    @Column(name = "target")
    private String target;

    @Column(name = "transmission")
    private double transmission;

    @Column(name = "spin_axis_ra_deg")
    private double spinAxisRaDeg;

    @Column(name = "spin_axis_dec_deg")
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
}
