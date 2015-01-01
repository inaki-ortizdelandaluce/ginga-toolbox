/**
 *
 */
package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnvironment;
import org.ginga.toolbox.runtime.GingaToolsRuntime;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class Lac2xspecPipe extends AbstractPipe<File, File> implements TransformPipe<File, File> {

    private static final Logger log = Logger.getLogger(Lac2xspecPipe.class);

    /*
     * Receives a ASCII spectrum file and emits the converted PHA file for further processing with
     * XSPEC
     */
    @Override
    protected File processNextStart() throws NoSuchElementException {
        try {
            File specAsciiFile = this.starts.next();
            if (specAsciiFile != null && specAsciiFile.exists()) {
                GingaToolboxEnvironment env = GingaToolboxEnvironment.getInstance();
                File workingDir = new File(env.getWorkingDir());
                if (!workingDir.exists()) {
                    workingDir.mkdirs();
                }
                log.info("Working directory " + workingDir.getAbsolutePath());

                // create 'lac2xspec' command
                String specAsciiFileBase = FileUtil.splitFileBaseAndExtension(specAsciiFile)[0];
                String[] cmd = new String[] { env.getGingaToolsBinDir() + "/lac2xspec", // lac2xspec
                        specAsciiFile.getName(), // Ginga ASCII Spectral File
                        "1", // sub file number
                        "1", // data number
                        "0.01", // systematic
                        specAsciiFileBase + ".pha", // PHA file
                        specAsciiFileBase + ".rsp" // RSP file
                };

                // create output file
                File outputFile = new File(workingDir, specAsciiFileBase + ".log");

                // execute command
                GingaToolsRuntime runtime = new GingaToolsRuntime(workingDir, null, outputFile, cmd);
                log.info("Executing command " + cmd[0] + " ...");
                int exitValue = runtime.exec();
                log.info("Exit value " + exitValue);
                if (exitValue == 0) { // return 'lac2xspec' output file
                    log.info("Command executed successfully");
                    return new File(workingDir, specAsciiFileBase + ".pha");
                } else {
                    log.error("Error executing command " + cmd);
                }
            }
        } catch (IOException e) {
            log.error("Error generating spectrum with lac2xspec", e);
        }
        return null;
    }
}
