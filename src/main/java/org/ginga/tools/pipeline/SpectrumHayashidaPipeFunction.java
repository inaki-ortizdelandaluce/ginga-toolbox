package org.ginga.tools.pipeline;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.ginga.tools.runtime.GingaToolsEnvironment;
import org.ginga.tools.runtime.GingaToolsRuntime;
import org.ginga.tools.spectrum.LacQrdFitsInputFileWriter;
import org.ginga.tools.spectrum.LacQrdFitsInputModel;
import org.ginga.tools.util.FileUtil;

import com.tinkerpop.pipes.PipeFunction;

public class SpectrumHayashidaPipeFunction implements PipeFunction<LacQrdFitsInputModel, File> {

    private static final Logger log = Logger.getLogger(SpectrumHayashidaPipeFunction.class);

    @Override
    public File compute(LacQrdFitsInputModel inputModel) {
        try {
            GingaToolsEnvironment gingaEnv = GingaToolsEnvironment.getInstance();
            File workingDir = new File(gingaEnv.getGingaWrkDir());
            log.info("Working directory " + workingDir.getAbsolutePath());

            // create input file
            File inputFile = new File(workingDir, FileUtil.nextFileName(workingDir, "lacqrd",
                    "input"));
            LacQrdFitsInputFileWriter lacQrdInputFileWriter = new LacQrdFitsInputFileWriter(
                    inputModel);
            lacQrdInputFileWriter.writeToFile(inputFile);
            log.info("Input file " + inputFile.getPath() + " created successfully");

            // create output file
            File outputFile = new File(workingDir, FileUtil.splitFileBaseAndExtension(inputFile)[0]
                    + ".log");

            // create 'lacqrdfits' command
            String cmd = gingaEnv.getGingaBinDir() + "/lacqrdfits";

            // execute command
            GingaToolsRuntime runtime = new GingaToolsRuntime(workingDir, cmd, inputFile,
                    outputFile);
            log.info("Executing command " + cmd + " ...");
            int exitValue = runtime.exec();
            log.info("Exit value " + exitValue);
            if (exitValue == 0) { // return 'lacqrdfits' output file
                log.info("Command " + cmd + " executed successfully");
                return new File(workingDir, inputModel.getSpectralFileName());
            } else {
                log.error("Error executing command " + cmd);
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }
}
