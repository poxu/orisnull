package com.evilcorp.orisnull.generator;

import com.evilcorp.orisnull.domain.BetterQueryWithHints;
import com.evilcorp.orisnull.domain.QueryParams;
import com.evilcorp.orisnull.model.BetterClass;
import com.evilcorp.orisnull.model.Field;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class FilterWrapperGenerator {
    private final BetterClass filter;
    private final BetterClass entity;

    public FilterWrapperGenerator(BetterClass filter, BetterClass entity) {
        this.filter = filter;
        this.entity = entity;
    }

    public TypeSpec generate() {
        return null;
    }

    public MethodSpec toQuery() {
        final var em = ClassName.get("javax.persistence", "EntityManager");

        ClassName entityName = ClassName.get(entity.packageName(), entity.shortName());
        ClassName query = ClassName.get("javax.persistence", "TypedQuery");
        TypeName typedQuery = ParameterizedTypeName.get(query, entityName);

        final var builder = MethodSpec.methodBuilder("toQuery")
                .returns(typedQuery)
                .addParameter(ParameterSpec.builder(em, "em")
                        .build())
                .addParameter(ParameterSpec.builder(ClassName.get(String.class), "query")
                        .build())
                .addStatement("final String result = params.cleanQuery(query)")
                .addStatement("final var jpqlQuery = em.createQuery(result, $L.class)", entity.shortName());
        for (Field field : filter.fields()) {
            builder.addStatement("$1L(jpqlQuery, \"$1L\")", field.name());
        }
        builder.addStatement("return jpqlQuery");

        return builder.build();

    }

    MethodSpec fieldEnabled() {
        final var builder = CodeBlock.builder()
                .beginControlFlow("switch(paramName)")
                ;
        for (Field field : filter.fields()) {
            builder.addStatement(CodeBlock.builder()
                    .add("case \":$L\":", field.name())
                    .add("return !$L()", field.name()).build())
            ;
        }
        builder.addStatement(CodeBlock.builder().add("default:")
                .add("throw new $T(\"Unexpected parameterName \" + paramName)", IllegalArgumentException.class).build());
        ;
        builder.endControlFlow();

        return MethodSpec.methodBuilder("fieldEnabled")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.BOOLEAN)
                .addAnnotation(Override.class)
                .addParameter(ClassName.get(String.class), "paramName")
                .addCode(builder
                        .build()
                ).build();
    }

    public MethodSpec addQueryParameter(Field field) {
        ClassName entityName = ClassName.get(entity.packageName(), entity.shortName());
        ClassName query = ClassName.get("javax.persistence", "TypedQuery");
        TypeName typedQuery = ParameterizedTypeName.get(query, entityName);
        final var methodSpec = MethodSpec.methodBuilder(field.name())
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(typedQuery, "query")
                        .build())
                .addParameter(ParameterSpec.builder(ClassName.get(String.class), "name")
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.builder()
                        .beginControlFlow("if (!$L())", field.name())
                        .addStatement("query.setParameter(name, filter.get$L())", capitalize(field.name()))
                        .endControlFlow()
                        .build())
                .build();
        return methodSpec;
    }

    String capitalize(String in) {
        return in.toUpperCase().charAt(0) + in.substring(1);
    }

    public MethodSpec parameterIsAbsent(Field field) {
        final var methodSpec = MethodSpec.methodBuilder(field.name())
                .returns(TypeName.BOOLEAN)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return filter.get$L() == null", capitalize(field.name()))
                .build();
        return methodSpec;
    }

    public TypeSpec.Builder classAndConstructor() {
        final ClassName filterName = ClassName.get(filter.packageName(), filter.shortName());
        final var helper = TypeSpec.classBuilder(filter.shortName() + "Helper")
                .addSuperinterface(ClassName.get(QueryParams.class))
                ;
        helper.addField(filterName, "filter", Modifier.PRIVATE);
        helper.addField(ClassName.get(BetterQueryWithHints.class), "params", Modifier.PRIVATE, Modifier.FINAL);
        helper.addMethod(MethodSpec.constructorBuilder()
                .addParameter(filterName, "filter")
                .addModifiers(Modifier.PUBLIC)
                .addStatement(CodeBlock.builder()
                        .add("this.filter = filter")
                        .build())
                .addStatement("this.params = new BetterQueryWithHints(this)")
                .build());
        return helper;
    }

    public MethodSpec fields(List<Field> fields) {
        final String allFields = fields.stream()
                .map(f -> f.name())
                .map(f -> "\"" + f + "\"")
                .collect(Collectors.joining(","));

        ClassName stringClass = ClassName.get(String.class);
        ClassName listClass = ClassName.get("java.util", "List");
        TypeName returnTypeClass = ParameterizedTypeName.get(listClass, stringClass);

        final var methodSpec = MethodSpec.methodBuilder("fields")
                .returns(returnTypeClass)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return List.of(" + allFields + ")")
                .build();
        return methodSpec;
    }

    public TypeSpec generateWrapper() {
        final TypeSpec.Builder helper = this.classAndConstructor();

        for (Field field : filter.fields()) {
            final MethodSpec methodSpec = this.parameterIsAbsent(field);
            helper.addMethod(methodSpec);
        }

        for (Field field : filter.fields()) {
            final MethodSpec methodSpec = this.addQueryParameter(field);
            helper.addMethod(methodSpec);
        }
        helper.addMethod(this.fieldEnabled());
        helper.addMethod(this.toQuery());
        helper.addMethod(this.fields(filter.fields()));
        return helper.build();
    }

}
