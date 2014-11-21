package org.ginga.tools.lacdump.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LacDumpEntity;
import org.ginga.tools.lacdump.LacDumpEntityList;
import org.ginga.tools.lacdump.dao.DaoException;
import org.ginga.tools.lacdump.dao.LacDumpDao;
import org.hibernate.Query;
import org.hibernate.Session;

public class LacDumpDaoImpl implements LacDumpDao {

    private static final Logger log = Logger.getLogger(LacDumpDaoImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.ginga.tools.lacdump.dao.LACDumpDao#save(org.ginga.tools.lacdump.LACDumpEntity)
     */
    @Override
    public void save(LacDumpEntity entity) throws DaoException {
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            hibernateSession.saveOrUpdate(entity);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ginga.tools.lacdump.dao.LACDumpDao#findById(long)
     */
    @Override
    public LacDumpEntity findById(long id) throws DaoException {
        LacDumpEntity entity = null;
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            entity = (LacDumpEntity) hibernateSession.get(LacDumpEntity.class, id);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ginga.tools.lacdump.dao.LACDumpDao#findMany(org.hibernate.Query)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LacDumpEntity> findMany(Query query) throws DaoException {
        List<LacDumpEntity> entityList = null;
        try {
            HibernateUtil.beginTransaction();
            entityList = query.list();
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return entityList;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ginga.tools.lacdump.dao.LACDumpDao#saveList(org.ginga.tools.lacdump.LACDumpEntityList)
     */
    @Override
    public void saveList(LacDumpEntityList entityList) throws DaoException {
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            LacDumpEntity entity = null;
            for (Iterator<LacDumpEntity> iterator = entityList.iterator(); iterator.hasNext();) {
                entity = iterator.next();
                hibernateSession.saveOrUpdate(entity);
                log.debug(entity.getSuperFrame() + ":" + entity.getSequenceNumber()
                        + " stored into the database successfully");
            }
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

}