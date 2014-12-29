package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.List;

import org.ginga.toolbox.lacdump.LacdumpConstraints;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.observation.TargetSingleModeObservation;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumHayashidaPipeline extends Pipeline<TargetSingleModeObservation, File> {
	
	private Pipe<TargetSingleModeObservation, TargetSingleModeObservation> modeFilter;
    private Pipe<TargetSingleModeObservation, LacdumpConstraints> constraintsBuilder;
    private Pipe<LacdumpConstraints, LacqrdfitsInputModel> lacqrdfitsInputBuilder;
    private Pipe<LacqrdfitsInputModel, File> lacqrdfits;
    private Pipe<File, File> lac2xspec;
    private Pipeline<TargetSingleModeObservation, File> pipeline;
    
	public SpectrumHayashidaPipeline() {
		 // initialize all pipes needed
	    this.modeFilter = new FilterFunctionPipe<TargetSingleModeObservation>(
	            new SpectrumModeFilterPipe());
	    this.constraintsBuilder = new LacdumpConstraintsPipe();
	    this.lacqrdfitsInputBuilder = new LacqrdfitsInputPipe();
	    this.lacqrdfits = new LacqrdfitsPipe();
	    this.lac2xspec = new Lac2xspecPipe();
	    this.pipeline = new Pipeline<TargetSingleModeObservation, File>(this.modeFilter, 
	    		this.constraintsBuilder, 
	    		this.lacqrdfitsInputBuilder,
	    		this.lacqrdfits,
	    		this.lac2xspec);
	}
	
	public void run(List<TargetSingleModeObservation> modeList) {
		this.pipeline.setStarts(modeList);
	}
	
	public boolean hasNext() {
		return this.pipeline.hasNext();
	}
	
	public File next() {
		return this.pipeline.next();
	}
}
