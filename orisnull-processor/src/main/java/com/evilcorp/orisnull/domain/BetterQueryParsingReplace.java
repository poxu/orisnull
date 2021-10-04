package com.evilcorp.orisnull.domain;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetterQueryParsingReplace implements BetterQuery {
    private final QueryParams params;

    public BetterQueryParsingReplace(QueryParams params) {
        this.params = params;
    }

    @Override
    public String cleanQuery(String query) {
        final List<String> paramNames = params.fields();
        String transformedQuery = query;
        for (String paramName : paramNames) {
            Pattern pattern  = Pattern.compile(":" + paramName + "\\s+is\\s+null", Pattern.CASE_INSENSITIVE);
            final Matcher matcher = pattern.matcher(transformedQuery);
            if (params.fieldEnabled(paramName)) {
                transformedQuery = matcher.replaceAll("(1!=1)");
            } else {
                transformedQuery = matcher.replaceAll("(1=1)");
            }
        }
        return transformedQuery;
    }

}
