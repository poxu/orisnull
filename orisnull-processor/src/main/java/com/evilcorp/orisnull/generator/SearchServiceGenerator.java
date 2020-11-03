package com.evilcorp.orisnull.generator;

import com.evilcorp.orisnull.model.BetterClass;
import com.evilcorp.orisnull.model.SearchMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public JavaFile file() {
        final var spec = TypeSpec.classBuilder(orIsNullSearchInterface.shortName() + "Impl");
        spec.addModifiers(Modifier.PUBLIC);
        spec.addSuperinterface(ClassName.get(orIsNullSearchInterface.packageName(), orIsNullSearchInterface.shortName()));
        spec.addAnnotation(ClassName.get("org.springframework.stereotype", "Service"));
        spec.addField(FieldSpec.builder(ClassName.get("javax.persistence", "EntityManagerFactory"), "emf", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired"))
                .build());

        Map<String, TypeSpec> wrappers = new HashMap<>();

        for (SearchMethod method : methods) {
            final var searchMethodGenerator = new SearchMethodGenerator(method);
            spec.addMethod(searchMethodGenerator.searchQueryMethod());

            if (!wrappers.containsKey(method.filter().name())) {
                final var generator = new FilterWrapperGenerator(
                        method.filter(),
                        method.entity()
                );
                final var wrapper = generator.generateWrapper();
                wrappers.put(method.filter().name(), wrapper);
                spec.addType(wrapper);
            }
        }

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
