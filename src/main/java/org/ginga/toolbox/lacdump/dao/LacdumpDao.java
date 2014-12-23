package org.ginga.toolbox.lacdump.dao;

import java.util.List;

import org.ginga.toolbox.lacdump.LacdumpConstraints;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;

public interface LacdumpDao {

    public void save(LacdumpSfEntity sf) throws LacdumpDaoException;

    public void saveList(List<LacdumpSfEntity> sfList) throws LacdumpDaoException;

    public LacdumpSfEntity findById(long id) throws LacdumpDaoException;

    public List<LacdumpSfEntity> findSfList(String bitRate, String mode, String target,
            String startTime, String endTime, double elevation, double rigidity)
            throws LacdumpDaoException;

    public List<LacdumpSfEntity> findSfList(String mode, String target, String startTime,
            String endTime, double elevation, double rigidity) throws LacdumpDaoException;

    public List<LacdumpSfEntity> findSfList(LacdumpConstraints constraints)
            throws LacdumpDaoException;

    public List<String> findModes(String target, String startTime, String endTime,
            double elevation, double rigidity) throws LacdumpDaoException;
}