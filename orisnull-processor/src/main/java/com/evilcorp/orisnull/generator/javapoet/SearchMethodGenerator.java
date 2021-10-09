package com.evilcorp.orisnull.generator.javapoet;

import com.evilcorp.orisnull.domain.BetterQueryParsingReplace;
import com.evilcorp.orisnull.model.Field;
import com.evilcorp.orisnull.model.SearchMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.HashMap;

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

        var mainMethod = MethodSpec.methodBuilder(method.name())
                .addAnnotation(ClassName.get(Override.class))
                .addModifiers(Modifier.PUBLIC)
                .returns(returnTypeClass)
                .addParameter(ParameterSpec.builder(filterName, "filter").build())
                .beginControlFlow("if (filter.isEmpty()) ")
                .addStatement("return $T.emptyList()", Collections.class)
                .endControlFlow()
                .addStatement("var params = new $1T<String, Object>()", HashMap.class);

        method.filter().fields()
                .forEach(field -> mainMethod.addStatement("params.put(\"$1L\",filter.get$2L())"
                        , field.name(), capitalize(field.name())));

        mainMethod.addStatement("final var orisnull = new $1T(params)", BetterQueryParsingReplace.class)
                .addStatement("String sql = $S", method.query())
                .addStatement("final var em = emf.createEntityManager()")
                .addStatement("final String result = orisnull.cleanQuery(sql)")
                .addStatement("final var jpqlQuery = em.createQuery(result, $L.class)", method.entity().shortName())
                .addStatement("params.forEach(jpqlQuery::setParameter)")
        ;
//        for (Field field : method.filter().fields()) {
//            mainMethod.addStatement("jpqlQuery.setParameter(\"$1L\", filter.get$2L())"
//                    , field.name(), capitalize(field.name()));
//        }

        return mainMethod.addStatement("return jpqlQuery.getResultList()")
                .build();
    }

    String capitalize(String in) {
        return in.toUpperCase().charAt(0) + in.substring(1);
    }

}
