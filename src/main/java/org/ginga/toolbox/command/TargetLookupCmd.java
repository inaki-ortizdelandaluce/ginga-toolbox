package org.ginga.toolbox.command;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.observation.dao.impl.ObservationDaoImpl;

public class TargetLookupCmd {
	
	public static final Logger log = Logger.getLogger(TargetLookupCmd.class);
	
	public static void main(String[] args) {
		try {
			CommandLine commandLine = new BasicParser().parse(getOptions(), args);
			// read command line argument values 
			String target = commandLine.getOptionValue("t");
			if(commandLine.hasOption("h")) {
				printHelp();
				System.exit(0);
			}
			// write target list 
			TargetLookupCmd cmd = new TargetLookupCmd();
			cmd.lookup(target);
		} catch (ParseException e) {
			log.error(e.getMessage());
			printHelp();
		}  
    }

	private static void printHelp() {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.setOptionComparator(new Comparator<Option>() {
			private static final String OPTS_ORDER = "th"; // short option names
			
		    @Override
			public int compare(Option o1, Option o2) {
		    	String argCharOption1 = o1.getLongOpt().substring(0, 1);
		    	String argCharOption2 = o2.getLongOpt().substring(0, 1);
		    	return OPTS_ORDER.indexOf(argCharOption1) - OPTS_ORDER.indexOf(argCharOption2);
			}
		});
    	helpFormatter.printHelp(TargetListWriterCmd.class.getCanonicalName(), getOptions());
	}
	
	@SuppressWarnings("static-access")
	private static Options getOptions() {
		Options options = new Options();
    	
    	Option targetOption = OptionBuilder.withArgName("target")
    			.withLongOpt("target")
    			.isRequired()
    			.withDescription("Target name")
    			.hasArg()
    			.create("t");
    	
    	Option helpOption = new Option("h", "help", false, "show command help");
    	
    	options.addOption(targetOption);
    	options.addOption(helpOption);
    	
    	return options;
	}

    public void lookup(String target) {
    	ObservationDao dao = new ObservationDaoImpl();
        List<ObservationEntity> targetList;
		try {
			targetList = dao.findListByTarget(target);
			if(targetList == null || targetList.size() == 0) {
				System.out.println("Target " + target + " not found");
			} else {
				System.out.println("Target " + target + " found in " 
						+ targetList.size() + " Ginga observation(s)");
			}
		} catch (ObservationDaoException e) {
			log.error("Error with target lookup", e);
		} 
    }
}
