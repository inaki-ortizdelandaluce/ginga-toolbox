package org.ginga.tools.lacdump.dao;

import java.util.List;

import org.ginga.tools.lacdump.LacDumpEntity;

public interface LacDumpDao {

    public void save(LacDumpEntity entity) throws DaoException;

    public void saveList(List<LacDumpEntity> entityList) throws DaoException;

    public LacDumpEntity findById(long id) throws DaoException;

    public List<LacDumpEntity> findManyBySf(String sf) throws DaoException;

}