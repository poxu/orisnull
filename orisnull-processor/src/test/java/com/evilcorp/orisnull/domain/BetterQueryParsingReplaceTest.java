package com.evilcorp.orisnull.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BetterQueryParsingReplaceTest {
    @Test
    void noOpQuery() {
        String query = ""
                + " select              "
                + "       b             "
                + " from                "
                + "      Book b         "
                + " where               "
                + "      b.name = :name ";
        Map<String, Object> params = Map.of("name", "name");
        BetterQuery betterQuery = new BetterQueryParsingReplace(params);

        final String cleanedQuery = betterQuery.cleanQuery(query);

        assertEquals(query, cleanedQuery);
    }

    @Test
    void isNullOrQueryWithBracesAndParameterPresent() {
        String expected = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "   ((1!=1) or b.name = :name) ";

        String query = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "   (:name is null or b.name = :name) ";

        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("fame", null);

        BetterQuery betterQuery = new BetterQueryParsingReplace(params);
        final String cleanedQuery = betterQuery.cleanQuery(query);

        assertEquals(expected, cleanedQuery);
    }

    @Test
    void isNullOrQueryWithBracesAndParameterAbsent() {
        String expected = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "   ((1=1) or b.name = :name) ";

        String query = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "   (:name is null or b.name = :name) ";

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);

        BetterQuery betterQuery = new BetterQueryParsingReplace(params);
        final String cleanedQuery = betterQuery.cleanQuery(query);

        assertEquals(expected, cleanedQuery);
    }

    @Test
    void orIsNullQueryWithBracesAndParameterAbsent() {
        String expected = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "   (b.fame = :fame or (1=1)) ";

        String query = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "   (b.fame = :fame or :fame is null) ";

        Map<String, Object> params = new HashMap<>();
        params.put("fame", null);

        BetterQuery betterQuery = new BetterQueryParsingReplace(params);
        final String cleanedQuery = betterQuery.cleanQuery(query);

        assertEquals(expected.length(), cleanedQuery.length());
        assertEquals(expected, cleanedQuery);
    }

    @Test
    void orIsNullQueryWithBracesAndParameterPresent() {
        String expected = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "   (b.name = :name or (1!=1)) ";

        String query = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "   (b.name = :name or :name is null) ";

        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");

        BetterQuery betterQuery = new BetterQueryParsingReplace(params);
        final String cleanedQuery = betterQuery.cleanQuery(query);

        assertEquals(expected, cleanedQuery);
    }

    @Test
    void orIsNullQueryWithBracesAndDifferentParametersPresent() {
        String expected = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "       (b.name = :name or (1!=1)) "
                + "   and (b.descr = :descr or (1!=1)) ";

        String query = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "       (b.name = :name or :name is null) "
                + "   and (b.descr = :descr or :descr is null) ";

        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("descr", "descr");

        BetterQuery betterQuery = new BetterQueryParsingReplace(params);
        final String cleanedQuery = betterQuery.cleanQuery(query);

        assertEquals(expected, cleanedQuery);
    }

    @Test
    void orIsNullQueryWithBracesAndDifferentParametersPresentRandomSpaces() {
        String expected = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "       (b.name = :name or  (1!=1)) "
                + "   and (b.descr = :descr or  (1!=1)) ";

        String query = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "       (b.name = :name or  :name  is    null) "
                + "   and (b.descr = :descr or  :descr    is  null) ";

        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("descr", "descr");

        BetterQuery betterQuery = new BetterQueryParsingReplace(params);
        final String cleanedQuery = betterQuery.cleanQuery(query);

        assertEquals(expected, cleanedQuery);
    }

}