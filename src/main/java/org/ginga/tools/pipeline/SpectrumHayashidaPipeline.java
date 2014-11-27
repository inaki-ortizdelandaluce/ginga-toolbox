package org.ginga.tools.pipeline;

import java.io.File;

import org.ginga.tools.lacdump.LacdumpQuery;

import com.tinkerpop.pipes.util.Pipeline;

public class SpectrumHayashidaPipeline extends Pipeline<LacdumpQuery, File>{


	public SpectrumHayashidaPipeline(LacqrdfitsInputPipe pipe1, LacqrdfitsPipe pipe2){
		super(pipe1,pipe2); 
	}
}
