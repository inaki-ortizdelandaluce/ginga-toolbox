package org.ginga.tools.pipeline;

import java.util.Arrays;
import java.util.List;

import org.ginga.tools.observation.ObservationEntity;

import com.tinkerpop.pipes.Pipe;

public class SpectrumHayashidaPipeline {

	private List<String> targets;
	
	public SpectrumHayashidaPipeline(List<String> targets){
		 this.targets = targets;
	}
	
	public SpectrumHayashidaPipeline(String... targets){
		 this.targets = Arrays.asList(targets);
	}
	
	public void execute() {
		Pipe<String, List<ObservationEntity>> obsScannerPipe = new ObservationScannerPipe();
		obsScannerPipe.setStarts(this.targets);
		// look for Ginga observations available for each target
		while(obsScannerPipe.hasNext()) {
			//List<ObslogEntity> obsList = obslogLookupPipe.next();
			//Arrays.asList(LacMode.MPC1, LacMode.MPC2);
		}
	}
}
