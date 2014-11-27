package org.ginga.tools.pipeline;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.gti.GtiFileWriter;
import org.ginga.tools.lacdump.LacdumpQuery;
import org.ginga.tools.lacdump.LacdumpSfEntity;
import org.ginga.tools.lacdump.dao.LacdumpDao;
import org.ginga.tools.lacdump.dao.impl.LacdumpDaoImpl;
import org.ginga.tools.runtime.GingaToolsEnvironment;
import org.ginga.tools.spectrum.LacqrdfitsInputModel;
import org.ginga.tools.util.FileUtil;

import com.tinkerpop.pipes.PipeFunction;

public class LacqrdfitsInputPipe implements PipeFunction<LacdumpQuery, LacqrdfitsInputModel> {

    private final static Logger log = Logger.getLogger(LacqrdfitsInputPipe.class);

    /*
     * Receives a LacDumpQuery, creates a GTI/Region file and finally emits a 
     * LacQrdFitsInputModel referencing such file
     * @see com.tinkerpop.pipes.PipeFunction#compute(java.lang.Object)
     */
    @Override
    public LacqrdfitsInputModel compute(LacdumpQuery query) {
        try {
            // set working directory
            GingaToolsEnvironment gingaEnv = GingaToolsEnvironment.getInstance();
            File workingDir = new File(gingaEnv.getGingaWrkDir());
            log.info("Working directory " + workingDir.getAbsolutePath());

            String target = query.getTargetName();
            // set output file name
            File gtiFile = new File(workingDir, FileUtil.nextFileName(workingDir,
                    query.getTargetName() + "_REGION", "DATA"));

            // query entities matching the criteria
            LacdumpDao dao = new LacdumpDaoImpl();
            List<LacdumpSfEntity> sfList = dao.findSfList(query);
            log.info("Query executed successfully. " + sfList.size() + " result(s) found");

            // save matching results into a GTI file
            GtiFileWriter gtiWriter = new GtiFileWriter();
            gtiWriter.writeToFile(target, sfList, false, gtiFile);
            log.debug("GTI file " + gtiFile.getPath() + " written successfully");
            
            LacqrdfitsInputModel inputModel = new LacqrdfitsInputModel();
            return inputModel;
        } catch (Exception e) {
            log.error("Error generating GTI file ", e);
        }
        return null;
    }

}
