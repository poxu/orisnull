package com.evilcorp.orisnull.model;

public class SimpleOrIsNullField implements OrIsNullField {
    private final String name;
    private final String type;

    public SimpleOrIsNullField(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public OrIsNullClass betterClass() {
        return null;
    }
}
