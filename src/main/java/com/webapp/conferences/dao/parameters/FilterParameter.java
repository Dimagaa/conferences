package com.webapp.conferences.dao.parameters;

import com.webapp.conferences.dao.impl.mysql.util.MySQLQueryBuilder;

public class FilterParameter {

    private final String field;
    private final Object value;
    private final MySQLQueryBuilder.Condition condition;

    public FilterParameter(String field, Object value, MySQLQueryBuilder.Condition condition) {
        this.field = field;
        this.value = value;
        this.condition = condition;
    }


    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public MySQLQueryBuilder.Condition getCondition() {
        return condition;
    }
}
