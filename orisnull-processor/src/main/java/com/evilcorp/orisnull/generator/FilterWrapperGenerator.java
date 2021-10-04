package com.evilcorp.orisnull.generator;

import com.evilcorp.orisnull.domain.BetterQueryParsingReplace;
import com.evilcorp.orisnull.domain.QueryParams;
import com.evilcorp.orisnull.model.BetterClass;
import com.evilcorp.orisnull.model.Field;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class FilterWrapperGenerator {
    private final BetterClass filter;

    public FilterWrapperGenerator(BetterClass filter) {
        this.filter = filter;
    }

    MethodSpec fieldEnabled() {
        final var builder = CodeBlock.builder()
                .beginControlFlow("switch(paramName)")
                ;
        for (Field field : filter.fields()) {
            builder.addStatement(CodeBlock.builder()
                    .add("case \"$L\":", field.name())
                    .add("return filter.get$L() != null", capitalize(field.name())).build())
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

    String capitalize(String in) {
        return in.toUpperCase().charAt(0) + in.substring(1);
    }

    public MethodSpec cleanQuery() {
        return MethodSpec.methodBuilder("cleanQuery")
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "query")
                .addStatement("return params.cleanQuery(query)")
                .build();
    }

    public TypeSpec.Builder classAndConstructor() {
        final ClassName filterName = ClassName.get(filter.packageName(), filter.shortName());
        final var helper = TypeSpec.classBuilder(filter.shortName() + "Helper")
                .addSuperinterface(ClassName.get(QueryParams.class))
                ;
        helper.addField(filterName, "filter", Modifier.PRIVATE);
        helper.addField(ClassName.get(BetterQueryParsingReplace.class), "params", Modifier.PRIVATE, Modifier.FINAL);
        helper.addMethod(MethodSpec.constructorBuilder()
                .addParameter(filterName, "filter")
                .addModifiers(Modifier.PUBLIC)
                .addStatement(CodeBlock.builder()
                        .add("this.filter = filter")
                        .build())
                .addStatement("this.params = new BetterQueryParsingReplace(this)")
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
        helper.addMethod(this.fieldEnabled());
        helper.addMethod(this.fields(filter.fields()));
        helper.addMethod(this.cleanQuery());
        return helper.build();
    }

}
