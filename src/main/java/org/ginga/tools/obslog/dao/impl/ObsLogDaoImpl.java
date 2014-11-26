package org.ginga.tools.obslog.dao.impl;

import java.util.List;

import org.ginga.tools.obslog.ObsLogEntity;
import org.ginga.tools.obslog.dao.ObsLogDao;
import org.ginga.tools.obslog.dao.ObsLogDaoException;
import org.ginga.tools.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class ObsLogDaoImpl implements ObsLogDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ObsLogEntity> findListByTarget(String target)
			throws ObsLogDaoException {
        List<ObsLogEntity> obsList = null;
        try {
            String hql = "FROM ObsLogEntity WHERE TARGET_NAME like :target ORDER BY ID";

            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            Query query = hibernateSession.createQuery(hql);
            query.setString("target", "%" + target + "%");
            
            obsList = query.list();
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new ObsLogDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return obsList;
	}

}
