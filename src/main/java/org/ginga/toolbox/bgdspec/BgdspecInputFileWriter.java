package org.ginga.toolbox.bgdspec;

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

public class BgdspecInputFileWriter {

    private final static Logger log = Logger.getLogger(BgdspecInputModel.class);

    private BgdspecInputModel inputModel;
    private Template template;

    public BgdspecInputFileWriter(BgdspecInputModel model) {
        this.inputModel = model;
        // init velocity
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        Velocity.init();
        this.template = Velocity
                .getTemplate("org/ginga/toolbox/bgdspec/template/bgdspec_template.vm");

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
}