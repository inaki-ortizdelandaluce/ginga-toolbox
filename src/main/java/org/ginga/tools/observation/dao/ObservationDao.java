package org.ginga.tools.observation.dao;

import java.util.List;

import org.ginga.tools.observation.ObservationEntity;

public interface ObservationDao {

    public List<ObservationEntity> findListByTarget(String target) throws ObservationDaoException;

}