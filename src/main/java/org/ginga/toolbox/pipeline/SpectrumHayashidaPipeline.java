package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.List;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.observation.SingleModeTargetObservation;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumHayashidaPipeline extends Pipeline<SingleModeTargetObservation, File> {
	
	private Pipe<SingleModeTargetObservation, SingleModeTargetObservation> modeFilter;
    private Pipe<SingleModeTargetObservation, LacdumpQuery> queryBuilder;
    private Pipe<LacdumpQuery, LacqrdfitsInputModel> lacqrdfitsInputBuilder;
    private Pipe<LacqrdfitsInputModel, File> lacqrdfits;
    private Pipe<File, File> lac2xspec;
    private Pipeline<SingleModeTargetObservation, File> pipeline;
    
	public SpectrumHayashidaPipeline() {
		 // initialize all pipes needed
	    this.modeFilter = new FilterFunctionPipe<SingleModeTargetObservation>(
	            new LacModeFilterPipe());
	    this.queryBuilder = new LacdumpQueryPipe();
	    this.lacqrdfitsInputBuilder = new LacqrdfitsInputPipe();
	    this.lacqrdfits = new LacqrdfitsPipe();
	    this.lac2xspec = new Lac2xspecPipe();
	    this.pipeline = new Pipeline<SingleModeTargetObservation, File>(this.modeFilter, 
	    		this.queryBuilder, 
	    		this.lacqrdfitsInputBuilder,
	    		this.lacqrdfits,
	    		this.lac2xspec);
	}
	
	public void run(List<SingleModeTargetObservation> modeList) {
		this.pipeline.setStarts(modeList);
	}
	
	public boolean hasNext() {
		return this.pipeline.hasNext();
	}
	
	public File next() {
		return this.pipeline.next();
	}
}
