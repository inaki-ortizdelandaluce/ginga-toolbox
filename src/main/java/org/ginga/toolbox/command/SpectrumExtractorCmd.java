package org.ginga.toolbox.command;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv.DataReductionMode;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.pipeline.SpectrumHayashidaPipeline;
import org.ginga.toolbox.util.DateUtil;
import org.ginga.toolbox.util.Constants.BackgroundSubtractionMethod;
import org.ginga.toolbox.util.Constants.LacMode;

public class SpectrumExtractorCmd {

	protected final static String DATE_FORMAT_PATTERN = DateUtil.DATE_FORMAT_INPUT.toPattern();
    private final static Logger log = Logger.getLogger(SpectrumExtractorCmd.class);

    public static void main(String[] args) {
		try {
			CommandLine commandLine = new BasicParser().parse(getOptions(), args);
			SpectrumExtractorScanner scanner = new SpectrumExtractorScanner();
			// read command line argument values 
	        // TARGET
			String target = null;
	        if(commandLine.hasOption("t")) {
	        	target = commandLine.getOptionValue("t");
	        } else {
	        	target = scanner.scanTarget();
	        }
	        // BACKGROUND SUBTRACTION METHOD
	        BackgroundSubtractionMethod method = null;
	        if(commandLine.hasOption("b")) {
	        	try {
	        		method = Enum.valueOf(BackgroundSubtractionMethod.class,
	        				commandLine.getOptionValue("b"));
	        	} catch (IllegalArgumentException e) {
	        		log.error("Unknown background subtraction method " 
	        				+ commandLine.getOptionValue("b"));
	        		printHelp();
	        		System.exit(1);
	        	}
	        } else {
	        	method = scanner.scanBackgroundMethod();
	        }
	        // LAC MODE
	        String mode = null;
	        if(commandLine.hasOption("m")) {
	        	mode = commandLine.getOptionValue("m");
	        	try {
	        		Enum.valueOf(LacMode.class,
	        				commandLine.getOptionValue("m"));
	        	} catch (IllegalArgumentException e) {
	        		log.error("Unknown background LAC mode " 
	        				+ commandLine.getOptionValue("m"));
	        		printHelp();
	        		System.exit(1);
	        	}
	        } else {
	        	mode = scanner.scanLacMode();
	        }
	        // OBSERVATION IDENTIFIER
			long obsId = 0;
			if(commandLine.hasOption("o")) {
				try {
					obsId = Long.valueOf(commandLine.getOptionValue("o")).longValue();
				} catch(NumberFormatException e) {
					log.error("Observation identifier " 
							+ commandLine.getOptionValue("o") + " is not an integer");
					printHelp();
					System.exit(1);
				}
			} else {
				obsId = scanner.scanObsId();
			}
			// START TIME
			String startTime = null;
			if(commandLine.hasOption("start-time")) {
				try {
					startTime = commandLine.getOptionValue("start-time");
					DateUtil.parseInputFormat(startTime);
				} catch(java.text.ParseException e) {
					log.error("Start time format is not valid. " + e.getMessage());
					printHelp();
					System.exit(1);
				}
			} else {
				startTime = scanner.scanStartTime();
			}
			String endTime = null;
			// END TIME
			if(commandLine.hasOption("start-time")) {
				try {
					endTime = commandLine.getOptionValue("end-time");
					DateUtil.parseInputFormat(endTime);
				} catch(java.text.ParseException e) {
					log.error("End time format is not valid. " + e.getMessage());
					printHelp();
					System.exit(1);
				}
			} else {
				endTime = scanner.scanEndTime();
			}
			scanner.close();
			if(commandLine.hasOption("i")) { // set interactive mode
				GingaToolboxEnv.getInstance()
					.setDataReductionEnvironment(DataReductionMode.INTERACTIVE);
			}
			// build single mode target observation instance from arguments
			SingleModeTargetObservation obs = new SingleModeTargetObservation();
			obs.setObsId(obsId);
			obs.setTarget(target);
			obs.setMode(mode);
			obs.setStartTime(startTime);
			obs.setStartTime(endTime);
			// extract spectrum
			extractSpectrum(obs, method);
		} catch (Exception e) {
			log.error(e.getMessage());
			printHelp();
		} 
    }
	
