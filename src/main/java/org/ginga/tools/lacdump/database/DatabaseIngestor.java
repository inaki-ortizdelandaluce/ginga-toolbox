package org.ginga.tools.lacdump.database;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LACDumpEntity;
import org.ginga.tools.lacdump.LACDumpEntityList;
import org.ginga.tools.lacdump.dao.DaoException;
import org.ginga.tools.lacdump.dao.LACDumpDao;
import org.ginga.tools.lacdump.dao.impl.LACDumpDaoImpl;
import org.ginga.tools.lacdump.utils.LACDumpParser;

public class DatabaseIngestor {

    private final static Logger log = Logger.getRootLogger();

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out
                    .println("Usage org.ginga.tools.lacdump.DatabaseIngestor <LAC dump directory>");
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

            log.info(lacdumpFiles.length + " file(s) found");
            LACDumpParser parser = new LACDumpParser();
            LACDumpDao dao = new LACDumpDaoImpl();
            LACDumpEntityList entityList = null;
            for (int i = 0; i < lacdumpFiles.length; i++) {
                try {
                    entityList = parser.parse(lacdumpFiles[i]);
                    log.info("LACDUMP contains " + entityList.getEntityCount() + " row(s)");
                    log.info("Inserting LACDUMP file " + lacdumpFiles[i]);
                    // save
                    for (Iterator<LACDumpEntity> iterator = entityList.iterator(); iterator
                            .hasNext();) {
                        dao.save(iterator.next());
                    }
                } catch (IOException e) {
                    log.error("Error parsing LACDUMP " + lacdumpFiles[i].getPath() + ". Message="
                            + e.getMessage(), e);
                    System.exit(1);
                } catch (DaoException e) {
                    log.error("Error ingesting LACDUMP " + lacdumpFiles[i].getPath()
                            + " into the database. Message=" + e.getMessage(), e);
                    System.exit(1);
                }
            }
        }
    }
}
