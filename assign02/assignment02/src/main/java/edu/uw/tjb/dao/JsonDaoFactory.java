package edu.uw.tjb.dao;

import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactory;
import edu.uw.ext.framework.dao.DaoFactoryException;


/**
 * Implementation of DaoFactory that creates a FileAccountDao instance.
 *
 * @author Russ Moul
 */
public final class JsonDaoFactory implements DaoFactory {
    /**
     * Instantiates an instance of FileAccountDao.
     *
     * @return a new instance of FileAccountDao
     *
     * @throws DaoFactoryException if instantiation fails
     */
	@Override
    public AccountDao getAccountDao() throws DaoFactoryException {
        return new JsonAccountDao();
    }
}

