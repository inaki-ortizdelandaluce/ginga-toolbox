package org.ginga.tools.lacdump.dao;

import java.util.List;

import org.ginga.tools.lacdump.LACDumpEntity;
import org.hibernate.Query;

public interface LACDumpDao {

    public void save(LACDumpEntity entity) throws DaoException;

    public LACDumpEntity findById(long id) throws DaoException;

    public List<LACDumpEntity> findMany(Query query) throws DaoException;

}