package com.evilcorp.orisnull.filter;

import com.evilcorp.orisnull.entity.Book;

import javax.persistence.TypedQuery;

public class FindBookHelper {
    private final BookFilter filter;

    public FindBookHelper(BookFilter filter) {
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


}
