package com.evilcorp.orisnull.model;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotatedBetterClass implements BetterClass {
    private final Element element;
    private final List<Field> fields;

    public AnnotatedBetterClass(Element element) {
        this.element = element;
        System.out.println("element.asType().getKind().getDeclaringClass().getSimpleName() = " + element.asType().getKind().getDeclaringClass().getSimpleName());
        System.out.println("element.asType() = " + element.asType());

        System.out.println("element = " + element);
        fields = element.getEnclosedElements().stream()
                .filter(e -> e.getKind().isField())
                .map(e -> new AnnotatedField(e))
                .collect(Collectors.toList());
    }

    @Override
    public String name() {
        return element.asType().toString();
    }

    @Override
    public List<Field> fields() {
        return fields;
    }
}
