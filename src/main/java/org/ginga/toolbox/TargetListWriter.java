package org.ginga.toolbox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnvironment;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;

public class TargetListWriter {

	private static final Logger log = Logger.getLogger(TargetListWriter.class);
	
	private PrintWriter writer;
	
	public TargetListWriter(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	
	public TargetListWriter(PrintStream stream) {
		this.writer = new PrintWriter(stream);
	}
	
    public void write() {
    	ObservationDao dao = new ObservationDaoImpl();
        Set<String> targetList;
		try {
			targetList = dao.findAllTargets();
			for (String target : targetList) {
				log.info("Target " + target);
	        	this.writer.println(target);
	        }
		} catch (ObservationDaoException e) {
			log.error("Error retrieving the list of targets available", e);
		} finally {
			this.writer.flush();
			this.writer.close();
		}
    }

    public static void main(String[] args) throws IOException {
        File workingDir = new File(GingaToolboxEnvironment.getInstance().getGingaWrkDir());
        if(!workingDir.exists()) {
        	workingDir.mkdirs();
        }
        File f = new File(workingDir, "Ginga-targets.txt");
        FileWriter fw = new FileWriter(f);
        TargetListWriter writer = new TargetListWriter(fw);
        writer.write();
    }
}
