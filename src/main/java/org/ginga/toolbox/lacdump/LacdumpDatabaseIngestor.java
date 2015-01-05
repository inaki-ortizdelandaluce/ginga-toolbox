package org.ginga.toolbox.lacdump;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.util.FileUtil;

public class LacdumpDatabaseIngestor {

    private final static Logger log = Logger.getRootLogger();

    public LacdumpDatabaseIngestor() {

    }

    public void ingest(File lacdumpRootDir) {
        if (!lacdumpRootDir.exists()) {
            log.error("Directory " + lacdumpRootDir.getPath() + " not found");
            System.exit(1);
        } else if (!lacdumpRootDir.isDirectory()) {
            log.error(lacdumpRootDir.getPath() + " is not a directory");
            System.exit(1);
        }

        File[] lacdumpFiles = lacdumpRootDir.listFiles(new FileUtil.LacdumpFileFilter());
        Arrays.sort(lacdumpFiles);

        log.info(lacdumpFiles.length + " file(s) found");
        LacdumpParser parser = new LacdumpParser();
        LacdumpDao dao = new LacdumpDaoImpl();
        List<LacdumpSfEntity> sfList = null;
        for (int i = 0; i < lacdumpFiles.length; i++) {
            try {
                sfList = parser.parse(lacdumpFiles[i]);
                log.info("LACDUMP contains " + sfList.size() + " row(s)");
                log.info("Inserting LACDUMP file " + lacdumpFiles[i]);
                dao.saveList(sfList);
            } catch (IOException e) {
                log.error(
                        "Error parsing LACDUMP " + lacdumpFiles[i].getPath() + ". Message="
                                + e.getMessage(), e);
                System.exit(1);
            } catch (LacdumpDaoException e) {
                log.error("Error ingesting LACDUMP " + lacdumpFiles[i].getPath()
                        + " into the database. Message=" + e.getMessage(), e);
                System.exit(1);
            }
        }
        log.info(lacdumpFiles.length + " file(s) ingested into the database successfully");
    }
}