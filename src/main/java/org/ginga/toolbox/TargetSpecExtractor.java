package org.ginga.toolbox;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.TargetObservationSingleMode;
import org.ginga.toolbox.pipeline.SpectrumHayashidaPipeline;
import org.ginga.toolbox.pipeline.TargetObservationListPipe;
import org.ginga.toolbox.util.Constants.BackgroundSubractionMethod;

import com.tinkerpop.pipes.Pipe;

public class TargetSpecExtractor {

    private final static Logger log = Logger.getLogger(TargetSpecExtractor.class);

    public TargetSpecExtractor() {
    }

    public void extractAllSpectra(String target, BackgroundSubractionMethod method) {
        // find all observations for input target
        Pipe<String, List<ObservationEntity>> scanner = new TargetObservationListPipe();
        scanner.setStarts(Arrays.asList(target));
        // extract spectra for all observations
    	switch(method) {
    	case HAYASHIDA:
    		extractAllSpectraHayashida(scanner.next());
    	case SIMPLE:
    	case SUD_SORT:
    	default:
    		log.error(method + " background subtraction method not supported");
    		System.exit(1);
    	}
    }
    
    public void extractSingleSpectrum(BackgroundSubractionMethod method) {
    	switch(method) {
    	case HAYASHIDA:
    		// TODO Scanner to build target observation single mode
    		extractSingleSpectrumHayashida(null);
    	case SIMPLE:
    	case SUD_SORT:
    	default:
    		log.error(method + " background subtraction method not supported");
    		System.exit(1);
    	}
    }

    private void extractAllSpectraHayashida(List<ObservationEntity> obsList) {
        SpectrumHayashidaPipeline specHayashidaPipeline = new SpectrumHayashidaPipeline();
        List<TargetObservationSingleMode> singleModeList = null;
        for (ObservationEntity obsEntity : obsList) {
            log.info("Processing observation " + obsEntity.getSequenceNumber() + "...");
            singleModeList = obsEntity.getSingleModeList();
            // extract spectrum for all relevant modes
            if (singleModeList != null) {
                // run pipeline
                specHayashidaPipeline.run(singleModeList);
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

    private void extractSingleSpectrumHayashida(TargetObservationSingleMode singleMode) {
        if (singleMode != null) {
        	// run pipeline
        	SpectrumHayashidaPipeline specHayashidaPipeline = new SpectrumHayashidaPipeline();
            specHayashidaPipeline.run(Arrays.asList(singleMode));
            File file = specHayashidaPipeline.next();
            if (file != null) {
            	log.info("Spectrum file " + file.getName() + " created successfully");
            }
        }
    }
    
    private static String getBackgroundSubtractionMethods() {
    	String s = "";
    	BackgroundSubractionMethod[] methods = BackgroundSubractionMethod.values();
    	for (int i = 0; i < methods.length; i++) {
    		s += " " + methods[i].toString() + ",";
		}
    	return s.substring(0, s.length()-1);
    }
    
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage org.ginga.toolbox.TargetSpecExtractor <target> <background subtraction method>");
            System.out.println("\t Background subtraction methods: " + getBackgroundSubtractionMethods());
            System.exit(1);
        }
        TargetSpecExtractor extractor = new TargetSpecExtractor();
        try {
        	BackgroundSubractionMethod method = Enum.valueOf(BackgroundSubractionMethod.class, args[1]);
        	extractor.extractAllSpectra(args[0], method);
        } catch (IllegalArgumentException e) {
        	log.error("Unknown background subtraction method " + args[1]);
        }
    }
}
