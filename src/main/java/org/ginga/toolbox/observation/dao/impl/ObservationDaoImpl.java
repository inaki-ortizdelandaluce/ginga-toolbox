package org.ginga.toolbox.observation.dao.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.ginga.toolbox.observation.ObservationEntity;
import org.ginga.toolbox.observation.dao.ObservationDao;
import org.ginga.toolbox.observation.dao.ObservationDaoException;
import org.ginga.toolbox.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class ObservationDaoImpl implements ObservationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ObservationEntity> findListByTarget(String target)
			throws ObservationDaoException {
        List<ObservationEntity> obsList = null;
        try {
            String hql = "FROM " + ObservationEntity.class.getSimpleName() + 
            		" WHERE TARGET_NAME like :target ORDER BY OBSERVATION_ID";

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

	@SuppressWarnings("unchecked")
	@Override
	public List<ObservationEntity> findAll() throws ObservationDaoException {
		List<ObservationEntity> obsList = null;
		try {
			HibernateUtil.beginTransaction();
			Session hibernateSession = HibernateUtil.getSession();
			Query query = hibernateSession.createQuery("from " + ObservationEntity.class.getName());
			obsList = query.list();
        } catch (Exception e) {
            throw new ObservationDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return obsList;
	}

	@Override
	public void update(ObservationEntity observation)
			throws ObservationDaoException {
		try {
			HibernateUtil.beginTransaction();
			Session hibernateSession = HibernateUtil.getSession();
			hibernateSession.update(observation);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			throw new ObservationDaoException(e);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> findAllTargets()
			throws ObservationDaoException {
		Set<String> targetList = new TreeSet<String>(); // sorted
		List<String> criteriaList = null;
		try {
			HibernateUtil.beginTransaction();
			Session hibernateSession = HibernateUtil.getSession();
			Criteria criteria = hibernateSession.createCriteria(ObservationEntity.class);
			criteria.setProjection(Projections.distinct(Projections.property("targetName")));
			criteriaList = criteria.list();
			// values contains concatenated targets with comma separator
			for (String colValue: criteriaList) {
				String[] sArray = colValue.split(",");
				for (int i = 0; i < sArray.length; i++) {
					targetList.add(sArray[i].trim());
				}
			}
        } catch (Exception e) {
            throw new ObservationDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
		// remove BGD as target list
		targetList.remove("BGD");
        return targetList;
	}

	@Override
    public ObservationEntity findById(long id) throws ObservationDaoException {
        ObservationEntity entity = null;
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            entity = (ObservationEntity) hibernateSession.get(ObservationEntity.class, id);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new ObservationDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return entity;
    }
}
