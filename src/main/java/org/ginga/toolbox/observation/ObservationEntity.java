package org.ginga.toolbox.observation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "OBSERVATION")
public class ObservationEntity {

    @Id
    @Column(name = "OBSERVATION_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "SEQ_NO")
    private int sequenceNumber;

    @Column(name = "NUM_PASSES")
    private int numOfPasses;

    @Column(name = "TARGET_NAME", length = 80)
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

    @Column(name = "FLAG", length = 4)
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
    @Type(type = "text")
    private String passNames;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "observation")
    private Set<ObservationBgEntity> obsLacdumpBgSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "observation")
    private Set<ObservationDataEntity> obsLacdumpDataSet;

    @Transient
    private List<ObservationModeDetails> availableModeDetails;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getNumOfPasses() {
        return this.numOfPasses;
    }

    public void setNumOfPasses(int numOfPasses) {
        this.numOfPasses = numOfPasses;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public float getAveAlpha() {
        return this.aveAlpha;
    }

    public void setAveAlpha(float aveAlpha) {
        this.aveAlpha = aveAlpha;
    }

    public float getAveDelta() {
        return this.aveDelta;
    }

    public void setAveDelta(float aveDelta) {
        this.aveDelta = aveDelta;
    }

    public float getMaxAlpha() {
        return this.maxAlpha;
    }

    public void setMaxAlpha(float maxAlpha) {
        this.maxAlpha = maxAlpha;
    }

    public float getMaxDelta() {
        return this.maxDelta;
    }

    public void setMaxDelta(float maxDelta) {
        this.maxDelta = maxDelta;
    }

    public float getMinAlpha() {
        return this.minAlpha;
    }

    public void setMinAlpha(float minAlpha) {
        this.minAlpha = minAlpha;
    }

    public float getMinDelta() {
        return this.minDelta;
    }

    public void setMinDelta(float minDelta) {
        this.minDelta = minDelta;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public float getPointing() {
        return this.pointing;
    }

    public void setPointing(float pointing) {
        this.pointing = pointing;
    }

    public String getPassNames() {
        return this.passNames;
    }

    public void setPassNames(String passNames) {
        this.passNames = passNames;
    }

    public List<ObservationModeDetails> getAvailableModesDetails() {
        return this.availableModeDetails;
    }

    public void setAvailableModeDetails(List<ObservationModeDetails> availableModeDetails) {
        this.availableModeDetails = availableModeDetails;
    }

    public void addAvailableModeDetails(ObservationModeDetails modeDetails) {
        if (this.availableModeDetails == null) {
            this.availableModeDetails = new ArrayList<ObservationModeDetails>();
        }
        this.availableModeDetails.add(modeDetails);
    }

	public Set<ObservationBgEntity> getObsLacdumpBgSet() {
		return obsLacdumpBgSet;
	}

	public void setObsLacdumpBgSet(Set<ObservationBgEntity> obsLacdumpBgSet) {
		this.obsLacdumpBgSet = obsLacdumpBgSet;
	}

	public Set<ObservationDataEntity> getObsLacdumpDataSet() {
		return obsLacdumpDataSet;
	}

	public void setObsLacdumpDataSet(Set<ObservationDataEntity> obsLacdumpDataSet) {
		this.obsLacdumpDataSet = obsLacdumpDataSet;
	}
}