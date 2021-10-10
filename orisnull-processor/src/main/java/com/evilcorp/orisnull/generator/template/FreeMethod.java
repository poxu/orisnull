package com.evilcorp.orisnull.generator.template;

import com.evilcorp.orisnull.model.OrIsNullSearchMethod;

public class FreeMethod {
    private FreeKlass filter;
    private FreeKlass entity;
    private String name;
    private String query;

    public FreeMethod(OrIsNullSearchMethod m) {
        filter = new FreeKlass(m.filter());
        entity = new FreeKlass(m.entity());
        name = m.name();
        query = m.query();
    }

    public String getAlignedQuery() {
        return query.replaceAll("\\n", " ");
    }
    public FreeKlass getFilter() {
        return filter;
    }

    public void setFilter(FreeKlass filter) {
        this.filter = filter;
    }

    public FreeKlass getEntity() {
        return entity;
    }

    public void setEntity(FreeKlass entity) {
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
