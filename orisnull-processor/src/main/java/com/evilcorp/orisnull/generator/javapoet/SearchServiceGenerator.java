package com.evilcorp.orisnull.generator.javapoet;

import com.evilcorp.orisnull.model.BetterClass;
import com.evilcorp.orisnull.model.SearchMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;

public class SearchServiceGenerator {

    private final BetterClass orIsNullSearchInterface;
    private final List<SearchMethod> methods;

    public SearchServiceGenerator(
             BetterClass orIsNullSearchInterface
            , List<SearchMethod> methods
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

        for (SearchMethod method : methods) {
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
