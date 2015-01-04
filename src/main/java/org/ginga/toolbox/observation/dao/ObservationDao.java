package org.ginga.toolbox.observation.dao;

import java.util.List;
import java.util.Set;

import org.ginga.toolbox.observation.ObservationEntity;

public interface ObservationDao {

    public List<ObservationEntity> findListByTarget(String target) throws ObservationDaoException;
    
    public List<ObservationEntity> findAll() throws ObservationDaoException;
    
    public Set<String> findAllTargets() throws ObservationDaoException;
    
    public void update(ObservationEntity observation) throws ObservationDaoException;
    
    public ObservationEntity findById(long id) throws ObservationDaoException;

}