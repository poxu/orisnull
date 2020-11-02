package com.evilcorp.orisnull.generator;

import com.evilcorp.orisnull.domain.BetterQuery;
import com.evilcorp.orisnull.domain.QueryParams;
import com.evilcorp.orisnull.model.BetterClass;
import com.evilcorp.orisnull.model.Field;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class SearchGenerator {

    private final BetterClass filter;
    private final BetterClass entity;
    private final BetterClass iface;
    private final String methodName;
    private final String className;
    private final String packageName;
    private final String query;

    public SearchGenerator(
            BetterClass filter
            , BetterClass entity
            , BetterClass iface
            , String methodName
            , String className
            , String packageName
            , String query
    ) {
        this.filter = filter;
        this.entity = entity;
        this.iface = iface;
        this.methodName = methodName;
        this.className = className;
        this.packageName = packageName;
        this.query = query;
    }

    public void toFile(File file) {
        final var out = file();
        try {
            out.writeTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String body() {
        StringBuilder builder = new StringBuilder();
        final var file = file();
        try {
            file.writeTo(builder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    MethodSpec toQuery() {
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

    public TypeSpec filterWrapper() {
        final ClassName filterName = ClassName.get(filter.packageName(), filter.shortName());
        final var helper = TypeSpec.classBuilder(filter.shortName() + "Helper")
                .addSuperinterface(ClassName.get(QueryParams.class))
                ;
        helper.addField(filterName, "filter", Modifier.PRIVATE);
        helper.addField(ClassName.get(BetterQuery.class), "params", Modifier.PRIVATE, Modifier.FINAL);
        helper.addMethod(MethodSpec.constructorBuilder()
                .addParameter(filterName, "filter")
                .addModifiers(Modifier.PUBLIC)
                .addStatement(CodeBlock.builder()
                        .add("this.filter = filter")
                        .build())
                .addStatement("this.params = new BetterQuery(this)")
                .build());
        for (Field field : filter.fields()) {
            final var methodSpec = MethodSpec.methodBuilder(field.name())
                    .returns(TypeName.BOOLEAN)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return filter.get$L() == null", capitalize(field.name()))
                    .build();
            helper.addMethod(methodSpec);
        }

        for (Field field : filter.fields()) {
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
            helper.addMethod(methodSpec);
        }
        helper.addMethod(fieldEnabled());
        helper.addMethod(toQuery());
        return helper.build();
    }

    String capitalize(String in) {
        return in.toUpperCase().charAt(0) + in.substring(1);
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

    public JavaFile file() {
        final var spec = TypeSpec.classBuilder(className + "Impl");
        spec.addModifiers(Modifier.PUBLIC);
        spec.addSuperinterface(ClassName.get(iface.packageName(), iface.shortName()));
        spec.addAnnotation(ClassName.get("org.springframework.stereotype", "Service"));
        spec.addField(FieldSpec.builder(ClassName.get("javax.persistence", "EntityManagerFactory"), "emf", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired"))
                .build());
        //ClassName iface = ClassName.get()
        ClassName entityName = ClassName.get(entity.packageName(), entity.shortName());
        ClassName list = ClassName.get("java.util", "List");
        TypeName methodResult = ParameterizedTypeName.get(list, entityName);
        ClassName filterName = ClassName.get(filter.packageName(), filter.shortName());
        final var mainMethod = MethodSpec.methodBuilder(methodName)
                .addAnnotation(ClassName.get(Override.class))
                .addModifiers(Modifier.PUBLIC)
                .returns(methodResult)
                .addParameter(ParameterSpec.builder(filterName, "filter").build())
                .beginControlFlow("if (filter.isEmpty()) ")
                .addStatement("return $T.emptyList()", Collections.class)
                .endControlFlow()
                .addStatement("$1L orisnull = new $1L(filter)", filter.shortName() + "Helper" )
                .addStatement("String sql = $S", query)
                .addStatement("final var em = emf.createEntityManager()")
                .addStatement("final var query = orisnull.toQuery(em, sql)")
                .addStatement("return query.getResultList()")
                .build();
        spec.addMethod(mainMethod);


        spec.addType(filterWrapper());
        JavaFile javaFile = JavaFile.builder(packageName, spec.build())
                .build();

        return javaFile;
    }

    public void toFiler(Filer filer) {
        final var out = file();
        try {
            out.writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
