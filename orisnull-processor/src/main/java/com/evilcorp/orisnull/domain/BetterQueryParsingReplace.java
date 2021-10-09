package com.evilcorp.orisnull.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetterQueryParsingReplace implements BetterQuery {
    private final Map<String, Object> params;

    public BetterQueryParsingReplace(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String cleanQuery(String query) {
        final Set<String> paramNames = params.keySet();
        String transformedQuery = query;
        for (String paramName : paramNames) {
            Pattern pattern  = Pattern.compile(":" + paramName + "\\s+is\\s+null", Pattern.CASE_INSENSITIVE);
            final Matcher matcher = pattern.matcher(transformedQuery);
            if (params.get(paramName) != null) {
                transformedQuery = matcher.replaceAll("(1!=1)");
            } else {
                transformedQuery = matcher.replaceAll("(1=1)");
            }
        }
        return transformedQuery;
    }

}
