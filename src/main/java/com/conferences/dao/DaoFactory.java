package com.conferences.dao;

import com.conferences.dao.impl.mysql.MySQLDaoFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract Factory class  that enables applications to obtain
 * Dao Objects that provides access to database.
 *
 */
public abstract class DaoFactory {

    public static final String MY_SQL = "mysql";
    private static final Logger logger = LogManager.getLogger("Global");

    /**
     * Obtains a corresponding dao factory from name of  database management system.
     * @param dbName name of  database management system
     * @return Corresponding instance of a {@link DaoFactory}
     */
    public static DaoFactory getDaoFactory(String dbName) {
        if (dbName.equalsIgnoreCase(MY_SQL)) {
            logger.info("Database " + MY_SQL);
            return MySQLDaoFactory.getInstance();
        }
        throw new IllegalArgumentException("Can't find specified data bases name  DaoFactory");
    }

    /**
     * Obtains instance of a {@link UserDao} that corresponding their factory.
     * @return instance of a {@link UserDao}
     */
    public abstract UserDao getUserDao();

    /**
     * Obtains instance of a {@link EventDao} that corresponding their factory.
     * @return instance of a {@link EventDao}
     */
    public abstract EventDao getEventDao();

    /**
     * Obtains instance of a {@link ReportDao} that corresponding their factory.
     * @return instance of a {@link ReportDao}
     */
    public abstract ReportDao getReportDao();
}
