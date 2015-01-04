package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacspec.LacspecInputFileWriter;
import org.ginga.toolbox.lacspec.LacspecInputModel;
import org.ginga.toolbox.runtime.GingaToolsRuntime;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacspecPipe extends AbstractPipe<LacspecInputModel, File>
		implements TransformPipe<LacspecInputModel, File> {

	private static final Logger log = Logger.getLogger(LacspecPipe.class);

	/*
	 * Receives a LacspecInputModel, writes it to an input file, executes the
	 * lacspec routine and finally emits the resulting spectrum file
	 */
	@Override
	protected File processNextStart() throws NoSuchElementException {
		try {
			LacspecInputModel inputModel = this.starts.next();
			if (inputModel != null) {
				GingaToolboxEnv env = GingaToolboxEnv
						.getInstance();
				File workingDir = new File(env.getWorkingDir());
				if (!workingDir.exists()) {
					workingDir.mkdirs();
				}
				log.info("Working directory " + workingDir.getAbsolutePath());

				// create input file
				File inputFile = new File(workingDir, FileUtil.nextFileName(
						"lacspec", inputModel.getStartTime(), inputModel.getLacMode(), "input"));
				LacspecInputFileWriter lacspecInputFileWriter = 
						new LacspecInputFileWriter(inputModel);
				lacspecInputFileWriter.writeToFile(inputFile);
				log.info("Input file " + inputFile.getPath()
						+ " created successfully");

				// create output file
				File outputFile = new File(workingDir,
						FileUtil.splitFileBaseAndExtension(inputFile)[0]
								+ ".log");

				// create 'lacspec' command
				String cmd = env.getGingaToolsBinDir() + "/lacspec";

				// execute command
				GingaToolsRuntime runtime = new GingaToolsRuntime(workingDir,
						inputFile, outputFile, cmd);
				log.info("Executing command " + cmd + " ...");
				int exitValue = runtime.exec();
				log.debug("Exit value " + exitValue);
				if (exitValue == 0) { // return 'lacqrdfits' output file
					log.info("Command executed successfully");
					return new File(workingDir,
							inputModel.getSpectralFileName());
				} else {
					log.error("Error executing command " + cmd);
				}
			}
		} catch (IOException e) {
			log.error("Error generating spectrum with lacspec", e);
		}
		return null;
	}
}
