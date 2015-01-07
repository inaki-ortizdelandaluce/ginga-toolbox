package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.bgdspec.BgdspecInputFileWriter;
import org.ginga.toolbox.bgdspec.BgdspecInputModel;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.runtime.GingaToolsRuntime;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class BgdspecRunner extends AbstractPipe<BgdspecInputModel, File> implements
        TransformPipe<BgdspecInputModel, File> {

    private static final Logger log = Logger.getLogger(BgdspecRunner.class);

    /*
     * Receives a Tim2filfitsInputModel, writes it to an input file, executes the lacspec routine
     * and finally emits the resulting spectrum file
     */
    @Override
    protected File processNextStart() throws NoSuchElementException {
        try {
            BgdspecInputModel inputModel = this.starts.next();
            if (inputModel != null) {
                GingaToolboxEnv env = GingaToolboxEnv.getInstance();
                File workingDir = new File(env.getWorkingDir());
                if (!workingDir.exists()) {
                    workingDir.mkdirs();
                }
                log.debug("Working directory " + workingDir.getAbsolutePath());

                // create input file
                File inputFile = new File(workingDir, FileUtil.nextFileName(workingDir, "bgdspec",
                        "input"));
                BgdspecInputFileWriter inputWriter = new BgdspecInputFileWriter(inputModel);
                inputWriter.writeToFile(inputFile);
                log.info("Input file " + inputFile.getPath() + " created successfully");

                // create output file
                File outputFile = new File(workingDir,
                        FileUtil.splitFileBaseAndExtension(inputFile)[0] + ".log");

                // create 'bgspec' command
                String cmd = env.getGingaToolsBinDir() + File.separator + "bgdspec";

                // execute command
                GingaToolsRuntime runtime = new GingaToolsRuntime(workingDir, inputFile,
                        outputFile, cmd);
                log.info("Executing command bgdspec < " + inputFile.getName() + " > "
                        + outputFile.getName());
                int exitValue = runtime.exec();
                log.debug("Exit value " + exitValue);
                if (exitValue == 0) { // return 'bgdspec' output file
                    log.info("Command executed successfully");
                    return new File(workingDir, inputModel.getMonitorFileName());
                } else {
                    log.error("Error executing command " + cmd);
                }
            }
        } catch (IOException e) {
            log.error("Error generating spectrum with bgdspec", e);
        }
        return null;
    }
}
