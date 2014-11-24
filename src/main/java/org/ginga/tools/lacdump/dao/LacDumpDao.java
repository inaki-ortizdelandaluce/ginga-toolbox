package org.ginga.tools.lacdump.dao;

import java.util.List;

import org.ginga.tools.lacdump.LacDumpSfEntity;

public interface LacDumpDao {

    public void save(LacDumpSfEntity sf) throws LacDumpDaoException;

    public void saveList(List<LacDumpSfEntity> sfList) throws LacDumpDaoException;

    public LacDumpSfEntity findById(long id) throws LacDumpDaoException;

    public List<LacDumpSfEntity> findSfList(String bitRate, String mode, String target,
            String startTime, String endTime, double elevation, double rigidity)
            throws LacDumpDaoException;

}