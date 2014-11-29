package org.ginga.tools.pipeline;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.ginga.tools.obslog.ObslogEntity;
import org.ginga.tools.obslog.dao.ObslogDao;
import org.ginga.tools.obslog.dao.ObslogDaoException;
import org.ginga.tools.obslog.dao.impl.ObslogDaoImpl;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.transform.TransformPipe;

public class TargetObslogLookupPipe extends AbstractPipe<String, List<ObslogEntity>> 
	implements TransformPipe<String, List<ObslogEntity>> {

	private static Logger log = Logger.getLogger(TargetObslogLookupPipe.class);
	
	@Override
	protected List<ObslogEntity> processNextStart()
			throws NoSuchElementException {
		if(this.starts.hasNext()) {
			String target = this.starts.next();
			// find observation list by target
	        ObslogDao obsLogDao = new ObslogDaoImpl();
	        List<ObslogEntity> obsList = null;
	        try {
	            obsList = obsLogDao.findListByTarget(target);
	            log.info(obsList.size() + " " + target + " observation(s) found");
	        } catch (ObslogDaoException e) {
	            log.error(target + " observation(s) not found", e);
	        }
	        return obsList;
		} else {
			throw new NoSuchElementException();
		}
	}

}
