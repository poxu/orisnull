package com.evilcorp.orisnull.processor;

import com.evilcorp.orisnull.generator.SearchGenerator;
import com.evilcorp.orisnull.model.AnnotatedBetterClass;
import com.evilcorp.orisnull.model.BetterClass;
import com.evilcorp.orisnull.model.SimpleBetterClass;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({"com.evilcorp.orisnull.annotation.OrIsNullRepository"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class OrIsNullProcessor extends AbstractProcessor {
    private Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.types = processingEnv.getTypeUtils();
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final PrintWriter printWriter;
        try {
            printWriter = new PrintWriter("/home/riptor/tmp/debug/sout-" + System.currentTimeMillis() + ".tst");
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
        getSupportedAnnotationTypes().forEach(printWriter::println);
        printWriter.println("started");
        printWriter.println("found annotations " + annotations.size());
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements
                    = roundEnv.getElementsAnnotatedWith(annotation);
            printWriter.println("found classes " + annotatedElements.size());
            for (Element annotatedElement : annotatedElements) {

                printWriter.println("found class " + annotatedElement.getSimpleName());
                final List<? extends Element> collect = annotatedElement.getEnclosedElements()
                        .stream()
                        .filter(e -> e.getKind() == ElementKind.METHOD)
                        .collect(Collectors.toList());
                printWriter.println("forund methods " + collect.size());
                if (collect.size() != 1) {
                    continue;
                }
                BetterClass iface = new AnnotatedBetterClass(annotatedElement);

                Element method = collect.iterator().next();
                printWriter.println(method.toString());
                printWriter.println(method.getEnclosedElements().size());
                printWriter.println(method.getKind());
                final var typeMirror = (ExecutableType) method.asType();
                if (typeMirror.getParameterTypes().size() != 1) {
                    continue;
                }
                final var methodSimpleName = method.getSimpleName().toString();

                final TypeMirror paratemeter = typeMirror.getParameterTypes().iterator().next();
                BetterClass filter = new AnnotatedBetterClass(processingEnv.getTypeUtils().asElement(paratemeter));

                filter.fields().forEach(f -> {
                    printWriter.println(f.name());
                    printWriter.println(f.type());
                });

                printWriter.println(typeMirror.getReturnType());

               final var returnTypeName = typeMirror.getReturnType().toString();

                if (!returnTypeName.startsWith("java.util.List")) {
                    continue;
                }

                final var book = returnTypeName.replaceAll("^.*<(.*)>", "$1");
                printWriter.println(book);
                BetterClass entity = new SimpleBetterClass(book, Collections.emptyList());
                final var searchGenerator = new SearchGenerator(
                        filter,
                        entity,
                        iface,
                        methodSimpleName,
                        iface.shortName(),
                        iface.packageName()
                );
                searchGenerator.toFile(new File("/home/riptor/tmp/"));
                searchGenerator.toFiler(processingEnv.getFiler());


                /*
                typeMirror.getParameterTypes().forEach(
                        p -> {
                            printWriter.println(p.toString());
                            processingEnv.getTypeUtils().asElement(p).getEnclosedElements()
                                    .stream()
                                    .filter(e -> e.getKind() == ElementKind.FIELD)
                                    .forEach(e -> {
                                        printWriter.println(e.toString());
                                        printWriter.println(e.asType().toString());
                                        final var simpleName = processingEnv.getTypeUtils().asElement(e.asType())
                                                .getSimpleName();
                                        printWriter.println(simpleName);
                                    });
                        });
                */
                //annotatedElement.
                //processingEnv.getFiler();
            }
        }
        printWriter.close();
        return true;
    }
}
