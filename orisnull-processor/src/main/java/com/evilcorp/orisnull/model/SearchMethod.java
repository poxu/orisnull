package com.evilcorp.orisnull.model;

public interface SearchMethod {
    String name();

    BetterClass entity();

    BetterClass filter();

    String query();
}
