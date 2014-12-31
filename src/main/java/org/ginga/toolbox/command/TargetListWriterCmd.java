package org.ginga.toolbox.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;

public class TargetListWriterCmd {

	public static final Logger log = Logger.getLogger(TargetListWriterCmd.class);
	
	private PrintWriter writer;
	
	public static void main(String[] args) {
		try {
			CommandLine commandLine = new BasicParser().parse(getOptions(), args);
			// read command line argument values 
	        Writer writer = null;
			if(commandLine.hasOption("f")) {
				String filePath = commandLine.getOptionValue("f");
				File f = new File(filePath);
				// create parent directory if it does not exist
				if(!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				writer = new FileWriter(f);
			} else {
				writer = new PrintWriter(System.out);
			}
			// write target list 
			TargetListWriterCmd cmd = new TargetListWriterCmd(writer);
			cmd.write();
		} catch (ParseException e) {
			log.error(e.getMessage());
			HelpFormatter helpFormatter = new HelpFormatter();
	    	helpFormatter.printHelp(TargetListWriterCmd.class.getCanonicalName(), getOptions());
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		} 
    }
	
	@SuppressWarnings("static-access")
	private static Options getOptions() {
    	Options options = new Options();
    	Option fileOption = OptionBuilder.withArgName("file path")
    			.withLongOpt("file")
    			.withDescription("write target list to file")
    			.hasArg()
    			.create("f");
    	Option consoleOption = new Option("c", "console", false, "write target list to console");
    	OptionGroup group = new OptionGroup();
    	group.setRequired(true);
    	group.addOption(fileOption);
    	group.addOption(consoleOption);
    	options.addOptionGroup(group);
    	return options;
	}

	public TargetListWriterCmd(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	
	public TargetListWriterCmd(PrintStream stream) {
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
}
