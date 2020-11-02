package com.evilcorp.orisnull.generator;

import com.evilcorp.orisnull.model.BetterClass;
import com.evilcorp.orisnull.model.SearchMethod;
import com.squareup.javapoet.ClassName;
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

public class SearchMethodGenerator {

    private final BetterClass orIsNullSearchInterface;
    private final SearchMethod method;

    public SearchMethodGenerator(
             BetterClass orIsNullSearchInterface
            , SearchMethod method
    ) {
        this.orIsNullSearchInterface = orIsNullSearchInterface;
        this.method = method;
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

    public TypeSpec filterWrapper() {
        FilterWrapperGenerator generator = new FilterWrapperGenerator(
                method.filter(),
                method.entity()
        );

        return generator.generateWrapper();
    }

    public JavaFile file() {
        final var spec = TypeSpec.classBuilder(orIsNullSearchInterface.shortName() + "Impl");
        spec.addModifiers(Modifier.PUBLIC);
        spec.addSuperinterface(ClassName.get(orIsNullSearchInterface.packageName(), orIsNullSearchInterface.shortName()));
        spec.addAnnotation(ClassName.get("org.springframework.stereotype", "Service"));
        spec.addField(FieldSpec.builder(ClassName.get("javax.persistence", "EntityManagerFactory"), "emf", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired"))
                .build());
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
        spec.addMethod(mainMethod);

        spec.addType(filterWrapper());
        JavaFile javaFile = JavaFile.builder(orIsNullSearchInterface.packageName(), spec.build())
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
