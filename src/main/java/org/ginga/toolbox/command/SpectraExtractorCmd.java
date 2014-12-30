package org.ginga.toolbox.command;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.SingleModeTargetObservation;
import org.ginga.toolbox.pipeline.SpectrumHayashidaPipeline;
import org.ginga.toolbox.pipeline.TargetObservationListPipe;
import org.ginga.toolbox.util.Constants.BackgroundSubtractionMethod;

import com.tinkerpop.pipes.Pipe;

public class SpectraExtractorCmd extends Command {

    private final static Logger log = Logger.getLogger(SpectraExtractorCmd.class);
   
    public SpectraExtractorCmd(String[] args) {
    	super(args);
    }
    
	@SuppressWarnings("static-access")
	@Override
	public Options getOptions() {
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
	
	@Override
	public void execute() throws IllegalArgumentException {
		// read required command line argument values 
        String target = super.getOptionValue("t");
		BackgroundSubtractionMethod method = null;
		try {
			method = Enum.valueOf(BackgroundSubtractionMethod.class,
				super.getOptionValue("b"));
    	} catch (IllegalArgumentException e) {
    		log.error("Unknown background subtraction method " 
    				+ super.getOptionValue("b"));
    		printHelp();
    		System.exit(1);
    	}
		extractSpectra(target, method);
    }
    
	private void extractSpectra(String target, BackgroundSubtractionMethod method) {
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
	
    private void extractSpectraHayashida(String target) {
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

	private String getBackgroundSubtractionMethods() {
    	String s = "";
    	BackgroundSubtractionMethod[] methods = BackgroundSubtractionMethod.values();
    	for (int i = 0; i < methods.length; i++) {
    		s += methods[i].toString() + ", ";
		}
    	return s.substring(0, s.length()-2);
    }

	public static void main(String[] args) {
		try {
			Command cmd = new SpectraExtractorCmd(args);
			cmd.execute();
		} catch (IllegalArgumentException e) {
			// ignore
		} catch (CommandExecutionException e) {
			log.error(e.getMessage(),e);
		}
    }
}