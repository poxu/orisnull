package com.evilcorp.orisnull.repository;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrIsNullReplace {
    private final Map<String, Object> params;

    public OrIsNullReplace(Map<String, Object> params) {
        this.params = params;
    }

    public String transform(String query) {
        String transformedQuery = query;
        for (String paramName : params.keySet()) {
            Pattern pattern = Pattern.compile(":" + paramName + "\\s+is\\s+null", Pattern.CASE_INSENSITIVE);
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
