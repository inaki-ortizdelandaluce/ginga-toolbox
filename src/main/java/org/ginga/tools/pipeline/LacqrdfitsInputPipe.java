package org.ginga.tools.pipeline;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.tools.gti.GtiFileWriter;
import org.ginga.tools.lacdump.LacdumpQuery;
import org.ginga.tools.lacdump.LacdumpSfEntity;
import org.ginga.tools.lacdump.dao.LacdumpDao;
import org.ginga.tools.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.tools.runtime.GingaToolsEnvironment;
import org.ginga.tools.spectrum.LacqrdfitsInputModel;
import org.ginga.tools.util.FileUtil;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class LacqrdfitsInputPipe extends
		AbstractPipe<LacdumpQuery, LacqrdfitsInputModel> implements
		TransformPipe<LacdumpQuery, LacqrdfitsInputModel> {

	private final static Logger log = Logger
			.getLogger(LacqrdfitsInputPipe.class);

	/*
	 * Receives a LacdumpQuery, creates a GTI/Region file and finally emits a
	 * LacqrdfitsInputModel referencing such file
	 */
	@Override
	protected LacqrdfitsInputModel processNextStart()
			throws NoSuchElementException {
		try {
			if (this.starts.hasNext()) {
				LacdumpQuery query = this.starts.next();

				// set working directory
				GingaToolsEnvironment gingaEnv = GingaToolsEnvironment
						.getInstance();
				File workingDir = new File(gingaEnv.getGingaWrkDir());
				log.info("Working directory " + workingDir.getAbsolutePath());

				String target = query.getTargetName();
				// set output file name
				File gtiFile = new File(workingDir, FileUtil.nextFileName(
						workingDir, target + "_REGION", "DATA"));

				// query entities matching the criteria
				LacdumpDao dao = new LacdumpDaoImpl();
				List<LacdumpSfEntity> sfList = dao.findSfList(query);
				log.info("Query executed successfully. " + sfList.size()
						+ " result(s) found");

				// save matching results into a GTI file
				GtiFileWriter gtiWriter = new GtiFileWriter();
				gtiWriter.writeToFile(target, sfList, false, gtiFile);
				log.debug("GTI file " + gtiFile.getPath()
						+ " written successfully");

				// emit lacqrdfits input model
				LacqrdfitsInputModel inputModel = new LacqrdfitsInputModel();
				// TODO populate lacqrdfits input model
				inputModel.setLacMode(query.getMode());
				inputModel.setMinElevation(query.getMinElevation());
				inputModel.setPsFileName(FileUtil.nextFileName(workingDir,
						target + "_lacqrd", "ps"));
				inputModel.setRegionFileName(gtiFile.getName());
				inputModel.setSpectralFileName(FileUtil.nextFileName(
						workingDir, target + "_SPEC_lacqrd", "FILE"));
				inputModel.setTimingFileName(FileUtil.nextFileName(workingDir,
						target + "_TIMING", "fits"));

				return inputModel;
			} else {
				throw new NoSuchElementException();
			}
		} catch (Exception e) {
			log.error("Error generating GTI file ", e);
		}
		return null;
	}
}
