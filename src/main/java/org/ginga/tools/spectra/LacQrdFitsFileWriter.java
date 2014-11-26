package org.ginga.tools.spectra;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class LacQrdFitsFileWriter {

    private final static Logger log = Logger.getLogger(LacQrdFitsInputModel.class);

    private LacQrdFitsInputModel inputModel;
    private Template template;

    public LacQrdFitsFileWriter(LacQrdFitsInputModel model) {
        this.inputModel = model;
        // init velocity
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        Velocity.init();
        this.template = Velocity.getTemplate("org/ginga/tools/templates/lacqrdfits_template.vm");
    }

    public void writeToFile(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        // create the context
        Context context = new VelocityContext();
        context.put("template", this.inputModel);
        // merge context and template
        this.template.merge(context, writer);
        // flush/close writer
        try {
            writer.flush();
            log.debug("\n" + writer.toString());
        } catch (IOException e) {
            log.warn("Error flushing writer stream");
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                log.warn("Error closing writer stream");
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            LacQrdFitsInputModel model = new LacQrdFitsInputModel();
            model.setLacMode("MPC2");
            model.setPsFileName("gs2000+25_lacqrd.ps");
            model.setMinElevation(5.0);
            model.setRegionFileName("GS2000+25_REGION.DATA");
            model.setSpectralFileName("GS2000+25_SPEC_lacqrd.FILE");
            model.setTimingFileName("GS2000+25_TIMING.fits");
            
            LacQrdFitsFileWriter writer = new LacQrdFitsFileWriter(model);
            writer.writeToFile(new File("/tmp/lacqrd.input"));
            log.info("Input file /tmp/lacqrd.input saved successfully");
        } catch (Exception e) {
            log.error("lacspec input file could not be generated", e);
        }

    }
}
