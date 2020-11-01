package com.evilcorp.orisnull.model;

import java.util.List;

public class SimpleBetterClass implements BetterClass {
    private final String name;
    private final List<Field> fields;

    public SimpleBetterClass(String name, List<Field> fields) {
        this.name = name;
        this.fields = fields;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<Field> fields() {
        return fields;
    }
}
