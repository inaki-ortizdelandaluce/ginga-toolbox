package org.ginga.tools.runtime;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class GingaToolsRuntime {

	private static final Logger log = Logger.getLogger(GingaToolsRuntime.class);
	
    private ProcessBuilder pb = null;
    private File logFile = null;
    private File workingDir = null;

    public GingaToolsRuntime(File wrkDir, String command, File logFile) {
        this.workingDir = wrkDir;
        this.pb = new ProcessBuilder(command);
        this.logFile = logFile;
        initialize();
    }

    private void initialize() {
        Map<String, String> env = this.pb.environment();
        // update process environment with ginga tools environment
        GingaToolsEnvironment gte = GingaToolsEnvironment.getInstance();
        Map<String, String> gingaEnv = gte.getEnv();
        for (Iterator<String> iterator = gingaEnv.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            env.put(key, gingaEnv.get(key));
        }
        String path = gte.getGingaBinDir() + ":" + env.get("PATH");
        env.put("PATH", path);
        log.info("PATH=" + env.get("PATH"));
        
        this.pb.directory(this.workingDir);
        this.pb.redirectErrorStream(true);
        this.pb.redirectOutput(Redirect.appendTo(this.logFile));
    }

    public Process exec() throws IOException {
        return this.pb.start();
    }

    public int exitValue(Process p) {
        return p.exitValue();
    }
}
