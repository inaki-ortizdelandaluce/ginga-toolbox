package org.ginga.toolbox.command;

import java.io.File;
import java.util.Arrays;
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
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.pipeline.SpectrumHayashidaPipeline;
import org.ginga.toolbox.pipeline.TargetObservationListPipe;
import org.ginga.toolbox.util.Constants.BackgroundSubtractionMethod;

import com.tinkerpop.pipes.Pipe;

public class SpectraExtractorCmd {

    private final static Logger log = Logger.getLogger(SpectraExtractorCmd.class);

    public static void main(String[] args) {
		try {
			CommandLine commandLine = new BasicParser().parse(getOptions(), args);
			// read command line argument values 
	        String target = commandLine.getOptionValue("t");
			BackgroundSubtractionMethod method = null;
			try {
				method = Enum.valueOf(BackgroundSubtractionMethod.class,
					commandLine.getOptionValue("b"));
	    	} catch (IllegalArgumentException e) {
	    		log.error("Unknown background subtraction method " 
	    				+ commandLine.getOptionValue("b"));
	    		printHelp();
	    		System.exit(1);
	    	}
			// extract spectra
			extractSpectra(target, method);
		} catch (ParseException e) {
			log.error(e.getMessage());
			printHelp();
		} 
    }
	
	@SuppressWarnings("static-access")
	private static Options getOptions() {
    	Options options = new Options();
    	Option targetOption = OptionBuilder.withArgName("target")
    			.withLongOpt("target")
    			.isRequired(true)
    			.withDescription("Target Name")
    			.hasArg()
    			.create("t");
    	Option methodOption = OptionBuilder.withArgName("method")
    			.withLongOpt("background-method")
    			.isRequired(true)
    			.withDescription("Background subtraction method. Possible values: " + getBackgroundSubtractionMethods())
    			.hasArg()
    			.create("b");
    	options.addOption(targetOption);
    	options.addOption(methodOption);
    	return options;
	}
	
	private static String getBackgroundSubtractionMethods() {
    	String s = "";
    	BackgroundSubtractionMethod[] methods = BackgroundSubtractionMethod.values();
    	for (int i = 0; i < methods.length; i++) {
    		s += methods[i].toString() + ", ";
		}
    	return s.substring(0, s.length()-2);
    }

	private static void printHelp() {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.setOptionComparator(new Comparator<Option>() {
			private static final String OPTS_ORDER = "tb"; // short option names
			
		    @Override
			public int compare(Option o1, Option o2) {
		    	return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
			}
		});
		helpFormatter.printHelp(SpectraExtractorCmd.class.getCanonicalName(), getOptions());
    }   
	
	public static void extractSpectra(String target, BackgroundSubtractionMethod method) {
		switch(method) {
		case HAYASHIDA:
			extractSpectraHayashida(target);
		case SIMPLE:
		case SUD_SORT:
		default:
			log.error(method + " background subtraction method not supported");
			System.exit(1);
		}
	}
	
    public static void extractSpectraHayashida(String target) {
        // find all observations for input target
        Pipe<String, List<ObservationEntity>> obsListPipe = new TargetObservationListPipe();
        obsListPipe.setStarts(Arrays.asList(target));
        List<ObservationEntity> obsList = obsListPipe.next();
        // find available modes for each observations and extract spectra
    	List<SingleModeTargetObservation> singleModeObsList = null;
        for (ObservationEntity obsEntity : obsList) {
            log.info("Processing observation " + obsEntity.getSequenceNumber() + "...");
            singleModeObsList = obsEntity.getSingleModeObsList();
            // extract spectra for all relevant modes
            if (singleModeObsList != null) {
                // run pipeline
            	SpectrumHayashidaPipeline specHayashidaPipeline = new SpectrumHayashidaPipeline();
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

}