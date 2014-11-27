package org.ginga.tools.pipeline;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.gti.GtiFileWriter;
import org.ginga.tools.lacdump.LacDumpQuery;
import org.ginga.tools.lacdump.LacDumpSfEntity;
import org.ginga.tools.lacdump.dao.LacDumpDao;
import org.ginga.tools.lacdump.dao.impl.LacDumpDaoImpl;
import org.ginga.tools.runtime.GingaToolsEnvironment;
import org.ginga.tools.util.FileUtil;

import com.tinkerpop.pipes.PipeFunction;

public class GoodTimeIntervalPipeFunction implements PipeFunction<LacDumpQuery, File> {

    private final static Logger log = Logger.getLogger(GoodTimeIntervalPipeFunction.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.tinkerpop.pipes.PipeFunction#compute(java.lang.Object)
     */
    @Override
    public File compute(LacDumpQuery query) {
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
            LacDumpDao dao = new LacDumpDaoImpl();
            List<LacDumpSfEntity> sfList = dao.findSfList(query);
            log.info("Query executed successfully. " + sfList.size() + " result(s) found");

            // save matching results into a GTI file
            GtiFileWriter gtiWriter = new GtiFileWriter();
            gtiWriter.writeToFile(target, sfList, false, gtiFile);
            log.debug("GTI file " + gtiFile.getPath() + " written successfully");
            return gtiFile;
        } catch (Exception e) {
            log.error("Error generating GTI file ", e);
        }
        return null;
    }

}
