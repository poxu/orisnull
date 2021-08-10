package com.evilcorp.orisnull.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

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
        QueryParams params = new QueryParams() {
            @Override
            public boolean fieldEnabled(String paramName) {
                return "name".equals(paramName);
            }

            @Override
            public List<String> fields() {
                return List.of("name");
            }
        };
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
        QueryParams params = new QueryParams() {
            @Override
            public boolean fieldEnabled(String paramName) {
                return "name".equals(paramName);
            }

            @Override
            public List<String> fields() {
                return List.of("name");
            }
        };

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
        QueryParams params = new QueryParams() {
            @Override
            public boolean fieldEnabled(String paramName) {
                return false;
            }

            @Override
            public List<String> fields() {
                return List.of("name");
            }
        };

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
                + "   (b.name = :name or (1=1)) "
                ;

        String query = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "   (b.name = :name or :name is null) ";
//        QueryParams params = "name"::equals;
        QueryParams params = new QueryParams() {
            @Override
            public boolean fieldEnabled(String paramName) {
                return false;
            }

            @Override
            public List<String> fields() {
                return List.of("name");
            }
        };

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
        QueryParams params = new QueryParams() {
            @Override
            public boolean fieldEnabled(String paramName) {
                return "name".equals(paramName);
            }

            @Override
            public List<String> fields() {
                return List.of("name");
            }
        };

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
                + "   and (b.descr = :descr or (1!=1)) "
                ;

        String query = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "       (b.name = :name or :name is null) "
                + "   and (b.descr = :descr or :descr is null) "
                ;
        QueryParams params = new QueryParams() {
            @Override
            public boolean fieldEnabled(String paramName) {
                return List.of("name", "descr").contains(paramName);
            }

            @Override
            public List<String> fields() {
                return List.of("name", "descr");
            }
        };

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
                + "       (b.name = :name or (1!=1)) "
                + "   and (b.descr = :descr or (1!=1)) "
                ;

        String query = ""
                + " select                              "
                + "       b                             "
                + " from                                "
                + "      Book b                         "
                + " where                               "
                + "       (b.name = :name or  :name  is    null) "
                + "   and (b.descr = :descr or  :descr    is  null) "
                ;
        QueryParams params = new QueryParams() {
            @Override
            public boolean fieldEnabled(String paramName) {
                return List.of("name", "descr").contains(paramName);
            }

            @Override
            public List<String> fields() {
                return List.of("name", "descr");
            }
        };

        BetterQuery betterQuery = new BetterQueryParsingReplace(params);
        final String cleanedQuery = betterQuery.cleanQuery(query);

        assertEquals(expected, cleanedQuery);
    }

}