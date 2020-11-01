package com.evilcorp.orisnull.model;

public class SimpleField implements Field {
    private final String name;
    private final String type;

    public SimpleField(String name, String type) {
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
    public BetterClass betterClass() {
        return null;
    }
}