	@SuppressWarnings("static-access")
	private static Options getOptions() {
    	Options options = new Options();
    	Option obsIdOption = OptionBuilder.withArgName("observation id")
    			.withLongOpt("observation-id")
    			.withDescription("Observation identifier")
    			.hasArg()
    			.create("o");
    	Option targetOption = OptionBuilder.withArgName("target")
    			.withLongOpt("target")
    			.withDescription("Target name")
    			.hasArg()
    			.create("t");
    	Option lacModeOption = OptionBuilder.withArgName("LAC mode")
    			.withLongOpt("mode")
    			.withDescription("LAC mode. Possible values: " + getLacModes())
    			.hasArg()
    			.create("m");
    	Option methodOption = OptionBuilder.withArgName("method")
    			.withLongOpt("background-method")
    			.withDescription("Background subtraction method. Possible values: " + getBackgroundSubtractionMethods())
    			.hasArg()
    			.create("b");
    	Option startTimeOption = OptionBuilder.withArgName("start time")
    			.withLongOpt("start-time")
    			.withDescription("Start time in " + DATE_FORMAT_PATTERN + " format")
    			.hasArg()
    			.create();
    	Option endTimeOption = OptionBuilder.withArgName("end time")
    			.withLongOpt("end-time")
    			.withDescription("End time in " + DATE_FORMAT_PATTERN + " format")
    			.hasArg()
    			.create();
    	OptionGroup group1 = new OptionGroup();
    	group1.setRequired(true);
    	group1.addOption(new Option("i", "interactive", false, "prompt for input values, e.g. LACDUMP elevation and rigidity constraints"));
    	group1.addOption(new Option("s", "systematic", false, "use default systematic values present in configuration file gingatoolbox.properties "));
    	
    	options.addOption(targetOption);
    	options.addOption(methodOption);
    	options.addOption(obsIdOption);
    	options.addOption(lacModeOption);
    	options.addOption(startTimeOption);
    	options.addOption(endTimeOption);
    	
    	options.addOptionGroup(group1);
    	
    	return options;
	}
	
    protected static String getLacModes() {
    	String s = "";
    	LacMode[] modes = LacMode.values();
    	for (int i = 0; i < modes.length; i++) {
    		s += modes[i].toString() + ", ";
		}
    	return s.substring(0, s.length()-2);
    }
    
    protected static String getBackgroundSubtractionMethods() {
    	String s = "";
    	BackgroundSubtractionMethod[] methods = BackgroundSubtractionMethod.values();
    	for (int i = 0; i < methods.length; i++) {
    		s += methods[i].toString() + ", ";
		}
    	return s.substring(0, s.length()-2);
    }

	private static void printHelp() {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.setOptionComparator(null);
    	helpFormatter.printHelp(SpectrumExtractorCmd.class.getCanonicalName(), getOptions());
    }   
	
	public static void extractSpectrum(SingleModeTargetObservation obs, BackgroundSubtractionMethod method) {
		switch(method) {
		case HAYASHIDA:
			extractSpectrumHayashida(obs);
		case SIMPLE:
		case SUD_SORT:
		default:
			log.error(method + " background subtraction method not supported");
			System.exit(1);
		}
	}
	
    public static void extractSpectrumHayashida(SingleModeTargetObservation obs) {
    	SpectrumHayashidaPipeline specHayashidaPipeline = new SpectrumHayashidaPipeline();
        specHayashidaPipeline.run(Arrays.asList(obs));
        File file = specHayashidaPipeline.next();
        if (file != null) {
        	log.info("Spectrum file " + file.getName() + " created successfully");
        }
    }

}