package org.ginga.toolbox.target.dao;

import org.ginga.toolbox.target.TargetEntity;

public interface TargetDao {

    public void save(TargetEntity target) throws TargetDaoException;

    public TargetEntity findByName(String targetName) throws TargetDaoException;

}