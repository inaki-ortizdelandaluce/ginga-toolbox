package org.ginga.tools.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.ginga.tools.util.DatabaseProperties;

public class GingaToolsEnvironment {

    private static final Logger log = Logger.getLogger(DatabaseProperties.class);

    private Properties properties;
    private static GingaToolsEnvironment instance;

    public static GingaToolsEnvironment getInstance() {
        if (instance == null) {
            instance = new GingaToolsEnvironment();
        }
        return instance;
    }

    private GingaToolsEnvironment() {
        initialize();
    }

    private void initialize() {
        InputStream in = null;
        String propertiesFilePath = System.getProperty("gingaToolsPropertiesFile");
        if (propertiesFilePath == null) {
            // if system property is not specified use classpath properties
            log.debug("Using Ginga Tools environment properties found in classpath");
            in = DatabaseProperties.class.getClassLoader().getResourceAsStream(
                    "ginga_tools.properties");
        } else {
            try {
                in = new FileInputStream(new File(propertiesFilePath));
                log.debug("Using Ginga Tools environment properties found in " + propertiesFilePath);
            } catch (FileNotFoundException e) {
                log.warn("System property 'gingaToolsPropertiesFile' badly defined. Value: "
                        + propertiesFilePath);
                log.debug("UsingGinga Tools environment properties found in classpath");
                in = DatabaseProperties.class.getClassLoader().getResourceAsStream(
                        "ginga_tools.properties");
            }
        }
        loadProperties(in);
    }

    private void loadProperties(InputStream in) {
        // read properties file
        this.properties = new Properties();
        if (in != null) {
            try {
                this.properties.load(in);
            } catch (IOException e) {
                log.warn("Could not read 'ginga_tools.properties'. Cause: " + e.getMessage());
            }
        } else {
            log.warn("Property file 'ginga_tools.properties' not found in classpath");
        }
    }

    public String getGingaHome() {
        try {
            return this.properties.getProperty("GINGA_HOME", "/home/inaki/ginga/ginga_tools/v1.02");
        } catch (Exception e) {
            log.warn("Cannot access GINGA_HOME, using default value", e);
            return "/home/inaki/ginga/ginga_tools/v1.02";
        }
    }

    public String getGingaCalDir() {
        try {
            return this.properties.getProperty("GINGA_CALDIR",
                    "/home/inaki/ginga/ginga_tools/v1.02/cal");
        } catch (Exception e) {
            log.warn("Cannot access GINGA_CALDIR, using default value", e);
            return "/home/inaki/ginga/ginga_tools/v1.02/cal";
        }
    }

    public String getGingaFrfDir() {
        try {
            return this.properties.getProperty("GINGA_FRFDIR",
                    "/home/inaki/ginga/ginga_tools/v1.02/frf");
        } catch (Exception e) {
            log.warn("Cannot access GINGA_FRFDIR, using default value", e);
            return "/home/inaki/ginga/ginga_tools/v1.02/frf";
        }
    }

    public String getGingaWrkDir() {
        try {
            return this.properties.getProperty("GINGA_WRKDIR", "/home/inaki/ginga/work");
        } catch (Exception e) {
            log.warn("Cannot access GINGA_WRKDIR, using default value", e);
            return "/home/inaki/ginga/work";
        }
    }

    public Map<String, String> getEnv() {
        Map<String, String> env = new HashMap<String, String>();
        // add ginga specific environment
        env.put("GINGA_HOME", getGingaHome());
        env.put("GINGA_CALDIR", getGingaCalDir());
        env.put("GINGA_FRFDIR", getGingaFrfDir());
        env.put("GINGA_WRKDIR", getGingaWrkDir());
        return env;
    }

    public void printEnv() {
        Map<String, String> env = getEnv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n", envName, env.get(envName));
        }
    }

}
