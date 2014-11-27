package org.ginga.tools.lacdump.dao;

import java.util.List;

import org.ginga.tools.lacdump.LacdumpQuery;
import org.ginga.tools.lacdump.LacdumpSfEntity;

public interface LacdumpDao {

    public void save(LacdumpSfEntity sf) throws LacdumpDaoException;

    public void saveList(List<LacdumpSfEntity> sfList) throws LacdumpDaoException;

    public LacdumpSfEntity findById(long id) throws LacdumpDaoException;

    public List<LacdumpSfEntity> findSfList(String bitRate, String mode, String target,
            String startTime, String endTime, double elevation, double rigidity)
                    throws LacdumpDaoException;

    public List<LacdumpSfEntity> findSfList(String mode, String target, String startTime,
            String endTime, double elevation, double rigidity) throws LacdumpDaoException;

    public List<LacdumpSfEntity> findSfList(LacdumpQuery query) throws LacdumpDaoException;
}