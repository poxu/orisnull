package com.evilcorp.orisnull.domain;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BetterQueryParsing implements BetterQuery {
    private final QueryParams params;

    public BetterQueryParsing(QueryParams params) {
        this.params = params;
    }

    @Override
    public String cleanQuery(String query) {
        final List<String> paramNames = params.fields();

        for (String paramName : paramNames) {
            Pattern pattern = Pattern.compile("or\\s+:" + paramName + "\\s+is\\s+null");
            final Matcher matcher = pattern.matcher(query);
            final List<MatchResult> collect = matcher.results().collect(Collectors.toList());
//            for (MatchResult matchResult : collect) {
//                matchResult.
//            }

            final int orIsNullStart = query.indexOf("or :" + paramName + " is null");
            if (orIsNullStart == -1) {
                continue;
            }

            int blockStart = -1;
            for (int i = orIsNullStart; i >= 0; i--) {
                if (query.charAt(i) == '(') {
                    blockStart = i;
                    break;
                }
            }

            int blockEnd = -1;
            for (int i = orIsNullStart; i < query.length(); i++) {
                if (query.charAt(i) == ')') {
                    blockEnd = i;
                    break;
                }
            }

            StringBuilder newQuery = new StringBuilder();
            if (params.fieldEnabled("name")) {
                newQuery.append(query.substring(0, orIsNullStart));
                newQuery.append("or 1!=1");
                newQuery.append(query.substring(blockEnd));
            } else {
                newQuery.append(query.substring(0, blockStart + 1));
                newQuery.append("1=1");
                newQuery.append(query.substring(blockEnd));
            }
            query = newQuery.toString();
        }

        return query;
    }
}
