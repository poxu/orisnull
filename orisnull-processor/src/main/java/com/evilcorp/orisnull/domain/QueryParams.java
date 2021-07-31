package com.evilcorp.orisnull.domain;

import java.util.List;

public interface QueryParams {
    boolean fieldEnabled(String paramName);

    List<String> fields();
}
