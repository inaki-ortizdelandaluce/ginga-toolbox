package org.ginga.tools.lacdump.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.tools.lacdump.LacDumpQuery;
import org.ginga.tools.lacdump.LacDumpSfEntity;
import org.ginga.tools.lacdump.dao.LacDumpDao;
import org.ginga.tools.lacdump.dao.LacDumpDaoException;
import org.ginga.tools.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class LacDumpDaoImpl implements LacDumpDao {

    private static final Logger log = Logger.getLogger(LacDumpDaoImpl.class);
    public final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /*
     * (non-Javadoc)
     *
     * @see org.ginga.tools.lacdump.dao.LacDumpDao#save(org.ginga.tools.lacdump.LACDumpEntity)
     */
    @Override
    public void save(LacDumpSfEntity sf) throws LacDumpDaoException {
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            hibernateSession.saveOrUpdate(sf);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new LacDumpDaoException(e);
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
    public LacDumpSfEntity findById(long id) throws LacDumpDaoException {
        LacDumpSfEntity entity = null;
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            entity = (LacDumpSfEntity) hibernateSession.get(LacDumpSfEntity.class, id);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new LacDumpDaoException(e);
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
    public void saveList(List<LacDumpSfEntity> entityList) throws LacDumpDaoException {
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
            throw new LacDumpDaoException(e);
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
                    throws LacDumpDaoException {
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
            throw new LacDumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return sfList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LacDumpSfEntity> findSfList(String mode, String target, String startTime,
            String endTime, double elevation, double rigidity) throws LacDumpDaoException {
        List<LacDumpSfEntity> sfList = null;
        try {
            String hql = "FROM LacDumpSfEntity WHERE MODE =:mode and TARGET like :target and "
                    + "DATE >=:start and DATE <= :end and EELV > :eelv and RIG >= :rig ORDER BY ID";

            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            Query query = hibernateSession.createQuery(hql);
            query.setString("mode", mode);
            query.setString("target", "%" + target + "%");
            query.setString("start", startTime);
            query.setString("end", endTime);
            query.setDouble("eelv", elevation);
            query.setDouble("rig", rigidity);

            sfList = query.list();
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new LacDumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return sfList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ginga.tools.lacdump.dao.LacDumpDao#findSfList(org.ginga.tools.lacdump.LacDumpQuery)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LacDumpSfEntity> findSfList(LacDumpQuery query) throws LacDumpDaoException {
        List<LacDumpSfEntity> sfList = null;
        try {
            String hql = "FROM LacDumpSfEntity WHERE";
            String startTime = null, endTime = null, mode = null, bitRate = null, target = null;
            double rigidity = 0, elevation = 0;

            if ((bitRate = query.getBitRate()) != null) {
                hql += " BR =:br and";
            }
            if ((mode = query.getMode()) != null) {
                hql += " MODE =:mode and";
            }
            if ((target = query.getTargetName()) != null) {
                hql += " TARGET like :target and";
            }
            if ((startTime = query.getStartTime()) != null) {
                hql += " DATE >=:start and";
            }
            if ((endTime = query.getEndTime()) != null) {
                hql += " DATE <=:end and";
            }
            if ((elevation = query.getMinElevation()) > 0) {
                hql += "  EELV > :eelv and";
            }
            if ((rigidity = query.getMinRigidity()) > 0) {
                hql += "  RIG >= :rig and";
            }
            // remove last and
            hql = hql.substring(0, hql.lastIndexOf("and"));

            hql += " ORDER BY ID";

            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            Query hquery = hibernateSession.createQuery(hql);
            if (bitRate != null) {
                hquery.setString("br", bitRate);
            }
            if (mode != null) {
                hquery.setString("mode", mode);
            }
            if (target != null) {
                hquery.setString("target", "%" + target + "%");
            }
            if (startTime != null) {
                hquery.setString("start", startTime);
            }
            if (endTime != null) {
                hquery.setString("end", endTime);
            }
            if (elevation > 0) {
                hquery.setDouble("eelv", elevation);
            }
            if (rigidity > 0) {
                hquery.setDouble("rig", rigidity);
            }

            sfList = hquery.list();
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new LacDumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return sfList;
    }

}