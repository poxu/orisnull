package com.evilcorp.orisnull.generator.template;

import com.evilcorp.orisnull.model.OrIsNullSearchMethod;

public class FreeMethod {
    private FreeKlass filter;
    private FreeKlass entity;
    OrIsNullSearchMethod method;
    public FreeMethod(OrIsNullSearchMethod method) {
        this.method = method;
        filter = new FreeKlass(method.filter());
        entity = new FreeKlass(method.entity());
    }

    public String getAlignedQuery() {
        return method.query().replaceAll("\\n", " ");
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
        return method.name();
    }

    public String getQuery() {
        return method.query();
    }
}
