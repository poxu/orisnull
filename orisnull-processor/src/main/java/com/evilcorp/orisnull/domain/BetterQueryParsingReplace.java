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
            query = removePattern(query, Pattern.compile(":" + paramName + "\\s+is\\s+null", Pattern.CASE_INSENSITIVE));
        }

        return query;
    }

    private String removePattern(String query, Pattern pattern) {
        final Matcher matcher = pattern.matcher(query);
        final List<MatchResult> collect = matcher.results().collect(Collectors.toList());
        for (MatchResult matchResult : collect) {
            query = removeIsNull(query, matchResult);
        }
        return query;
    }

    private String removeIsNull(String query, MatchResult matchResult) {
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
        newQuery.append("(1" + separator + "1)");
        newQuery.append(query.substring(orIsNullEnd));
        query = newQuery.toString();
        return query;
    }
}
