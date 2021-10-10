package com.evilcorp.orisnull.model;

public class SimpleOrIsNullSearchMethod implements OrIsNullSearchMethod {
    private final OrIsNullClass filter;
    private final OrIsNullClass entity;
    private final String name;
    private final String query;

    public SimpleOrIsNullSearchMethod(OrIsNullClass filter, OrIsNullClass entity, String name, String query) {
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
    public OrIsNullClass entity() {
        return entity;
    }

    @Override
    public OrIsNullClass filter() {
        return filter;
    }

    @Override
    public String query() {
        return query;
    }
}
