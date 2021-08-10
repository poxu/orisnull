package com.evilcorp.orisnull.domain;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BetterQueryParsingReplace implements BetterQuery {
    private final QueryParams params;

    public BetterQueryParsingReplace(QueryParams params) {
        this.params = params;
    }

    @Override
    public String cleanQuery(String query) {
        final List<String> paramNames = params.fields();

        for (String paramName : paramNames) {
            query = removePattern(query, Pattern.compile("or\\s+:" + paramName + "\\s+is\\s+null", Pattern.CASE_INSENSITIVE), true);
            query = removePattern(query, Pattern.compile(":" + paramName + "\\s+is\\s+null\\s+or", Pattern.CASE_INSENSITIVE), false);
        }

        return query;
    }

    private String removePattern(String query, Pattern pattern, boolean leftOr) {
        final Matcher matcher = pattern.matcher(query);
        final List<MatchResult> collect = matcher.results().collect(Collectors.toList());
        for (MatchResult matchResult : collect) {
            System.out.println("m");
            query = removeIsNull(query, matchResult, leftOr);
        }
        return query;
    }

    private String removeIsNull(String query, MatchResult matchResult, boolean leftOr) {
        final int orIsNullStart = matchResult.start();
        if (orIsNullStart == -1) {
            return query;
        }

        final int orIsNullEnd = matchResult.end();

        StringBuilder newQuery = new StringBuilder();
        final String separator;
        if (params.fieldEnabled("name")) {
            separator = "!=";
        } else {
            separator = "=";
        }
        newQuery.append(query.substring(0, orIsNullStart));
        if (leftOr) {
            newQuery.append("or (1" + separator + "1)");
            newQuery.append(query.substring(orIsNullEnd));
        } else {
            newQuery.append("(1" + separator + "1) or ");
            newQuery.append(query.substring(orIsNullEnd+1));
        }
        query = newQuery.toString();
        return query;
    }
}
