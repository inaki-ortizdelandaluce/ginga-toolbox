/**
 * BepiColombo BSCS
 *
 * Subsystem: CIS/RHS/RHAP/RHCO
 *
 * Name: DatabaseProperties.java
 *
 * Description: This class provides singleton access to configuration properties like JDBC
 * parameters
 *
 * Dependencies: None
 *
 * History: 1.00 2014-08-22 First version
 *
 * @author iortiz
 */
package org.ginga.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DatabaseProperties {

    private static final Logger log = Logger.getLogger(DatabaseProperties.class);

    private Properties properties;
    private static DatabaseProperties instance;

    private DatabaseProperties() {
        initialize();
    }

    public static DatabaseProperties getInstance() {
        if (instance == null) {
            instance = new DatabaseProperties();
        }
        return instance;
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

    private void initialize() {
        InputStream in = null;
        String propertiesFilePath = System.getProperty("databasePropertiesFile");
        if (propertiesFilePath == null) {
            // if system property is not specified use classpath properties
            log.debug("Using database properties found in classpath");
            in = DatabaseProperties.class.getClassLoader().getResourceAsStream(
                    "database.properties");
        } else {
            try {
                in = new FileInputStream(new File(propertiesFilePath));
                log.debug("Using database properties found in " + propertiesFilePath);
            } catch (FileNotFoundException e) {
                log.warn("System property 'databasePropertiesFile' badly defined. Value: "
                        + propertiesFilePath);
                log.debug("Using database properties found in classpath");
                in = DatabaseProperties.class.getClassLoader().getResourceAsStream(
                        "database.properties");
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
                log.warn("Could not read 'database.properties'. Cause: " + e.getMessage());
            }
        } else {
            log.warn("Property file 'database.properties' not found in classpath");
        }
    }
}
