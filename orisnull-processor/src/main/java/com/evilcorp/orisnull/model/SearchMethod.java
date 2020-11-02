package com.evilcorp.orisnull.model;

public class SearchMethod {
    private final BetterClass filter;
    private final BetterClass entity;
    private final String name;
    private final String query;

    public SearchMethod(BetterClass filter, BetterClass entity, String name, String query) {
        this.filter = filter;
        this.entity = entity;
        this.name = name;
        this.query = query;
    }

    public String name() {
        return name;
    }

    public BetterClass entity() {
        return entity;
    }

    public BetterClass filter() {
        return filter;
    }

    public String query() {
        return query;
    }
}
