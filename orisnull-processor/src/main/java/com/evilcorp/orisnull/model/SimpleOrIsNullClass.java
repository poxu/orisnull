package com.evilcorp.orisnull.model;

import java.util.List;

public class SimpleOrIsNullClass implements OrIsNullClass {
    private final String name;
    private final List<OrIsNullField> fields;

    public SimpleOrIsNullClass(String name, List<OrIsNullField> fields) {
        this.name = name;
        this.fields = fields;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<OrIsNullField> fields() {
        return fields;
    }
}
