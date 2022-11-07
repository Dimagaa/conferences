package com.webapp.conferences.dao.impl.mysql.util;

import com.webapp.conferences.dao.parameters.FilterParameter;
import com.webapp.conferences.dao.parameters.SortParameter;
import com.webapp.conferences.exceptions.DaoException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.webapp.conferences.dao.parameters.SortParameter.*;

public class MySQLQueryBuilder {

    private final StringBuilder sb = new StringBuilder();
    private final LinkedList<Object> values = new LinkedList<>();

    public LinkedList<Object> getValues() {
        return values;
    }

    public MySQLQueryBuilder select() {
        sb.append("SELECT ");
        return this;
    }

    public MySQLQueryBuilder select(String field) throws DaoException {
        sb.append("SELECT ");
        setField(field);
        return this;
    }

    public MySQLQueryBuilder select(MySQLQueryBuilder qb) {
        this.values.addAll(qb.values);
        this.sb.append("SELECT ").append(qb.sb).append(" ");
        return this;

    }

    public MySQLQueryBuilder addSelectField(){
        sb.append(", ");
        return this;
    }

    public MySQLQueryBuilder from(String table) throws DaoException {
        if (isValidNaming(table)) {
            sb.append("FROM ").append(table).append(" ");
        }
        return this;
    }

    public MySQLQueryBuilder from(MySQLQueryBuilder qb) {
        this.values.addAll(qb.values);
        this.sb.append("FROM (").append(qb.sb).append(") ");
        return this;
    }

    public MySQLQueryBuilder where() {
        sb.append("WHERE ");
        return this;
    }

    public MySQLQueryBuilder and() {
        sb.append("AND ");
        return this;
    }

    public MySQLQueryBuilder or() {
        sb.append("OR ");
        return this;
    }

    public MySQLQueryBuilder count(String field) throws DaoException {
        sb.append("COUNT(");
        setField(field);
        sb.append(") ");
        return this;
    }

    public MySQLQueryBuilder count() {
        sb.append("COUNT(*) ");
        return this;
    }

    public MySQLQueryBuilder all() {
        sb.append("* ");
        return this;
    }

    public MySQLQueryBuilder on(String field1, String field2) throws DaoException {
        if(isValidNaming(field1)|| isValidNaming(field2)) {
            sb.append("ON ").append(field1).append(" = ").append(field2).append(" ");
        }
        return this;
    }

    public MySQLQueryBuilder as(String field) throws DaoException {
        sb.append("AS ");
        return setField(field);
    }


    public MySQLQueryBuilder orderBy(Object cause, Order order) throws DaoException {
        sb.append("ORDER BY ");
        if(cause.getClass() == String.class) {
            setField((String) cause);
        } else {
            setValue(cause);
        }
        sb.append(order).append(" ");
        return this;
    }

    public MySQLQueryBuilder orderBy(List<SortParameter> mapSorter) throws DaoException {
        Iterator<SortParameter> tokens = mapSorter.iterator();
        if (!tokens.hasNext()) {
            return this;
        }
        SortParameter orderBy = tokens.next();
        this.orderBy(orderBy.getClause(), orderBy.getOrder());
        while (tokens.hasNext()) {
            orderBy = tokens.next();
            this.addColumOrder(orderBy.getClause(), orderBy.getOrder());
        }
        return this;
    }

    public MySQLQueryBuilder leftJoin(String field) throws DaoException {
        sb.append("LEFT JOIN ");
        setField(field);
        return this;
    }
    public MySQLQueryBuilder leftJoin(MySQLQueryBuilder qb) {
        this.values.addAll(qb.values);
        this.sb.append("LEFT JOIN (").append(qb.getQuery()).append(") ");
        return this;
    }


    public MySQLQueryBuilder addColumOrder(Object value, Order order) {
        setValues(value);
        sb.append(", ").append(order).append(" ");
        return this;
    }


    public MySQLQueryBuilder limit(Object offset, Object count) {
        sb.append("LIMIT ");
        setValues(offset, count);
        return this;
    }

    public MySQLQueryBuilder where(List<FilterParameter> filters) throws DaoException {
        Iterator<FilterParameter> tokens = filters.iterator();
        if (!tokens.hasNext()) {
            return this;
        }
        FilterParameter filter = tokens.next();
        this.where().setFilter(filter);
        while (tokens.hasNext()) {
            filter = tokens.next();
            this.and().setFilter(filter);
        }
        return this;
    }
    public MySQLQueryBuilder groupBy(String field) throws DaoException {
        sb.append("GROUP BY ");
        setField(field);
        return this;
    }

    public MySQLQueryBuilder setFilter(FilterParameter filter) throws DaoException {
        return this.setField(filter.getField())
                .setCondition(filter.getCondition())
                .setValues(filter.getValue());
    }
    public MySQLQueryBuilder having() {
        sb.append("HAVING ");
        return this;
    }

    public MySQLQueryBuilder setCondition(Condition condition) {
        sb.append(condition.getOperator());
        return this;
    }

    public String getQuery() {
        return sb.toString().trim();
    }

    public MySQLQueryBuilder setField(String field) throws DaoException {
        if (isValidNaming(field)) {
            sb.append(field).append(" ");
        }
        return this;
    }

    public MySQLQueryBuilder setValues(Object... values) {

        String joining = Arrays.stream(values).peek(this::setValue).filter(v -> v.getClass() != MySQLQueryBuilder.class)
                .map(o -> "?")
                .collect(Collectors.joining(", "));
        sb.append(joining).append(" ");
        return this;
    }

    private void setValue(Object value) {
        if (value.getClass() == MySQLQueryBuilder.class) {
            appendSubQuery((MySQLQueryBuilder) value);
            return;
        }
        values.add(value);
    }

    private boolean isValidNaming(String tableName) throws DaoException {
        Pattern pattern = Pattern.compile("^\\w+\\.?\\w*$");
        Matcher matcher = pattern.matcher(tableName);
        if(matcher.matches()){
            return true;
        }
        throw new DaoException("Illegal Argument: field should contain only word characters. Field: " + tableName);
    }

    private void appendSubQuery(MySQLQueryBuilder subQuery) {
        values.addAll(subQuery.values);
        this.sb.append("(")
                .append(subQuery.getQuery())
                .append(") ");
    }

    public enum Condition {
        EQUAL("= "),
        GREATER_THAN("> "),
        LESS_THAN("< "),
        GREATER_THAN_OR_EQUAL(">= "),
        LESS_THAN_OR_EQUAL("<= "),
        IN("IN "),
        LIKE("LIKE ");

        private final String operator;

        Condition(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }

    }
}
