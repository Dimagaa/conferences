package com.webapp.conferences.dao.impl.mysql;

import com.webapp.conferences.dao.DaoFactory;
import com.webapp.conferences.dao.EventDao;
import com.webapp.conferences.dao.ReportDao;
import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.exceptions.DaoException;

public class MySQLDaoFactory extends DaoFactory {

    private static MySQLDaoFactory instance;

    private MySQLDaoFactory(){

    }

    public static synchronized MySQLDaoFactory getInstance() {
        if(instance == null) {
            instance = new MySQLDaoFactory();
        }
        return instance;
    }

    @Override
    public UserDao getUserDao() {
        return new MySQLUserDao();
    }

    @Override
    public EventDao getEventDao() {
        return new MySQLEventDao();
    }

    @Override
    public ReportDao getReportDao() {
        return new MySQLReportDao();
    }
}
