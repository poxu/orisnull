package com.evilcorp.orisnull.model;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotatedBetterClass implements BetterClass {
    private final Element element;

    public AnnotatedBetterClass(Element element) {
        this.element = element;
    }

    @Override
    public String name() {
        return element.asType().toString();
    }

    @Override
    public List<Field> fields() {
        return element.getEnclosedElements().stream()
                .filter(e -> e.getKind().isField())
                .map(e -> new AnnotatedField(e))
                .collect(Collectors.toList());
    }
}
