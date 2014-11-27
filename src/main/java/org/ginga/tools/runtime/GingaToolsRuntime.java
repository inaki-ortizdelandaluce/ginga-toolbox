package org.ginga.tools.runtime;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class GingaToolsRuntime {

    private static final Logger log = Logger.getLogger(GingaToolsRuntime.class);

    private ProcessBuilder pb = null;
    private File inputFile = null;
    private File outputFile = null;
    private File workingDir = null;

    public GingaToolsRuntime(File wrkDir, String command, File inputFile, File outputFile) {
        this.workingDir = wrkDir;
        this.pb = new ProcessBuilder(command);
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        initialize();
    }

    private void initialize() {
        Map<String, String> env = this.pb.environment();
        // update environment variables with ginga tools environment
        GingaToolsEnvironment gte = GingaToolsEnvironment.getInstance();
        Map<String, String> gingaEnv = gte.getEnv();
        for (Iterator<String> iterator = gingaEnv.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            env.put(key, gingaEnv.get(key));
        }
        // update PATH
        String path = gte.getGingaBinDir() + ":" + env.get("PATH");
        env.put("PATH", path);
        log.info("PATH=" + env.get("PATH"));
        // set working directory
        this.pb.directory(this.workingDir);
        // set input redirection
        this.pb.redirectInput(this.inputFile); // this.pb.redirectInput(Redirect.PIPE);
        // set output redirection
        this.pb.redirectOutput(this.outputFile); // this.pb.redirectOutput(Redirect.PIPE);
        // set error redirection
        this.pb.redirectErrorStream(true);
    }

    public int exec() throws IOException {
        Process p = this.pb.start();
        try {
            return p.waitFor();
        } catch (InterruptedException e) {
            return -1;
        }
    }
}
