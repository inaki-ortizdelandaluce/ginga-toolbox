package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputFileWriter;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.runtime.GingaToolsRuntime;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacqrdfitsRunner extends AbstractPipe<LacqrdfitsInputModel, File> implements TransformPipe<LacqrdfitsInputModel, File> {

    private static final Logger log = Logger.getLogger(LacqrdfitsRunner.class);

    private boolean timingBg;

    public LacqrdfitsRunner() {
        this(false);
    }

    public LacqrdfitsRunner(boolean timingBg) {
        this.timingBg = timingBg;
    }

    /*
     * Receives a LacqrdfitsInputModel, writes it to an input file, executes the lacqrdfits routine
     * and finally emits the resulting spectrum file
     */
    @Override
    protected File processNextStart() throws NoSuchElementException {
        try {
            LacqrdfitsInputModel inputModel = this.starts.next();
            if (inputModel != null) {
                GingaToolboxEnv env = GingaToolboxEnv.getInstance();
                File workingDir = new File(env.getWorkingDir());
                if (!workingDir.exists()) {
                    workingDir.mkdirs();
                }
                log.info("Working directory " + workingDir.getAbsolutePath());

                // create input file
                // File inputFile = new File(workingDir,
                // FileUtil.nextFileName(workingDir, "lacqrd",
                // "input"));
                File inputFile = new File(workingDir, FileUtil.nextFileName("lacqrd", inputModel.getStartTime(), inputModel.getLacMode(),
                        "input"));
                LacqrdfitsInputFileWriter lacqrdInputFileWriter = new LacqrdfitsInputFileWriter(inputModel);
                lacqrdInputFileWriter.writeToFile(inputFile);
                log.info("Input file " + inputFile.getPath() + " created successfully");

                // create output file
                File outputFile = new File(workingDir, FileUtil.splitFileBaseAndExtension(inputFile)[0] + ".log");

                // create 'lacqrdfits' command
                String cmd = env.getGingaToolsBinDir() + File.separator + "lacqrdfits";

                // execute command
                GingaToolsRuntime runtime = new GingaToolsRuntime(workingDir, inputFile, outputFile, cmd);
                log.info("Executing command lacqrdfits < " + inputFile.getName() + " > " + outputFile.getName());
                int exitValue = runtime.exec();
                log.debug("Exit value " + exitValue);
                if (exitValue == 0) { // return 'lacqrdfits' output file
                    log.info("Command executed successfully");
                } else {
                    log.error("Error executing command " + cmd);
                }
                // FIX: return file even if execution fails
                if (this.timingBg) {
                    return new File(workingDir, inputModel.getTimingFileName());
                } else {
                    return new File(workingDir, inputModel.getSpectralFileName());
                }
            }
        } catch (IOException e) {
            log.error("Error generating spectrum with lacqrdfits", e);
        }
        return null;
    }
}
