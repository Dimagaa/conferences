package com.conferences.services.pagination;

import com.conferences.dao.parameters.SortParameter;

import java.util.*;

public class Page {

    private final int DEFAULT_PAGE_SIZE = 10;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private Map<String, String> targetFilters;
    private final Map<String, String> filters = new HashMap<>();
    private final Map<String, SortParameter.Order> sorters = new HashMap<>();
    private int pageNumber;
    private int totalRows;

    public Page(int pageNumber){
        this.pageNumber = pageNumber;
    }

    public Page(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public void setSortParameter(String sortBy, String order) {
        if(order.equalsIgnoreCase("DESC")) {
            sorters.put(sortBy, SortParameter.Order.DESC);
            return;
        }
        sorters.put(sortBy, SortParameter.Order.ASC);
    }

    public void setFilterParameter(String field, String value) {
        if(value == null || value.isEmpty()) {
            filters.remove(field);
            return;
        }
        filters.put(field, value);
    }

    public Map<String, SortParameter.Order> getSorters() {
        return sorters;
    }

    public Map<String, String> getFilters() {
        Map<String, String> rawFilter = new HashMap<>(filters);
        targetFilters.forEach(rawFilter::putIfAbsent);
        return rawFilter;
    }

    public int getOffset() {
        return (pageNumber-1)*pageSize;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getPagesCount() {
        return (int) Math.ceil(totalRows*1.0/pageSize);
    }

    public void clearParams() {
        filters.clear();
        sorters.clear();
    }

    public boolean containsFilter(String filter){
        return filters.containsKey(filter);
    }

    public boolean containsSort(String sort) {
        return sorters.containsKey(sort);
    }

    public AbstractMap.SimpleEntry<String, String> getFirstSort() {
        return sorters.entrySet().stream()
                .findFirst()
                .map((e) -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().name()))
                .orElse(null);
    }

    public String getFilterValue(String field) {
        return filters.get(field);
    }

    public Map<String, String> getTargetFilters() {
        return targetFilters;
    }

    public void setTargetFilters(String filter, String value) {
        targetFilters = new HashMap<>();
        targetFilters.put(filter, value);
    }

    public void puttTargetFilter(String filter, String value) {
        targetFilters.put(filter, value);
    }
}
