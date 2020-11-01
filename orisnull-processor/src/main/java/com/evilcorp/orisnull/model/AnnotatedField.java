package com.evilcorp.orisnull.model;

import javax.lang.model.element.Element;

public class AnnotatedField implements Field {
    private final Element element;

    public AnnotatedField(Element element) {
        this.element = element;
    }

    @Override
    public String name() {
        return element.getSimpleName().toString();
    }

    @Override
    public String type() {
        return element.asType().toString();
    }

    public BetterClass betterClass() {
        if (element.asType().getKind().isPrimitive()) {
            return null;
        }
        return new AnnotatedBetterClass(element);
    }

    public BetterClass getKlassType() {
        return betterClass();
    }
}
