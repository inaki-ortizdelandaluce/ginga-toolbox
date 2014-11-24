package org.ginga.tools.obslog.dao;

import java.util.List;

import org.ginga.tools.obslog.ObsLogEntity;

public interface ObsLogDao {

    public List<ObsLogEntity> findListByTarget(String target) throws ObsLogDaoException;

}