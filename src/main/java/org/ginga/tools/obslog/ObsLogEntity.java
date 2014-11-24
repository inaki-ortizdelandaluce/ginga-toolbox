package org.ginga.tools.obslog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "OBSLOG")
public class ObsLogEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "SEQ_NO")
    private int sequenceNumber;

    @Column(name = "NUM_PASSES")
    private int numOfPasses;
    
    @Column(name = "TARGET_NAME", length=80)
    private String targetName;
    
    @Column(name = "AVE_ALPHA")
    private float aveAlpha;
    
    @Column(name = "AVE_DELTA")
    private float aveDelta;

    @Column(name = "MAX_ALPHA")
    private float maxAlpha;
    
    @Column(name = "MAX_DELTA")
    private float maxDelta;

    @Column(name = "MIN_ALPHA")
    private float minAlpha;
    
    @Column(name = "MIN_DELTA")
    private float minDelta;

    @Column(name = "FLAG", length=4)
    private String flag;
    
    @Column(name = "DISTANCE")
    private float distance;

    @Column(name = "STARTTIME")
    private Date startTime;

    @Column(name = "ENDTIME")
    private Date endTime;

    @Column(name = "POINTING")
    private float pointing;

    @Column(name = "PASS_NAMES")
    @Type(type="text")
    private String passNames;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getNumOfPasses() {
		return numOfPasses;
	}

	public void setNumOfPasses(int numOfPasses) {
		this.numOfPasses = numOfPasses;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public float getAveAlpha() {
		return aveAlpha;
	}

	public void setAveAlpha(float aveAlpha) {
		this.aveAlpha = aveAlpha;
	}

	public float getAveDelta() {
		return aveDelta;
	}

	public void setAveDelta(float aveDelta) {
		this.aveDelta = aveDelta;
	}

	public float getMaxAlpha() {
		return maxAlpha;
	}

	public void setMaxAlpha(float maxAlpha) {
		this.maxAlpha = maxAlpha;
	}

	public float getMaxDelta() {
		return maxDelta;
	}

	public void setMaxDelta(float maxDelta) {
		this.maxDelta = maxDelta;
	}

	public float getMinAlpha() {
		return minAlpha;
	}

	public void setMinAlpha(float minAlpha) {
		this.minAlpha = minAlpha;
	}

	public float getMinDelta() {
		return minDelta;
	}

	public void setMinDelta(float minDelta) {
		this.minDelta = minDelta;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public float getPointing() {
		return pointing;
	}

	public void setPointing(float pointing) {
		this.pointing = pointing;
	}

	public String getPassNames() {
		return passNames;
	}

	public void setPassNames(String passNames) {
		this.passNames = passNames;
	}

}
