package org.ginga.toolbox.target.dao.impl;

import java.util.List;

import org.ginga.toolbox.target.TargetEntity;
import org.ginga.toolbox.target.dao.TargetDao;
import org.ginga.toolbox.target.dao.TargetDaoException;
import org.ginga.toolbox.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class TargetDaoImpl implements TargetDao {

    @Override
    public void save(TargetEntity target) throws TargetDaoException {
        try {
            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            hibernateSession.save(target);
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new TargetDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public TargetEntity findByName(String name) throws TargetDaoException {
        TargetEntity entity = null;
        try {
            String hql = "FROM " + TargetEntity.class.getSimpleName() + " WHERE TARGET_NAME =:name";

            HibernateUtil.beginTransaction();
            Session hibernateSession = HibernateUtil.getSession();
            Query query = hibernateSession.createQuery(hql);
            query.setString("name", name);
            List<TargetEntity> entityList = query.list();
            if (entityList.size() > 0) {
                entity = entityList.get(0);
            }
            HibernateUtil.commitTransaction();
        } catch (Exception e) {
            throw new TargetDaoException(e);
        } finally {
            HibernateUtil.closeSession();
        }
        return entity;
    }
}
