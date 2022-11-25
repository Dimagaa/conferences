package com.conferences.MySQLDaoTest;

import com.conferences.dao.parameters.FilterParameter;
import com.conferences.dao.impl.mysql.util.MySQLQueryBuilder;
import com.conferences.dao.parameters.SortParameter;
import com.conferences.exceptions.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Date;
import java.util.LinkedList;
import java.util.stream.Stream;

import static com.conferences.dao.impl.mysql.util.MySQLQueryBuilder.Condition.*;
import static org.junit.jupiter.api.Assertions.*;


public class MySQLQueryBuilderTest {

    private MySQLQueryBuilder builder;


    @BeforeEach
    public void init() {
        builder = new MySQLQueryBuilder();
    }

    @Test
    void creatingBuilderTest() {
        assertNotNull(builder);
    }

    @Test
    void whereMethodTestWhenArgumentListIsEmpty() throws DaoException {
        builder = new MySQLQueryBuilder();
        LinkedList<FilterParameter> list = new LinkedList<>();
        assertEquals(builder.where(list).getQuery(), "");
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(SimpleFilterArgumentProvider.class)
    void whereMethodTestWhenListHaveOneValidSimpleFilter(String expected, FilterParameter filter) throws DaoException {
        builder = new MySQLQueryBuilder();
        LinkedList<FilterParameter> list = new LinkedList<>();
        list.add(filter);
        assertEquals(expected, builder.where(list).getQuery());

    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(WrappedFilterArgumentProvider.class)
    void whereMethodTestWhenListHaveOneValidWrappedFilter(String expected, FilterParameter filter) throws DaoException {
        LinkedList<FilterParameter> list = new LinkedList<>();
        list.add(filter);
        assertEquals(expected, builder.where(list).getQuery());

    }

    @Test
    void testSetValuesWithoutArgs(){
        builder.setValues();
        assertEquals("", builder.getQuery());
    }

    @Test
    void testSetValuesWithOneArg() {
        builder.setValues("Test");
        assertEquals("?", builder.getQuery());
    }

    @Test
    void testSetValuesWithTwoArgs() {
        builder.setValues("Test", new Date());
        assertEquals("?, ?", builder.getQuery());
    }

    @Test
    void testSetValuesWithThreeArgsAndQueryBuilder() {
        builder.setValues("Test", new Date(), new MySQLQueryBuilder(), "test");
        assertEquals("() ?, ?, ?", builder.getQuery());
    }

    @Test
    void orderByTestWhenWithValidParameters() throws DaoException {
        builder.select("test").from("test_table");
        SortParameter sortParameter = new SortParameter("test_id", SortParameter.Order.ASC);
        String expected = "SELECT test FROM test_table ORDER BY test_id ASC";
        assertEquals(expected, builder.orderBy(sortParameter.getClause(), sortParameter.getOrder()).getQuery());
    }

    static class SimpleFilterArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of("WHERE test_field1 = ?", new FilterParameter("test_field1", "test_value", EQUAL)),
                    Arguments.of("WHERE test_field1 IN ?", new FilterParameter("test_field1", "test_value", IN)),
                    Arguments.of("WHERE test_field1 > ?", new FilterParameter("test_field1", "test_value", MySQLQueryBuilder.Condition.GREATER_THAN)),
                    Arguments.of("WHERE test_field1 < ?", new FilterParameter("test_field1", "test_value", LESS_THAN)),
                    Arguments.of("WHERE test_field1 >= ?", new FilterParameter("test_field1", "test_value", GREATER_THAN_OR_EQUAL)),
                    Arguments.of("WHERE test_field1 <= ?", new FilterParameter("test_field1", "test_value", LESS_THAN_OR_EQUAL))

            );
        }
    }

    static class WrappedFilterArgumentProvider implements ArgumentsProvider {
        MySQLQueryBuilder qb = new MySQLQueryBuilder().select().from("test_table").where().setCondition(EQUAL);
        WrappedFilterArgumentProvider() throws DaoException {
        }
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of("WHERE test_field = (" + qb.getQuery()+")", new FilterParameter("test_field", qb, EQUAL)),
                    Arguments.of("WHERE test_field IN (" + qb.getQuery()+")", new FilterParameter("test_field", qb, IN)),
                    Arguments.of("WHERE test_field > (" + qb.getQuery()+")", new FilterParameter("test_field", qb, GREATER_THAN)),
                    Arguments.of("WHERE test_field < (" + qb.getQuery()+")", new FilterParameter("test_field", qb, LESS_THAN)),
                    Arguments.of("WHERE test_field >= (" + qb.getQuery()+")", new FilterParameter("test_field", qb, GREATER_THAN_OR_EQUAL)),
                    Arguments.of("WHERE test_field <= (" + qb.getQuery()+")", new FilterParameter("test_field", qb, LESS_THAN_OR_EQUAL))

            );
        }
    }

}
