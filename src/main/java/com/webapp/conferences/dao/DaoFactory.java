package com.webapp.conferences.dao;

import com.webapp.conferences.dao.impl.mysql.MySQLDaoFactory;
import com.webapp.conferences.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class DaoFactory {

    private static final Logger logger = LogManager.getLogger("Global");

    public static DaoFactory getDaoFactory(String dbName) {
        if(dbName.equalsIgnoreCase("MySQL")) {
            return MySQLDaoFactory.getInstance();
        }
        throw new IllegalArgumentException("Can't find specified data bases name  DaoFactory");
    }

    public abstract UserDao getUserDao() throws DaoException;
    public abstract EventDao getEventDao();
    public abstract ReportDao getReportDao();
}
