package org.ginga.toolbox.lacdump.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.ginga.toolbox.environment.GingaToolboxEnv;
import org.ginga.toolbox.lacdump.LacdumpQuery;
import org.ginga.toolbox.lacdump.LacdumpQuery.SkyAnnulus;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.LacdumpDaoException;
import org.ginga.toolbox.util.Constants.BitRate;
import org.ginga.toolbox.util.Constants.LacMode;
import org.ginga.toolbox.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class LacdumpDaoImpl implements LacdumpDao {

    private static final Logger log = Logger.getLogger(LacdumpDaoImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see org.ginga.toolbox.lacdump.dao.LacDumpDao#save(org.ginga.toolbox.lacdump. LACDumpEntity)
     */
    @Override
    public void save(LacdumpSfEntity sf) throws LacdumpDaoException {
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            hibernateSession.saveOrUpdate(sf);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new LacdumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ginga.toolbox.lacdump.dao.LacDumpDao#findById(long)
     */
    @Override
    public LacdumpSfEntity findById(long id) throws LacdumpDaoException {
        LacdumpSfEntity entity = null;
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            entity = (LacdumpSfEntity) hibernateSession.get(LacdumpSfEntity.class, id);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new LacdumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return entity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ginga.toolbox.lacdump.dao.LACDumpDao#saveList(lava.util.List< LacDumpSfEntity>)
     */
    @Override
    public void saveList(List<LacdumpSfEntity> entityList) throws LacdumpDaoException {
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            for (LacdumpSfEntity entity : entityList) {
                hibernateSession.saveOrUpdate(entity);
                log.debug(entity.getSuperFrame() + ":" + entity.getSequenceNumber()
                        + " stored into the database successfully");
            }
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new LacdumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ginga.toolbox.lacdump.dao.LacDumpDao#findSfList(java.lang.String, java.lang.String,
     * java.lang.String, java.util.Date, java.util.Date, double, double)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LacdumpSfEntity> findSfList(String bitRate, String mode, String target,
            String startTime, String endTime, double elevation, double rigidity)
                    throws LacdumpDaoException {
        List<LacdumpSfEntity> sfList = null;
        try {
            String hql = null;
            if (GingaToolboxEnv.isPostgreSQLDatabase()) {
                hql = "FROM " + LacdumpSfEntity.class.getSimpleName()
                        + " WHERE BR =:br and MODE =:mode and TARGET like :target and "
                        + "DATE >= to_timestamp(:start,'yyyy-mm-dd hh24:mi:ss') and "
                        + "DATE <= to_timestamp(:end,'yyyy-mm-dd hh24:mi:ss') and "
                        + "EELV > :eelv and RIG >= :rig ORDER BY ID";
            } else {
                hql = "FROM "
                        + LacdumpSfEntity.class.getSimpleName()
                        + " WHERE BR =:br and MODE =:mode and TARGET like :target and "
                        + "DATE >=:start and DATE <= :end and EELV > :eelv and RIG >= :rig ORDER BY ID";
            }
            log.debug("HQL=" + hql);

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
            throw new LacdumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return sfList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LacdumpSfEntity> findSfList(String mode, String target, String startTime,
            String endTime, double elevation, double rigidity) throws LacdumpDaoException {
        List<LacdumpSfEntity> sfList = null;
        try {
            String hql = null;
            if (GingaToolboxEnv.isPostgreSQLDatabase()) {
                hql = "FROM " + LacdumpSfEntity.class.getSimpleName()
                        + " WHERE MODE =:mode and TARGET like :target and "
                        + "DATE >= to_timestamp(:start,'yyyy-mm-dd hh24:mi:ss') and "
                        + "DATE <= to_timestamp(:end,'yyyy-mm-dd hh24:mi:ss') and "
                        + "EELV > :eelv and RIG >= :rig ORDER BY ID";
            } else {
                hql = "FROM "
                        + LacdumpSfEntity.class.getSimpleName()
                        + " WHERE MODE =:mode and TARGET like :target and "
                        + "DATE >= :start and DATE <= :end and EELV > :eelv and RIG >= :rig ORDER BY ID";
            }
            log.debug("HQL=" + hql);

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
            throw new LacdumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return sfList;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ginga.toolbox.lacdump.dao.LacDumpDao#findSfList(org.ginga.toolbox.lacdump
     * .LacDumpQuery)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LacdumpSfEntity> findSfList(LacdumpQuery query) throws LacdumpDaoException {
        List<LacdumpSfEntity> sfList = null;
        String startTime = null;
        String endTime = null;
        String target = null;
        Double minRigidity = null;
        Double minElevation = null;
        LacMode mode = null;
        BitRate bitRate = null;
        List<String> lacdumpFiles = null;
        SkyAnnulus skyAnnulus = null;

        try {
            String hql = "FROM " + LacdumpSfEntity.class.getSimpleName() + " WHERE";

            if ((bitRate = query.getBitRate()) != null && !bitRate.equals(BitRate.ANY)) {
                hql += " BR =:br and";
            }
            if ((mode = query.getMode()) != null) {
                hql += " MODE =:mode and";
            }
            if ((target = query.getTargetName()) != null && !query.isBackground()) {
                hql += " TARGET like :target and";
            } else {
                hql += " TARGET is NULL and";
            }
            if ((startTime = query.getStartTime()) != null) {
                if (GingaToolboxEnv.isPostgreSQLDatabase()) {
                    hql += " DATE >= to_timestamp(:start,'yyyy-mm-dd hh24:mi:ss') and";
                } else {
                    hql += " DATE >=:start and";
                }
            }
            if ((endTime = query.getEndTime()) != null) {
                if (GingaToolboxEnv.isPostgreSQLDatabase()) {
                    hql += " DATE <= to_timestamp(:end,'yyyy-mm-dd hh24:mi:ss') and";
                } else {
                    hql += " DATE <=:end and";
                }
            }
            if ((minElevation = query.getMinElevation()) != null) {
                hql += "  EELV > :eelv and";
            }
            if ((minRigidity = query.getMinCutOffRigidity()) != null) {
                hql += "  RIG >= :rig and";
            }
            if ((lacdumpFiles = query.getLacdumpFiles()) != null && lacdumpFiles.size() > 0) {
                hql += "  LACDUMP_FILE IN :files and";
            }
            if ((skyAnnulus = query.getSkyAnnulus()) != null) {
                if (GingaToolboxEnv.isMySQLDatabase()) {
                    hql += "  Sphedist( :targetRa, :targetDec, RA_DEG_B1950, DEC_DEG_B1950 )/60 > :innerRadii and";
                    hql += "  Sphedist( :targetRa, :targetDec, RA_DEG_B1950, DEC_DEG_B1950 )/60 < :outerRadii and";
                } else if (GingaToolboxEnv.isPostgreSQLDatabase()) {
                    hql += "  q3c_dist( :targetRa, :targetDec, RA_DEG_B1950, DEC_DEG_B1950 ) > :innerRadii and";
                    hql += "  q3c_dist( :targetRa, :targetDec, RA_DEG_B1950, DEC_DEG_B1950 ) < :outerRadii and";
                } else {
                    throw new IllegalArgumentException(
                            "No spherical indexing available for computing the distance");
                }
            }

            // remove last and
            hql = hql.substring(0, hql.lastIndexOf("and"));

            hql += " ORDER BY ID";

            log.debug("HQL=" + hql);

            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            Query hquery = hibernateSession.createQuery(hql);
            if (bitRate != null && !bitRate.equals(BitRate.ANY)) {
                hquery.setString("br", bitRate.toString());
            }
            if (mode != null) {
                hquery.setString("mode", mode.toString());
            }
            if (target != null && !query.isBackground()) {
                hquery.setString("target", "%" + target + "%");
            }
            if (startTime != null) {
                hquery.setString("start", startTime);
            }
            if (endTime != null) {
                hquery.setString("end", endTime);
            }
            if (minElevation != null) {
                hquery.setDouble("eelv", minElevation);
            }
            if (minRigidity != null) {
                hquery.setDouble("rig", minRigidity);
            }
            if (lacdumpFiles != null && lacdumpFiles.size() > 0) {
                hquery.setParameterList("files", lacdumpFiles);
            }
            if (skyAnnulus != null) {
                hquery.setDouble("targetRa", skyAnnulus.getTargetRaDeg());
                hquery.setDouble("targetDec", skyAnnulus.getTargetDecDeg());
                hquery.setDouble("innerRadii", skyAnnulus.getInnerRadiiDeg());
                hquery.setDouble("outerRadii", skyAnnulus.getOuterRadiiDeg());
            }

            sfList = hquery.list();
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new LacdumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return sfList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findModes(String target, String startTime, String endTime,
            double elevation, double rigidity) throws LacdumpDaoException {
        List<String> modes = null;
        try {
            String hql = null;
            if (GingaToolboxEnv.isPostgreSQLDatabase()) {
                hql = "SELECT distinct(lacdump.mode) FROM " + LacdumpSfEntity.class.getSimpleName()
                        + " as lacdump" + " WHERE TARGET like :target and "
                        + "DATE >= to_timestamp(:start,'yyyy-mm-dd hh24:mi:ss') and "
                        + "DATE <= to_timestamp(:end,'yyyy-mm-dd hh24:mi:ss') and "
                        + "EELV > :eelv and RIG >= :rig " + "ORDER BY mode";
            } else {
                hql = "SELECT distinct(lacdump.mode) FROM "
                        + LacdumpSfEntity.class.getSimpleName()
                        + " as lacdump"
                        + " WHERE TARGET like :target and "
                        + "DATE >= :start and DATE <= :end and EELV > :eelv and RIG >= :rig ORDER BY mode";
            }
            log.debug("HQL=" + hql);

            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            Query query = hibernateSession.createQuery(hql);
            query.setString("target", "%" + target + "%");
            query.setString("start", startTime);
            query.setString("end", endTime);
            query.setDouble("eelv", elevation);
            query.setDouble("rig", rigidity);

            modes = query.list();
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new LacdumpDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return modes;
    }

}