package com.webapp.conferences.dao;

import com.webapp.conferences.dao.impl.mysql.MySQLDaoFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class DaoFactory {

    public static final String MY_SQL = "mysql";
    private static final Logger logger = LogManager.getLogger("Global");

    public static DaoFactory getDaoFactory(String dbName) {
        if (dbName.equalsIgnoreCase(MY_SQL)) {
            logger.info("Database " + MY_SQL);
            return MySQLDaoFactory.getInstance();
        }
        throw new IllegalArgumentException("Can't find specified data bases name  DaoFactory");
    }

    public abstract UserDao getUserDao();

    public abstract EventDao getEventDao();

    public abstract ReportDao getReportDao();
}
