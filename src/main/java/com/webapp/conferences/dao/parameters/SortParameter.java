package com.webapp.conferences.dao.parameters;

public class SortParameter {
    private final Object clause;
    private final Order order;

    public SortParameter(Object clause, Order value){
        this.clause = clause;
        this.order = value;
    }

    public Order getOrder() {
        return order;
    }

    public Object getClause() {
        return clause;
    }

    public enum Order {
        ASC,DESC
    }
}
