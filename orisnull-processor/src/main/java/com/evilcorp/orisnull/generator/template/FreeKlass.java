package com.evilcorp.orisnull.generator.template;

import com.evilcorp.orisnull.model.OrIsNullClass;

import java.util.List;
import java.util.stream.Collectors;

public class FreeKlass {
    private String packageName;
    private String fullname;
    private String shortname;
    private List<FreeField> fields;

    public FreeKlass(OrIsNullClass orIsNullSearchInterface) {
        shortname = orIsNullSearchInterface.shortName();
        fullname = orIsNullSearchInterface.name();
        packageName = orIsNullSearchInterface.packageName();
        fields = orIsNullSearchInterface.fields().stream()
                .map(f -> new FreeField(f.name(), f.type(), f.betterClass()))
                .collect(Collectors.toList());
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public List<FreeField> getFields() {
        return fields;
    }

    public void setFields(List<FreeField> fields) {
        this.fields = fields;
    }
}
