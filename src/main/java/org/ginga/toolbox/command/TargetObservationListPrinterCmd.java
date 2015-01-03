package org.ginga.toolbox.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv.DataReductionMode;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.pipeline.TargetObservationListPipe;
import org.ginga.toolbox.util.Constants.LacMode;

public class TargetObservationListPrinterCmd {

	private PrintWriter writer;
	public static final Logger log = Logger.getLogger(TargetListPrinterCmd.class);
	
	public static void main(String[] args) {
		try {
			CommandLine commandLine = new BasicParser().parse(getOptions(), args);
			// read command line argument values 
	        Writer writer = null;
	        String target = commandLine.getOptionValue("t");
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
			if(commandLine.hasOption("i")) { // set interactive mode
				GingaToolboxEnv.getInstance()
					.setDataReductionEnvironment(DataReductionMode.INTERACTIVE);
			}
			// write target list 
			TargetObservationListPrinterCmd cmd = new TargetObservationListPrinterCmd(writer);
			if(commandLine.hasOption("a")) {
				cmd.printAllModes(target);
			} else {
				cmd.printSpectralModes(target);
			}
		} catch (ParseException e) {
			log.error(e.getMessage());
			printHelp();
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		} 
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
    	
    	Option fileOption = OptionBuilder.withArgName("file path")
    			.withLongOpt("file")
    			.withDescription("write observation list to file")
    			.hasArg()
    			.create("f");
    	
    	Option consoleOption = new Option("c", "console", false, "write observation list to console");
    	
    	OptionGroup group1 = new OptionGroup();
    	group1.setRequired(true);
    	group1.addOption(fileOption);
    	group1.addOption(consoleOption);
    	
    	OptionGroup group2 = new OptionGroup();
    	group2.setRequired(true);
    	group2.addOption(new Option("a", "all-modes", false, "include all LAC modes"));
    	group2.addOption(new Option("m", "spectral-modes-only", false, "include MPC1 and MPC2 LAC modes only"));
    	
    	OptionGroup group3 = new OptionGroup();
    	group3.setRequired(true);
    	group3.addOption(new Option("i", "interactive", false, "prompt for input values, e.g. LACDUMP elevation and rigidity constraints"));
    	group3.addOption(new Option("s", "systematic", false, "use default systematic values present in configuration file gingatoolbox.properties "));
    	
    	options.addOption(targetOption);
    	options.addOptionGroup(group1);
    	options.addOptionGroup(group2);
    	options.addOptionGroup(group3);
    	return options;
	}
	
	private static void printHelp() {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.setOptionComparator(new Comparator<Option>() {
			private static final String OPTS_ORDER = "tamcfis"; // short option names
			
		    @Override
			public int compare(Option o1, Option o2) {
		    	return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
			}
		});
    	helpFormatter.printHelp(TargetObservationListPrinterCmd.class.getCanonicalName(), getOptions());
    }   

	public TargetObservationListPrinterCmd(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	
	public TargetObservationListPrinterCmd(PrintStream stream) {
		this.writer = new PrintWriter(stream);
	}
	
    public void printAllModes(String target) {
        TargetObservationListPipe pipe = new TargetObservationListPipe();
        pipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = pipe.next();

        List<SingleModeTargetObservation> singleModeObsList = null;
        for (ObservationEntity obsEntity : obsList) {
        	singleModeObsList = obsEntity.getSingleModeObsList();
            if (singleModeObsList != null) {
                for (SingleModeTargetObservation singleModeObs : singleModeObsList) {
                    this.writer.println(" "
                            + String.format(
                                    "%18s",
                                    obsEntity.getId() + " " 
                                    + obsEntity.getSequenceNumber() + " "
                                            + String.format("%8s", singleModeObs.getMode()) + " "
                                            + String.format("%20s", singleModeObs.getStartTime()) + " "
                                            + String.format("%20s", singleModeObs.getEndTime())));
                }
                this.writer.println("----------------------------------------------------------");
            }
        }
        this.writer.flush();
        this.writer.close();
    }

    public void printSpectralModes(String target) {
        TargetObservationListPipe pipe = new TargetObservationListPipe();
        pipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = pipe.next();

        List<SingleModeTargetObservation> singleModeObsList = null;
        for (ObservationEntity obsEntity : obsList) {
        	singleModeObsList = obsEntity.getSingleModeObsList();
            if (singleModeObsList != null) {
                for (SingleModeTargetObservation singleModeObs : singleModeObsList) {
                	LacMode mode = singleModeObs.getLacMode();
                	if(mode.equals(LacMode.MPC1) || mode.equals(LacMode.MPC2)) {
                        this.writer.println(" "
                                + String.format(
                                        "%18s",
                                        obsEntity.getId() + " " 
                                        + obsEntity.getSequenceNumber() + " "
                                                + String.format("%8s", singleModeObs.getMode()) + " "
                                                + String.format("%20s", singleModeObs.getStartTime()) + " "
                                                + String.format("%20s", singleModeObs.getEndTime())));
                	}
                }
            }
        }
        this.writer.flush();
        this.writer.close();
    }
}