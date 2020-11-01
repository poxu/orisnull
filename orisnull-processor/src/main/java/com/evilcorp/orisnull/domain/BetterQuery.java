package com.evilcorp.orisnull.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BetterQuery {
    private final QueryParams params;

    public BetterQuery(QueryParams params) {
        this.params = params;
    }

    public boolean lineToRemove(String line) {
        if (!line.contains("-- op")) {
            return false;
        }
        final var paramStartIdx = line.indexOf(':');
        if (paramStartIdx == -1) {
            throw new IllegalArgumentException("Line has comments, but doesn't contain a parameter");
        }
        final var paramEndIdx = line.indexOf(' ', paramStartIdx);

        final var paramName = line.substring(paramStartIdx, paramEndIdx);
        return !params.fieldEnabled(paramName);
    }

    public String removeComments(String line) {
        if (!line.contains("-- op")) {
            return line;
        }
        final var commentsStartIdx = line.indexOf("-- op");
        final var substring = line.substring(0, commentsStartIdx);
        return substring;
    }

    public String cleanQuery(String query) {
        final var lines = query.split("\n");
        final var result = Arrays.stream(lines)
                .filter(l -> !lineToRemove(l))
                .map(l -> removeComments(l))
                .collect(Collectors.joining("\n"));
        return result;
    }
}
