package com.conferences.dao.parameters;

import com.conferences.model.Event;
import com.conferences.dao.impl.mysql.util.MySQLQueryBuilder;
import com.conferences.exceptions.DaoException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.conferences.dao.impl.mysql.util.MySQLQueryBuilder.Condition.*;

/**
 * Factory class that contain prepared templates with specific parameters for obtain data from the database
 */
public class FilterFactory {
    /**
     * {@link HashMap} contains method source that supplies {@link FilterParameter} object
     */
    private final Map<String, FilterSupplier> filterStore = new HashMap<>();

    {
        filterStore.put("expiredEvents", this::expiredEvents);
        filterStore.put("eventsBySpeaker", this::eventsBySpeaker);
        filterStore.put("eventsByPlace", this::eventsByPlace);
        filterStore.put("joinedEvents", this::joinedEvents);
        filterStore.put("searchEvent", this::searchEvent);
        filterStore.put("eventsByStatus", this::eventsByStatus);
    }

    /**
     * Allows get prepared filter
     * @param filter a name of filter template
     * @param value a parameter value
     * @return prepared {@link FilterParameter} object
     * @throws DaoException if a filter was not found
     */
    public FilterParameter getFilter(String filter, String value) throws DaoException {
        if (filterStore.containsKey(filter)) {
            return filterStore.get(filter).getFilter(value);
        }
        throw new DaoException("Cannot get filter because filter not found: " + filter);
    }

    private FilterParameter expiredEvents(String value) {
        return new FilterParameter("start_time", new Timestamp(new Date().getTime()), LESS_THAN_OR_EQUAL);
    }

    private FilterParameter eventsBySpeaker(String speakerId) throws DaoException {
        MySQLQueryBuilder qb = new MySQLQueryBuilder()
                .select("reports.event_id")
                .from("reports")
                .where().setFilter(new FilterParameter("reports.speaker_id", speakerId, EQUAL));

        return new FilterParameter("events.id", qb, IN);
    }

    private FilterParameter eventsByPlace(String place) {
        return new FilterParameter("events.place", place, EQUAL);
    }

    private FilterParameter eventsByStatus(String status) {
        MySQLQueryBuilder builder = new MySQLQueryBuilder()
                .setValues((Object[]) status.toUpperCase().split(" "));
        return new FilterParameter("events.status", builder , IN);
    }

    private FilterParameter joinedEvents(String id) throws DaoException {
        MySQLQueryBuilder qb = new MySQLQueryBuilder()
                .select("participants.event_id")
                .from("participants")
                .where().setFilter(new FilterParameter("user_id", id, EQUAL));

        return new FilterParameter("events.id", qb, IN);
    }

    private FilterParameter searchEvent(String input) {
        input = String.format("%%%s%%", input);
        return new FilterParameter("events.name", input, LIKE);
    }

    @FunctionalInterface
    private interface FilterSupplier {
        FilterParameter getFilter(String value) throws DaoException;
    }
}
