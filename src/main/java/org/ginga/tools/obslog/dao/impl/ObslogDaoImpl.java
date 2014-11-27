package org.ginga.tools.obslog.dao.impl;

import java.util.List;

import org.ginga.tools.obslog.ObslogEntity;
import org.ginga.tools.obslog.dao.ObslogDao;
import org.ginga.tools.obslog.dao.ObslogDaoException;
import org.ginga.tools.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class ObslogDaoImpl implements ObslogDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ObslogEntity> findListByTarget(String target)
			throws ObslogDaoException {
        List<ObslogEntity> obsList = null;
        try {
            String hql = "FROM ObslogEntity WHERE TARGET_NAME like :target ORDER BY ID";

            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            Query query = hibernateSession.createQuery(hql);
            query.setString("target", "%" + target + "%");
            
            obsList = query.list();
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new ObslogDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return obsList;
	}

}
