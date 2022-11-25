package com.conferences.dao.parameters;

import com.conferences.dao.impl.mysql.util.MySQLQueryBuilder;

/**
 * Represents a predicate of field, value and condition.
 */
public class FilterParameter {

    private final String field;
    private final Object value;
    private final MySQLQueryBuilder.Condition condition;

    /**
     * Initializes a new {@code FilterParameter} object
     * @param field a column to be filtered
     * @param value a parameter value
     * @param condition {@link com.conferences.dao.impl.mysql.util.MySQLQueryBuilder.Condition} object
     */
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
