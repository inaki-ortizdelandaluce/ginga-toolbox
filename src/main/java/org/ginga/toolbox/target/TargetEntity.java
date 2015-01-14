package org.ginga.toolbox.target;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TARGET")
public class TargetEntity {

    @Id
    @Column(name = "TARGET_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "TARGET_NAME", length = 80)
    private String targetName;

    @Column(name = "RA_DEG_B1950")
    private double raDegB1950; // B1950

    @Column(name = "DEC_DEG_B1950")
    private double decDegB1950; // B1950

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
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
     * @return the objectType
     */
    public String getObjectType() {
        return this.objectType;
    }

    /**
     * @param objectType the objectType to set
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

}