package com.evilcorp.orisnull.filter;

import com.evilcorp.orisnull.domain.BetterQuery;
import com.evilcorp.orisnull.domain.QueryParams;
import com.evilcorp.orisnull.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class FindBookCrazyHelper implements QueryParams {
    private final BookFilter filter;
    private final BetterQuery params;

    public FindBookCrazyHelper(BookFilter filter) {
        this.filter = filter;
        this.params = new BetterQuery(this);
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

    @Override
    public boolean fieldEnabled(String paramName) {
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

    public TypedQuery<Book> toQuery(EntityManager em, String query) {
        final String result = params.cleanQuery(query);

        final var jpqlQuery = em.createQuery(result, Book.class);

        author(jpqlQuery, "author");
        country(jpqlQuery, "country");
        name(jpqlQuery, "name");
        rating(jpqlQuery, "rating");
        return jpqlQuery;
    }


}
