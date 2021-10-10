package com.evilcorp.orisnull.model;

import com.evilcorp.orisnull.annotation.OrIsNullQuery;

import javax.lang.model.element.Element;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

public class AnnotatedSearchMethod implements SearchMethod {
    private final Element queryMethod;
    private final Types typeUtils;

    public AnnotatedSearchMethod(Element queryMethod, final Types typeUtils) {
        this.queryMethod = queryMethod;
        this.typeUtils = typeUtils;
    }

    public String name() {
        return queryMethod.getSimpleName().toString();
    }

    public BetterClass entity() {
        return new AnnotatedEntity(queryMethod);
    }

    public BetterClass filter() {
        final var queryMethod = (ExecutableType) this.queryMethod.asType();
        final TypeMirror queryMethodParameter = queryMethod.getParameterTypes().iterator().next();
        return new AnnotatedBetterClass(typeUtils.asElement(queryMethodParameter));
    }

    public String query() {
        return queryMethod.getAnnotation(OrIsNullQuery.class).value();
    }
}
