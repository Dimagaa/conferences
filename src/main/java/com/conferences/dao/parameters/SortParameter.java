package com.conferences.dao.parameters;

/**
 * Is specific class that represent sort parameter by ascending or descending order
 */
public class SortParameter {
    private final Object clause;
    private final Order order;

    /**
     * Initializes a new {@link SortParameter} object with clause and order value (ascending or descending)
     * @param clause is condition for sorting
     * @param value is {@link Order} value
     */
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
