package com.evilcorp.orisnull.generator.template;

import com.evilcorp.orisnull.model.BetterClass;

public class FreeField {
    private String name;
    private String type;
    private FreeKlass klass;

    public FreeField(String name, String type, BetterClass betterClass) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FreeKlass getKlass() {
        return klass;
    }

    public void setKlass(FreeKlass klass) {
        this.klass = klass;
    }

    public String getGetter() {
        return "get" + name.toUpperCase().charAt(0) + name.substring(1);
    }
}
