package org.ginga.toolbox.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class GingaToolboxEnv {

    private static final Logger log = Logger.getLogger(GingaToolboxEnv.class);
    private static GingaToolboxEnv instance;

    private Properties properties;
    private DataReductionEnv dataReductionEnv;
    
    public static GingaToolboxEnv getInstance() {
        if (instance == null) {
            instance = new GingaToolboxEnv();
        }
        return instance;
    }

    private GingaToolboxEnv() {
        initialize();
    }

    private void initialize() {
        InputStream in = null;
        String propertiesFilePath = System.getProperty("gingaToolboxPropertiesFile");
        if (propertiesFilePath == null) {
            // if system property is not specified use classpath properties
            log.debug("Using Ginga Tools environment properties found in classpath");
            in = GingaToolboxEnv.class.getClassLoader().getResourceAsStream(
                    "gingatoolbox.properties");
        } else {
            try {
                in = new FileInputStream(new File(propertiesFilePath));
                log.debug("Using Ginga Tools environment properties found in " + propertiesFilePath);
            } catch (FileNotFoundException e) {
                log.warn("System property 'gingaToolboxPropertiesFile' badly defined. Value: "
                        + propertiesFilePath);
                log.debug("UsingGinga Tools environment properties found in classpath");
                in = GingaToolboxEnv.class.getClassLoader().getResourceAsStream(
                        "gingatoolbox.properties");
            }
        }
        // read properties file
        this.properties = new Properties();
        if (in != null) {
            try {
                this.properties.load(in);
            } catch (IOException e) {
                log.warn("Could not read 'gingatoolbox.properties'. Cause: " + e.getMessage());
            }
        } else {
            log.warn("Property file 'gingatoolbox.properties' not found in classpath");
        }
        // set default data reduction environment
        setDataReductionEnvironment(DataReductionMode.SYSTEMATIC);
    }
    
    public String getGingaToolsHome() {
        try {
            return this.properties.getProperty("GINGA_HOME", "$HOME/ginga/ginga_tools/v1.02");
        } catch (Exception e) {
            log.warn("Cannot access GINGA_HOME, using default value", e);
            return "$HOME/ginga/ginga_tools/v1.02";
        }
    }

    public String getGingaToolsCalDir() {
        try {
            return this.properties.getProperty("GINGA_CALDIR", "$GINGA_HOME/cal");
        } catch (Exception e) {
            log.warn("Cannot access GINGA_CALDIR, using default value", e);
            return "$GINGA_HOME/cal";
        }
    }

    public String getGingaToolsFrfDir() {
        try {
            return this.properties.getProperty("GINGA_FRFDIR", "$HOME/ginga/data/frf");
        } catch (Exception e) {
            log.warn("Cannot access GINGA_FRFDIR, using default value", e);
            return "$HOME/ginga/data/frf";
        }
    }

    public String getWorkingDir() {
        try {
            return this.properties.getProperty("working.dir", "$HOME/ginga/work");
        } catch (Exception e) {
            log.warn("Cannot access working.dir property, using default value", e);
            return "$HOME/ginga/work";
        }
    }

    public String getGingaToolsBinDir() {
        try {
            return this.properties.getProperty("GINGA_BINDIR", "$GINGA_HOME/bin/linux");
        } catch (Exception e) {
            log.warn("Cannot access GINGA_BINDIR, using default value", e);
            return "$GINGA_HOME/bin/linux";
        }
    }
    
    public String getGingaToolsPfiles() {
        try {
            return this.properties.getProperty("PFILES", "$HOME/pfiles");
        } catch (Exception e) {
            log.warn("Cannot access PFILES, using default value", e);
            return "$HOME/pfiles";
        }
    }

    public String getGingaToolsPgPlotFont() {
        try {
            return this.properties.getProperty("PGPLOT_FONT",
                    "$HOME/ginga/ginga_ledas/bin/grfont.dat");
        } catch (Exception e) {
            log.warn("Cannot access PGPLOT_FONT, using default value", e);
            return "$HOME/ginga/ginga_ledas/bin/grfont.dat";
        }
    }
    
    public String getDatabaseDriverClassName() {
        try {
            return this.properties.getProperty("jdbc.driverClassName", "com.mysql.jdbc.Driver");
        } catch (Exception e) {
            log.warn("Cannot access JDBC driver classname, using default value", e);
            return "com.mysql.jdbc.Driver";
        }
    }

    public String getDatabaseUrl() {
        try {
            return this.properties.getProperty("jdbc.url", "jdbc:mysql://localhost:3306");
        } catch (Exception e) {
            log.warn("Cannot access JDBC database url, using default value", e);
            return "jdbc:mysql://localhost:3306";
        }
    }

    public String getDatabaseUser() {
        try {
            return this.properties.getProperty("jdbc.user", "dbadmin");
        } catch (Exception e) {
            log.warn("Cannot access JDBC database user, using default value", e);
            return "dbadmin";
        }
    }

    public String getDatabasePassword() {
        try {
            return this.properties.getProperty("jdbc.password", "dbadmin");
        } catch (Exception e) {
            log.warn("Cannot access JDBC database password, using default value", e);
            return "dbadmin";
        }
    }

    public enum DataReductionMode { DEFAULT, SYSTEMATIC, INTERACTIVE }
    
    public void setDataReductionEnvironment(DataReductionMode mode) {
    	switch(mode) {
    	case SYSTEMATIC:
    	case DEFAULT:
    		log.info("Selecting systematic data reduction");
    		this.dataReductionEnv = SystematicDataReductionEnv.getInstance(this.properties);
    		break;
    	case INTERACTIVE:
    		log.info("Selecting interactive data reduction");
    		this.dataReductionEnv = new InteractiveDataReductionEnv();
    		break;
    	}
    }
    
    public DataReductionEnv getDataReductionEnv() {
    	return this.dataReductionEnv;
    }

    public Map<String, String> getEnv() {
        Map<String, String> env = new HashMap<String, String>();
        // add ginga specific environment
        env.put("GINGA_HOME", getGingaToolsHome());
        env.put("GINGA_BINDIR", getGingaToolsBinDir());
        env.put("GINGA_CALDIR", getGingaToolsCalDir());
        env.put("GINGA_FRFDIR", getGingaToolsFrfDir());
        env.put("PFILES", getGingaToolsPfiles());
        env.put("PG_PLOT_FONT", getGingaToolsPgPlotFont());
        return env;
    }

    public void printEnv() {
        Map<String, String> env = getEnv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n", envName, env.get(envName));
        }
    }

}
