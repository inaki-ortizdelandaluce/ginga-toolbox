package org.ginga.toolbox.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class DatabaseSchemaGenerator {

    private enum Dialect {
        MySQL("org.hibernate.dialect.MySQLDialect"), MySQLInno(
                "org.hibernate.dialect.MySQLInnoDBDialect"), MySQLMyISAM(
                "org.hibernate.dialect.MySQLMyISAMDialect"), MySQL5(
                "org.hibernate.dialect.MySQL5Dialect"), MySQL5Inno(
                "org.hibernate.dialect.MySQL5InnoDBDialect"), PostgreSQL9(
                                                "org.hibernate.dialect.PostgreSQL9Dialect");

        private String className;

        private Dialect(String className) {
            this.className = className;
        }

        public String getClassName() {
            return this.className;
        }
    }

    public void generate(Class<?> entity, File outputFile) {
        List<Class<?>> list = new ArrayList<>();
        list.add(entity);
        generate(list, outputFile);
    }

    public void generate(List<Class<?>> entities, File outputFile) {
        // set-up hibernate configuration
        Configuration configuration = new Configuration();
        if (GingaToolboxEnv.isMySQLDatabase()) {
            configuration.setProperty(Environment.DIALECT, Dialect.MySQL5Inno.getClassName());
        } else if (GingaToolboxEnv.isPostgreSQLDatabase()) {
            configuration.setProperty(Environment.DIALECT, Dialect.PostgreSQL9.getClassName());
        } else {
            throw new IllegalArgumentException("Dialect "
                    + GingaToolboxEnv.getInstance().getDatabaseDialect() + " not supported");
        }
        configuration.setProperty("hibernate.default_schema", "default_schema");
        for (Class<?> entityClass : entities) {
            configuration.addAnnotatedClass(entityClass);
        }
        // build output file
        File schemaDirectory = outputFile.getParentFile();
        if (schemaDirectory != null && !schemaDirectory.exists()) {
            schemaDirectory.mkdirs();
        }
        // run hbm2ddl programmatically
        SchemaExport schemaExport = new SchemaExport(configuration);
        schemaExport.setDelimiter(";");
        schemaExport.setOutputFile(outputFile.getPath());
        boolean consolePrint = true;
        boolean exportInDatabase = false;
        schemaExport.create(consolePrint, exportInDatabase);
    }
}
