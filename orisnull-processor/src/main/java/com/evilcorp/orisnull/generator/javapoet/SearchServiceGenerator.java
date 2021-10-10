package com.evilcorp.orisnull.generator.javapoet;

import com.evilcorp.orisnull.model.OrIsNullClass;
import com.evilcorp.orisnull.model.OrIsNullSearchMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;

public class SearchServiceGenerator {

    private final OrIsNullClass orIsNullSearchInterface;
    private final List<OrIsNullSearchMethod> methods;

    public SearchServiceGenerator(
             OrIsNullClass orIsNullSearchInterface
            , List<OrIsNullSearchMethod> methods
    ) {
        this.orIsNullSearchInterface = orIsNullSearchInterface;
        this.methods = methods;
    }

    public void toFiler(Filer filer) {
        final var spec = TypeSpec.classBuilder(orIsNullSearchInterface.shortName() + "Impl");
        spec.addModifiers(Modifier.PUBLIC);
        spec.addSuperinterface(ClassName.get(orIsNullSearchInterface.packageName(), orIsNullSearchInterface.shortName()));
        spec.addAnnotation(ClassName.get("org.springframework.stereotype", "Service"));
        spec.addField(FieldSpec.builder(ClassName.get("javax.persistence", "EntityManagerFactory"), "emf", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired"))
                .build());

        for (OrIsNullSearchMethod method : methods) {
            final var searchMethodGenerator = new SearchMethodGenerator(method);
            spec.addMethod(searchMethodGenerator.searchQueryMethod());
        }

        JavaFile javaFile = JavaFile.builder(orIsNullSearchInterface.packageName(), spec.build())
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
