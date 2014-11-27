package org.ginga.tools.obslog.dao;

import java.util.List;

import org.ginga.tools.obslog.ObslogEntity;

public interface ObslogDao {

    public List<ObslogEntity> findListByTarget(String target) throws ObslogDaoException;

}