package com.evilcorp.orisnull.model;

public class SimpleSearchMethod implements SearchMethod {
    private final BetterClass filter;
    private final BetterClass entity;
    private final String name;
    private final String query;

    public SimpleSearchMethod(BetterClass filter, BetterClass entity, String name, String query) {
        this.filter = filter;
        this.entity = entity;
        this.name = name;
        this.query = query;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public BetterClass entity() {
        return entity;
    }

    @Override
    public BetterClass filter() {
        return filter;
    }

    @Override
    public String query() {
        return query;
    }
}
