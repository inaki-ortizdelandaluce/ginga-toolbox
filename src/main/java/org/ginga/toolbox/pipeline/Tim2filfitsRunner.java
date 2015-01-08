package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.runtime.GingaToolsRuntime;
import org.ginga.toolbox.tim2filfits.Tim2filfitsInputFileWriter;
import org.ginga.toolbox.tim2filfits.Tim2filfitsInputModel;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class Tim2filfitsRunner extends AbstractPipe<Tim2filfitsInputModel, File> implements
        TransformPipe<Tim2filfitsInputModel, File> {

    private static final Logger log = Logger.getLogger(Tim2filfitsRunner.class);

    /*
     * Receives a Tim2filfitsInputModel, writes it to an input file, executes the lacspec routine
     * and finally emits the resulting spectrum file
     */
    @Override
    protected File processNextStart() throws NoSuchElementException {
        try {
            Tim2filfitsInputModel inputModel = this.starts.next();
            if (inputModel != null) {
                GingaToolboxEnv env = GingaToolboxEnv.getInstance();
                File workingDir = new File(env.getWorkingDir());
                if (!workingDir.exists()) {
                    workingDir.mkdirs();
                }
                log.debug("Working directory " + workingDir.getAbsolutePath());

                // create input file
                File inputFile = new File(workingDir, FileUtil.nextFileName("tim2filfits",
                        inputModel.getStartTime(), LacMode.PC, "input"));

                Tim2filfitsInputFileWriter inputFileWriter = new Tim2filfitsInputFileWriter(
                        inputModel);
                inputFileWriter.writeToFile(inputFile);
                log.info("Input file " + inputFile.getPath() + " created successfully");

                // create output file
                File outputFile = new File(workingDir,
                        FileUtil.splitFileBaseAndExtension(inputFile)[0] + ".log");

                // create 'tim2filfits' command
                String cmd = env.getGingaToolsBinDir() + File.separator + "tim2filfits";

                // execute command
                GingaToolsRuntime runtime = new GingaToolsRuntime(workingDir, inputFile,
                        outputFile, cmd);
                log.info("Executing command tim2filfits < " + inputFile.getName() + " > "
                        + outputFile.getName());
                int exitValue = runtime.exec();
                log.debug("Exit value " + exitValue);
                if (exitValue == 0) { // return 'tim2filfits' output file
                    log.info("Command executed successfully");
                    return new File(workingDir, inputModel.getTimingFileName());
                } else {
                    log.error("Error executing command " + cmd);
                }
            }
        } catch (IOException e) {
            log.error("Error generating spectrum with tim2filfits", e);
        }
        return null;
    }
}
