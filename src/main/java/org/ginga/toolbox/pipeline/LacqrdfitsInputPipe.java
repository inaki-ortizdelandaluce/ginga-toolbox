package org.ginga.toolbox.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.DataReductionEnv;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.gti.GtiFileWriter;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.toolbox.lacqrdfits.LacqrdfitsInputModel;
import org.ginga.toolbox.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacqrdfitsInputPipe extends
		AbstractPipe<LacdumpQuery, LacqrdfitsInputModel> implements
		TransformPipe<LacdumpQuery, LacqrdfitsInputModel> {

	private final static Logger log = Logger
			.getLogger(LacqrdfitsInputPipe.class);

	/*
	 * Receives a LacdumpQuery, creates a GTI/Region file and finally
	 * emits a LacqrdfitsInputModel referencing such file
	 */
	@Override
	protected LacqrdfitsInputModel processNextStart()
			throws NoSuchElementException {
		try {
			LacdumpQuery query = this.starts.next();

			// set working directory
			File workingDir = new File(GingaToolboxEnv.getInstance().getWorkingDir());
			if (!workingDir.exists()) {
				workingDir.mkdirs();
			}
			log.info("Working directory " + workingDir.getAbsolutePath());

			// build empty GTI file
			File gtiFile = new File(workingDir, FileUtil.nextFileName("REGION", query.getStartTime(), 
					query.getMode().toString(), "DATA"));

			// query entities matching the criteria
			LacdumpDao dao = new LacdumpDaoImpl();
			List<LacdumpSfEntity> sfList = dao.findSfList(query);
			log.info("Query executed successfully. " + sfList.size()
					+ " result(s) found");

			if (sfList.size() > 0) {
				// save matching results into a GTI file
				GtiFileWriter gtiWriter = new GtiFileWriter();
				gtiWriter.writeToFile(query.getTargetName(), sfList, false, gtiFile);
				log.debug("GTI file " + gtiFile.getPath()
						+ " written successfully");

				// emit lacqrdfits input model
				LacqrdfitsInputModel inputModel = new LacqrdfitsInputModel();
				DataReductionEnv env = GingaToolboxEnv.getInstance().getDataReductionEnv();
				inputModel.setLacMode(query.getMode());
				inputModel.setStartTime(query.getStartTime());
				inputModel.setMinElevation(env.getElevationMin());
				inputModel.setMaxElevation(env.getElevationMax());
				inputModel.setPsFileName(FileUtil.nextFileName("lacqrd",
						query.getStartTime(), query.getMode()
								.toString(), "ps"));
				inputModel.setRegionFileName(gtiFile.getName());
				inputModel.setSpectralFileName(FileUtil.nextFileName("SPEC",
						query.getStartTime(), query.getMode()
								.toString(), "FILE"));
				inputModel.setTimingFileName(FileUtil.nextFileName("TIMING",
						query.getStartTime(), query.getMode()
								.toString(), "fits"));

				return inputModel;
			}
		} catch (IOException | LacdumpDaoException e) {
			log.error("Error generating GTI file. Message= " + e.getMessage(),
					e);
		}
		return null;
	}
}
