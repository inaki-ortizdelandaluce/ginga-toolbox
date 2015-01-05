package org.ginga.toolbox.pipeline;

import java.io.File;
import java.util.List;

import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacspec.LacspecInputModel;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumBgPipeline extends Pipeline<SingleModeTargetObservation, File> {
	
	private Pipe<SingleModeTargetObservation, SingleModeTargetObservation> modeFilter;
    private Pipe<SingleModeTargetObservation, LacdumpQuery> lacdumpQueryBuilder;
    private Pipe<LacdumpQuery, LacspecInputModel> lacspecInputBuilder;
    private Pipe<LacspecInputModel, File> lacspec;
    private Pipeline<SingleModeTargetObservation, File> pipeline;
    
	public SpectrumBgPipeline() {
		 // initialize all pipes needed
		this.modeFilter = new FilterFunctionPipe<SingleModeTargetObservation>(
		    new LacModeFilterPipe());
		this.lacdumpQueryBuilder = new LacdumpQueryBgPipe();
	    this.lacspecInputBuilder = new LacspecInputPipe() {
			@Override
			public boolean isBackground() {
				return true;
			}
			@Override
			public BgSubtractionMethod getBgSubtractionMethod() {
				return null;
			}
			@Override
			public String getBgFileName() {
				return null;
			}
		};
		this.lacspec = new LacspecPipe();
	    this.pipeline = new Pipeline<SingleModeTargetObservation, File>(
	    		this.modeFilter,
	    		this.lacdumpQueryBuilder, 
	    		this.lacspecInputBuilder,
	    		this.lacspec);
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
