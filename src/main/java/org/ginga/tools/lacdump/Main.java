package org.ginga.tools.lacdump;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Main {

	private final static Logger log = Logger.getRootLogger();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage org.ginga.tools.lacdump.Main <LAC dump directory>");
            System.exit(1);
        } else {
            File f = new File(args[0]);
            if (!f.exists()) {
                log.error("Directory " + f.getPath() + " not found");
                System.exit(1);
            } else if(!f.isDirectory()) {
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
            LACDumpEntityList entityList = null;
            for (int i = 0; i < lacdumpFiles.length; i++) {
                try {
                	entityList = parser.parse(lacdumpFiles[i]);
                    log.info("LACDUMP contains " + entityList.getEntityCount() + " row(s)");
                } catch (IOException e) {
                    log.error("Error parsing LACDUMP " + lacdumpFiles[i].getPath() + ". Message=" + e.getMessage());
				}
            }
        }
	}

}
