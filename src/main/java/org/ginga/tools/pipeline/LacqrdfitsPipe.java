package org.ginga.tools.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.tools.runtime.GingaToolsEnvironment;
import org.ginga.tools.runtime.GingaToolsRuntime;
import org.ginga.tools.spectrum.LacqrdfitsInputFileWriter;
import org.ginga.tools.spectrum.LacqrdfitsInputModel;
import org.ginga.tools.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacqrdfitsPipe extends AbstractPipe<LacqrdfitsInputModel, File>
		implements TransformPipe<LacqrdfitsInputModel, File> {

	private static final Logger log = Logger.getLogger(LacqrdfitsPipe.class);

	/*
	 * Receives a LacqrdfitsInputModel, writes it to an input file, executes the
	 * lacqrdfits routine and finally emits the resulting spectrum file
	 */
	@Override
	protected File processNextStart() throws NoSuchElementException {
		try {
			if (this.starts.hasNext()) {
				LacqrdfitsInputModel inputModel = this.starts.next();
				GingaToolsEnvironment gingaEnv = GingaToolsEnvironment
						.getInstance();
				File workingDir = new File(gingaEnv.getGingaWrkDir());
				log.info("Working directory " + workingDir.getAbsolutePath());

				// create input file
				File inputFile = new File(workingDir, FileUtil.nextFileName(
						workingDir, "lacqrd", "input"));
				LacqrdfitsInputFileWriter lacQrdInputFileWriter = new LacqrdfitsInputFileWriter(
						inputModel);
				lacQrdInputFileWriter.writeToFile(inputFile);
				log.info("Input file " + inputFile.getPath()
						+ " created successfully");

				// create output file
				File outputFile = new File(workingDir,
						FileUtil.splitFileBaseAndExtension(inputFile)[0]
								+ ".log");

				// create 'lacqrdfits' command
				String cmd = gingaEnv.getGingaBinDir() + "/lacqrdfits";

				// execute command
				GingaToolsRuntime runtime = new GingaToolsRuntime(workingDir,
						inputFile, outputFile, cmd);
				log.info("Executing command " + cmd + " ...");
				int exitValue = runtime.exec();
				log.info("Exit value " + exitValue);
				if (exitValue == 0) { // return 'lacqrdfits' output file
					log.info("Command executed successfully");
					return new File(workingDir,
							inputModel.getSpectralFileName());
				} else {
					log.error("Error executing command " + cmd);
				}
			} else {
				throw new NoSuchElementException();
			}
		} catch (IOException e) {
			log.error("Error generating spectrum with lacqrdfits", e);
		}
		return null;
	}
}
