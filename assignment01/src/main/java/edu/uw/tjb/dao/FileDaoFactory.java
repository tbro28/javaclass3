package edu.uw.tjb.dao;

import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactory;
import edu.uw.ext.framework.dao.DaoFactoryException;

public class FileDaoFactory implements DaoFactory {
    @Override
    public AccountDao getAccountDao() throws DaoFactoryException {
        return new FileAccountDao();
    }
}
