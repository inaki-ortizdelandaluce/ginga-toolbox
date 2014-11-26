package org.ginga.tools.spectra;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class LacSpecInputFileWriter {

	private final static Logger log = Logger.getLogger(LacSpecInputModel.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LacSpecInputModel model = new LacSpecInputModel();
			model.setHasBackground(false);
			model.setLacMode("MPC2");
			model.setPsFileName("gs2000+25.ps");
			model.setMinElevation(5.0);
			model.setMinRigidity(10.0);
			model.setRegionFileName("GS2000+25_REGION.DATA");
			model.setSpectralFileName("GS2000+25_SPEC.FILE");
			model.setMonitorFileName("MONI.SPEC");
			
			Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			Velocity.init();
		
			Template template = Velocity.getTemplate("org/ginga/tools/templates/lacspec_template.vm");

			// Create the context
			Context context = new VelocityContext();
			context.put("template", model);
		
			//File f = new File("/tmp/lacspec-2.input");
			StringWriter sw = new StringWriter();
			template.merge(context, sw);
			log.info("\n" + sw.toString());
			//log.info("lacspec input file " + f.getName() + " generated successfully");
		} catch (Exception e) {
			log.error("lacspec input file could not be generated",e);
		}
		
	}

}
