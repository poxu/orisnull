package com.evilcorp.orisnull.generator;

import com.evilcorp.orisnull.model.SearchMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.Collections;

public class SearchMethodGenerator {
    private final SearchMethod method;

    public SearchMethodGenerator(SearchMethod method) {
        this.method = method;
    }

    public MethodSpec searchQueryMethod() {
        ClassName entityClass = ClassName.get(method.entity().packageName(), method.entity().shortName());
        ClassName entityListClass = ClassName.get("java.util", "List");
        TypeName returnTypeClass = ParameterizedTypeName.get(entityListClass, entityClass);
        ClassName filterName = ClassName.get(method.filter().packageName(), method.filter().shortName());

        final var mainMethod = MethodSpec.methodBuilder(method.name())
                .addAnnotation(ClassName.get(Override.class))
                .addModifiers(Modifier.PUBLIC)
                .returns(returnTypeClass)
                .addParameter(ParameterSpec.builder(filterName, "filter").build())
                .beginControlFlow("if (filter.isEmpty()) ")
                .addStatement("return $T.emptyList()", Collections.class)
                .endControlFlow()
                .addStatement("$1L orisnull = new $1L(filter)", method.filter().shortName() + "Helper")
                .addStatement("String sql = $S", method.query())
                .addStatement("final var em = emf.createEntityManager()")
                .addStatement("final var query = orisnull.toQuery(em, sql)")
                .addStatement("return query.getResultList()")
                .build();
        return mainMethod;
    }
}
