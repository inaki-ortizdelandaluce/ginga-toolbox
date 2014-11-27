package org.ginga.tools.pipeline;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.ginga.tools.spectrum.LacQrdFitsInputFileWriter;
import org.ginga.tools.spectrum.LacQrdFitsInputModel;
import org.ginga.tools.util.FileUtil;

import com.tinkerpop.pipes.PipeFunction;

public class SpectrumHayashidaPipeFunction implements PipeFunction<LacQrdFitsInputModel, File>{

	private static final Logger log = Logger.getLogger(SpectrumHayashidaPipeFunction.class);
	
	@Override
	public File compute(LacQrdFitsInputModel inputModel) {
		try {
		
			// create input file name
		    File workingDir = new File(System.getProperty("user.dir"));
		    log.info("Working directory " + workingDir.getAbsolutePath());
		    String fileName = FileUtil.nextFileName(workingDir, "lacqrd", "input");
		    File lacQrdInputFile = new File(fileName);
		    log.info("Input file to be created " + lacQrdInputFile.getPath());
		
			// write input file from model 
			LacQrdFitsInputFileWriter lacQrdInputFileWriter = new LacQrdFitsInputFileWriter(inputModel);
			lacQrdInputFileWriter.writeToFile(lacQrdInputFile);
			log.info("Input file " + lacQrdInputFile.getPath() + " created successfully");
			
			// execute 'lacqrdfits' routine
			String logFile = FileUtil.splitFileBaseAndExtension(lacQrdInputFile)[0] + ".log";
			String cmd = "lacqrdfits < " + lacQrdInputFile.getName() + " > " + logFile;
			
			log.info("Executing command " + cmd + " ...");
			// TODO PATH, Env Variables, Input/output using stdin, stdout, sterr frmm process
			Process p = Runtime.getRuntime().exec(cmd, new String[]{"PATH=$PATH"}, null);
			if(p.exitValue() == 0) {
				// return 'lacqrdfits' output file 
				return new File(workingDir, inputModel.getSpectralFileName());			
			} else {
				log.error("Error executing command " + cmd);
			}
			
		} catch (IOException e) {
			log.error("",e);
		}
		return null;
	}
}
