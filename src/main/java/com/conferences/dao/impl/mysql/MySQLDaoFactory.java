package com.conferences.dao.impl.mysql;

import com.conferences.dao.DaoFactory;
import com.conferences.dao.EventDao;
import com.conferences.dao.ReportDao;
import com.conferences.dao.UserDao;
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
