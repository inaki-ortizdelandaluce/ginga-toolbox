package org.ginga.toolbox.observation.dao;

import java.util.List;

import org.ginga.toolbox.observation.ObservationEntity;

public interface ObservationDao {

    public List<ObservationEntity> findListByTarget(String target) throws ObservationDaoException;

}