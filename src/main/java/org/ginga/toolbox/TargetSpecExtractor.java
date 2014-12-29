package org.ginga.toolbox;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.pipeline.SpectrumHayashidaPipeline;
import org.ginga.toolbox.pipeline.TargetObservationListPipe;
import org.ginga.toolbox.util.Constants.BackgroundSubtractionMethod;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.DateUtil;

import com.tinkerpop.pipes.Pipe;

public class TargetSpecExtractor {

    private final static Logger log = Logger.getLogger(TargetSpecExtractor.class);
   
    public TargetSpecExtractor() {
    }

    public void extractAllSpectra(String target, BackgroundSubtractionMethod method) {
  	switch(method) {
    	case HAYASHIDA:
    		extractAllSpectraHayashida(target);
    	case SIMPLE:
    	case SUD_SORT:
    	default:
    		log.error(method + " background subtraction method not supported");
    		System.exit(1);
    	}
    }
    
    public void extractSingleSpectrum(SingleModeTargetObservation obs, BackgroundSubtractionMethod method) {
    	switch(method) {
    	case HAYASHIDA:
    		extractSingleSpectrumHayashida(obs);
    	case SIMPLE:
    	case SUD_SORT:
    	default:
    		log.error(method + " background subtraction method not supported");
    		System.exit(1);
    	}
    }

    private void extractAllSpectraHayashida(String target) {
        // find all observations for input target
        Pipe<String, List<ObservationEntity>> obsListPipe = new TargetObservationListPipe();
        obsListPipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = obsListPipe.next();
        // extract spectra for all observations
    	SpectrumHayashidaPipeline specHayashidaPipeline = new SpectrumHayashidaPipeline();
        List<SingleModeTargetObservation> singleModeObsList = null;
        for (ObservationEntity obsEntity : obsList) {
            log.info("Processing observation " + obsEntity.getSequenceNumber() + "...");
            singleModeObsList = obsEntity.getSingleModeObsList();
            // extract spectrum for all relevant modes
            if (singleModeObsList != null) {
                // run pipeline
                specHayashidaPipeline.run(singleModeObsList);
                File file = null;
                while (specHayashidaPipeline.hasNext()) {
                    file = specHayashidaPipeline.next();
                    if (file != null) {
                        log.info("Spectrum file " + file.getName() + " created successfully");
                    }
                }
            }
            log.info("Observation " + obsEntity.getSequenceNumber() + " processed successfully");
        }
    }

    private void extractSingleSpectrumHayashida(SingleModeTargetObservation obs) {
		if (obs != null) {
        	// run pipeline
        	SpectrumHayashidaPipeline specHayashidaPipeline = new SpectrumHayashidaPipeline();
            specHayashidaPipeline.run(Arrays.asList(obs));
            File file = specHayashidaPipeline.next();
            if (file != null) {
            	log.info("Spectrum file " + file.getName() + " created successfully");
            }
        }
    }
    
    @SuppressWarnings("static-access")
	public static void main(String[] args) {
    	// build options
    	Options options = new Options();
    	options.addOption("a", "all", false, "Extract all spectra available for the target");
    	options.addOption(OptionBuilder.withArgName("target")
    			.withLongOpt("target")
    			.isRequired(true)
    			.withDescription("Target Name")
    			.hasArg()
    			.create("t"));
    	options.addOption(OptionBuilder.withArgName("method")
    			.withLongOpt("background-method")
    			.isRequired(true)
    			.withDescription("Background subtraction method. Possibe values: " + getBackgroundSubtractionMethods())
    			.hasArg()
    			.create("b"));
    	// process command line arguments
		try {
			// parse the command line arguments
	        CommandLine commandLine = new BasicParser().parse(options, args);
	        TargetSpecExtractor extractor = new TargetSpecExtractor();
	        // read required command line argument values 
	        String target = commandLine.getOptionValue("t");
    		BackgroundSubtractionMethod method = null;
    		try {
    			method = Enum.valueOf(BackgroundSubtractionMethod.class,
    				commandLine.getOptionValue("b"));
        	} catch (IllegalArgumentException e) {
        		log.error("Unknown background subtraction method " + args[1]);
        		printHelp(options);
        		System.exit(1);
        	}
	        // execute spectra extraction
    		if(commandLine.hasOption("a")) { // extract all spectra
    			extractor.extractAllSpectra(target, method);
	        } else { // extract single spectra
	        	SingleModeTargetObservation obs = getSingleModeObservationFromInput();
	        	obs.setTarget(target);
	        	//extractor.extractSingleSpectrum(obs, method);
	        }
		} catch (ParseException| IllegalArgumentException e) {
			log.error(e.getMessage());
			printHelp(options);
		}
    }
    
    private static SingleModeTargetObservation getSingleModeObservationFromInput() {
    	SingleModeTargetObservation obs = new SingleModeTargetObservation();
    	Scanner scanner = new Scanner(System.in);
    	try {
        	// OBSERVATION ID 
        	System.out.print("Enter Observation Id: ");
        	long obsId = scanner.nextLong();

    		// LAC MODE
        	System.out.print("Enter LAC Mode (" + getLacModes() + "): ");
        	String mode = scanner.next();
        	// check value
        	try {
        		LacMode.valueOf(mode);
        	} catch(IllegalArgumentException e2) {
        		throw new IllegalArgumentException(mode + " is not a valid LAC mode");
        	}

    		// START DATE
    		System.out.print("Enter Observation Start Time (" + DateUtil.DATE_FORMAT_INPUT.toPattern() + "): ");
    		Date startTime = DateUtil.parseInputFormat(scanner.next());
    		
    		// END TIME
    		System.out.print("Enter Observation End Time (" + DateUtil.DATE_FORMAT_DATABASE.toPattern() + "): ");
    		Date endTime = DateUtil.parseInputFormat(scanner.next());
    			 
    		obs.setObsId(obsId);
    		obs.setStartTime(DateUtil.formatInputDate(startTime));
    		obs.setEndTime(DateUtil.formatInputDate(endTime));
    		obs.setMode(mode);
        	
    	} catch(Exception  e) {
    		throw new IllegalArgumentException(e.getMessage());
    	} finally {
    		scanner.close();
    	} 
    	return obs;
    }
    
    private static String getLacModes() {
    	String s = "";
    	LacMode[] modes = LacMode.values();
    	for (int i = 0; i < modes.length; i++) {
    		s += modes[i].toString() + ", ";
		}
    	return s.substring(0, s.length()-2);
    }
    
    private static String getBackgroundSubtractionMethods() {
    	String s = "";
    	BackgroundSubtractionMethod[] methods = BackgroundSubtractionMethod.values();
    	for (int i = 0; i < methods.length; i++) {
    		s += methods[i].toString() + ", ";
		}
    	return s.substring(0, s.length()-2);
    }
    
    private static void printHelp(Options options) {
		HelpFormatter helpFormatter = new HelpFormatter();
    	helpFormatter.printHelp(TargetSpecExtractor.class.getCanonicalName(), options);
    }   
}