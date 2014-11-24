package org.ginga.tools.lacdump.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LacDumpSfEntity;
import org.ginga.tools.lacdump.dao.DaoException;
import org.ginga.tools.lacdump.dao.LacDumpDao;
import org.ginga.tools.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class LacDumpDaoImpl implements LacDumpDao {

    private static final Logger log = Logger.getLogger(LacDumpDaoImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.ginga.tools.lacdump.dao.LacDumpDao#save(org.ginga.tools.lacdump.LACDumpEntity)
     */
    @Override
    public void save(LacDumpSfEntity sf) throws DaoException {
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            hibernateSession.saveOrUpdate(sf);
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
     * @see org.ginga.tools.lacdump.dao.LacDumpDao#findById(long)
     */
    @Override
    public LacDumpSfEntity findById(long id) throws DaoException {
        LacDumpSfEntity entity = null;
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            entity = (LacDumpSfEntity) hibernateSession.get(LacDumpSfEntity.class, id);
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
     * @see org.ginga.tools.lacdump.dao.LACDumpDao#saveList(lava.util.List<LacDumpSfEntity>)
     */
    @Override
    public void saveList(List<LacDumpSfEntity> entityList) throws DaoException {
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            for (LacDumpSfEntity entity : entityList) {
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

    /*
     * (non-Javadoc)
     * 
     * @see org.ginga.tools.lacdump.dao.LacDumpDao#findSfList(java.lang.String, java.lang.String,
     * java.lang.String, java.util.Date, java.util.Date, double, double)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LacDumpSfEntity> findSfList(String bitRate, String mode, String target,
            String startTime, String endTime, double elevation, double rigidity)
            throws DaoException {
        List<LacDumpSfEntity> sfList = null;
        try {
            String hql = "FROM LacDumpSfEntity WHERE BR =:br and MODE =:mode and TARGET like :target and "
                    + "DATE >=:start and DATE <= :end and EELV > :eelv and RIG >= :rig ORDER BY ID";

            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            Query query = hibernateSession.createQuery(hql);
            query.setString("br", bitRate);
            query.setString("mode", mode);
            query.setString("target", "%" + target + "%");
            query.setString("start", startTime);
            query.setString("end", endTime);
            query.setDouble("eelv", elevation);
            query.setDouble("rig", rigidity);

            sfList = query.list();
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return sfList;
    }

}