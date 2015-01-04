package org.ginga.toolbox.lacspec;

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
import org.ginga.toolbox.util.Constants.BgSubtractionMethod;
import org.ginga.toolbox.util.Constants.BitRate;
import org.ginga.toolbox.util.Constants.LacMode;

public class LacspecInputFileWriter {

    private final static Logger log = Logger.getLogger(LacspecInputModel.class);

    private LacspecInputModel inputModel;
    private Template template;

    public LacspecInputFileWriter(LacspecInputModel model) {
        this.inputModel = model;
        // init velocity
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        Velocity.init();
        this.template = Velocity.getTemplate("org/ginga/toolbox/lacspec/template/lacspec_template.vm");

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
            LacspecInputModel model = new LacspecInputModel();
            model.setHasBackground(true);
            model.setBgMethod(BgSubtractionMethod.SIMPLE);
            model.setBgFileName("GS2000+25_BGS_MONI1.SPEC");
            model.setBgSubFileNumber(1);
            model.setLacMode(LacMode.MPC2);
            model.setBitRate(BitRate.ANY);
            model.setPsFileName("gs2000+25_lacspec.ps");
            model.setMinElevation(5.0);
            model.setMaxElevation(180.0);
            model.setMinRigidity(10.0);
            model.setMaxRigidity(20.0);
            model.setBgCorrection(0);
            model.setAspectCorrection(0);
            model.setDeadTimeCorrection(1);
            model.setChannelToEnergy(1);
            model.setDataUnit(1);
            model.setAce(1);
            model.setCounter1(3);
            model.setCounter2(3);
            model.setCounter3(3);
            model.setCounter4(3);
            model.setCounter5(3);
            model.setCounter6(3);
            model.setCounter7(3);
            model.setCounter8(3);
            model.setMixedMode(1);
            model.setRegionFileName("GS2000+25_REGION.DATA");
            model.setSpectralFileName("GS2000+25_SPEC_lacspec.FILE");
            model.setMonitorFileName("MONI.SPEC");

            LacspecInputFileWriter writer = new LacspecInputFileWriter(model);
            writer.writeToFile(new File("/tmp/lacspec.input"));
            log.info("Input file /tmp/lacspec.input saved successfully");
        } catch (Exception e) {
            log.error("lacspec input file could not be generated", e);
        }

    }
}
