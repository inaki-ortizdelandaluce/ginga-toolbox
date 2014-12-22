package org.ginga.toolbox.observation.dao.impl;

import java.util.List;

import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class ObservationDaoImpl implements ObservationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ObservationEntity> findListByTarget(String target)
			throws ObservationDaoException {
        List<ObservationEntity> obsList = null;
        try {
            String hql = "FROM " + ObservationEntity.class.getSimpleName() + 
            		" WHERE TARGET_NAME like :target ORDER BY ID";

            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            Query query = hibernateSession.createQuery(hql);
            query.setString("target", "%" + target + "%");
            
            obsList = query.list();
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new ObservationDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return obsList;
	}

}
