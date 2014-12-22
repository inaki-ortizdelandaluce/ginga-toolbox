package org.ginga.toolbox.lacdump;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;

public class LacdumpDatabaseIngestor {

    private final static Logger log = Logger.getRootLogger();

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out
                    .println("Usage org.ginga.toolbox.lacdump.DatabaseIngestor <LAC dump directory>");
            System.exit(1);

        } else {

            File f = new File(args[0]);
            if (!f.exists()) {
                log.error("Directory " + f.getPath() + " not found");
                System.exit(1);
            } else if (!f.isDirectory()) {
                log.error(f.getPath() + " is not a directory");
                System.exit(1);
            }

            File[] lacdumpFiles = f.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return name.matches("J[0-9]{6}");
                }
            });
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
                    log.error("Error parsing LACDUMP " + lacdumpFiles[i].getPath() + ". Message="
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
}
