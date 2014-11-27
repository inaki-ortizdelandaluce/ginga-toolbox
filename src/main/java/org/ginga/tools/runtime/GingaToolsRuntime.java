package org.ginga.tools.runtime;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Iterator;
import java.util.Map;

public class GingaToolsRuntime {

    private ProcessBuilder pb = null;
    private File logFile = null;

    public GingaToolsRuntime(String command, File logFile) {
        this.pb = new ProcessBuilder(command);
        this.logFile = logFile;
        initialize();
    }

    private void initialize() {
        Map<String, String> env = this.pb.environment();
        // update process environment with ginga tools environment
        GingaToolsEnvironment gingaToolsEnv = GingaToolsEnvironment.getInstance();
        Map<String, String> newEnv = gingaToolsEnv.getEnv();
        for (Iterator<String> iterator = newEnv.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            env.put(key, newEnv.get(key));
        }
        this.pb.directory(new File(gingaToolsEnv.getGingaWrkDir()));
        this.pb.redirectErrorStream(true);
        this.pb.redirectOutput(Redirect.appendTo(this.logFile));
    }

    public Process exec() throws IOException {
        return this.pb.start();
    }
}
