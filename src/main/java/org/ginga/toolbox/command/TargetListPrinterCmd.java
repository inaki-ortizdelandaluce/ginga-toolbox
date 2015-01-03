package org.ginga.toolbox.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Comparator;
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

public class TargetListPrinterCmd {

	public static final Logger log = Logger.getLogger(TargetListPrinterCmd.class);
	
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
			} else if(commandLine.hasOption("c")){
				writer = new PrintWriter(System.out);
			} else {
				printHelp();
				System.exit(0);
			}
			// write target list 
			TargetListPrinterCmd cmd = new TargetListPrinterCmd(writer);
			cmd.print();
		} catch (ParseException e) {
			log.error(e.getMessage());
			printHelp();
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		} 
    }

	private static void printHelp() {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.setOptionComparator(new Comparator<Option>() {
			private static final String OPTS_ORDER = "fch"; // short option names
			
		    @Override
			public int compare(Option o1, Option o2) {
		    	String argCharOption1 = o1.getLongOpt().substring(0, 1);
		    	String argCharOption2 = o2.getLongOpt().substring(0, 1);
		    	return OPTS_ORDER.indexOf(argCharOption1) - OPTS_ORDER.indexOf(argCharOption2);
			}
		});
    	helpFormatter.printHelp(TargetListPrinterCmd.class.getCanonicalName(), getOptions());
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
    	Option helpOption = new Option("h", "help", false, "show command help");
    	
    	OptionGroup group = new OptionGroup();
    	group.setRequired(true);
    	group.addOption(helpOption);
    	group.addOption(fileOption);
    	group.addOption(consoleOption);
    	options.addOptionGroup(group);
    	return options;
	}

	public TargetListPrinterCmd(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	
	public TargetListPrinterCmd(PrintStream stream) {
		this.writer = new PrintWriter(stream);
	}
	
    public void print() {
    	ObservationDao dao = new ObservationDaoImpl();
        Set<String> targetList;
		try {
			targetList = dao.findAllTargets();
			for (String target : targetList) {
				log.debug("Target " + target);
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
