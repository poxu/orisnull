package com.evilcorp.orisnull.filter;

import com.evilcorp.orisnull.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FindBookCrazyHelper {
    private final BookFilter filter;

    public FindBookCrazyHelper(BookFilter filter) {
        this.filter = filter;
    }

    public boolean name() {
        return filter.getName() == null;
    }

    public boolean country() {
        return filter.getCountry() == null;
    }

    public boolean author() {
        return filter.getAuthor() == null;
    }

    public boolean rating() {
        return filter.getRating() == null;
    }

    public void author(TypedQuery<Book> query, String name) {
        if (!author()) {
            query.setParameter(name, filter.getAuthor());
        }
    }

    public void country(TypedQuery<Book> query, String name) {
        if (!country()) {
            query.setParameter(name, filter.getCountry());
        }
    }

    public void name(TypedQuery<Book> query, String name) {
        if (!name()) {
            query.setParameter(name, filter.getName());
        }
    }

    public void rating(TypedQuery<Book> query, String name) {
        if (!rating()) {
            query.setParameter(name, filter.getRating());
        }
    }

    boolean fieldEnabled(String paramName) {
        switch (paramName) {
            case ":author":
                return !author();
            case ":country":
                return !country();
            case ":name":
                return !name();
            case ":rating":
                return !rating();
            default:
                throw new IllegalArgumentException("Unexpected parameterName " + paramName);
        }
    }

    boolean lineToRemove(String line) {
        if (!line.contains("-- op")) {
            return false;
        }
        final var paramStartIdx = line.indexOf(':');
        if (paramStartIdx == -1) {
            throw new IllegalArgumentException("Line has comments, but doesn't contain a parameter");
        }
        final var paramEndIdx = line.indexOf(' ', paramStartIdx);

        final var paramName = line.substring(paramStartIdx, paramEndIdx);
        return !fieldEnabled(paramName);
    }

    String removeComments(String line) {
        if (!line.contains("-- op")) {
            return line;
        }
        final var commentsStartIdx = line.indexOf("-- op");
        final var substring = line.substring(0, commentsStartIdx);
        return substring;
    }

    public TypedQuery<Book> toQuery(EntityManager em, String query) {
        final var lines = query.split("\n");
        final var result = Arrays.stream(lines)
                .filter(l -> !lineToRemove(l))
                .map(l -> removeComments(l))
                .collect(Collectors.joining("\n"));
        System.out.println(result);

        final var jpqlQuery = em.createQuery(result, Book.class);

        author(jpqlQuery, "author");
        country(jpqlQuery, "country");
        name(jpqlQuery, "name");
        rating(jpqlQuery, "rating");
        return jpqlQuery;
    }

}
