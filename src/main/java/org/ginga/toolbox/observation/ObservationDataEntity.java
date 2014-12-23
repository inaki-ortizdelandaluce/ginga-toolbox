package org.ginga.toolbox.observation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "OBSERVATION_DATA")
public class ObservationDataEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OBSERVATION_ID", nullable = false)
	private ObservationEntity observation;
	
	@Column(name = "LACDUMP_FILE", nullable = false, length = 7)
	private String lacdumpFile;

	public ObservationEntity getObservation() {
		return observation;
	}

	public void setObservation(ObservationEntity observation) {
		this.observation = observation;
	}

	public String getLacdumpFile() {
		return lacdumpFile;
	}

	public void setLacdumpFile(String lacdumpFile) {
		this.lacdumpFile = lacdumpFile;
	}
}
