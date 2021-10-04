package com.evilcorp.orisnull.processor;

import com.evilcorp.orisnull.annotation.OrIsNullQuery;
import com.evilcorp.orisnull.annotation.OrIsNullRepository;
import com.evilcorp.orisnull.generator.SearchServiceGenerator;
import com.evilcorp.orisnull.generator.TemplateSearchServiceGenerator;
import com.evilcorp.orisnull.model.AnnotatedBetterClass;
import com.evilcorp.orisnull.model.BetterClass;
import com.evilcorp.orisnull.model.SearchMethod;
import com.evilcorp.orisnull.model.SimpleBetterClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({"com.evilcorp.orisnull.annotation.OrIsNullRepository"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class OrIsNullProcessor extends AbstractProcessor {
    private final Logger logger = LogManager.getLogger(OrIsNullProcessor.class);

    private Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.types = processingEnv.getTypeUtils();
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        getSupportedAnnotationTypes().forEach(at -> logger.debug(() -> at));
        logger.debug(() -> "started");
        logger.debug(() -> "found annotations " + annotations.size());
        for (TypeElement orIsNullAnnotation : annotations) {
            Set<? extends Element> annotatedElements
                    = roundEnv.getElementsAnnotatedWith(orIsNullAnnotation);
            logger.debug(() -> "found classes " + annotatedElements.size());
            for (Element orIsNullRepository : annotatedElements) {

                logger.debug(() -> "found class " + orIsNullRepository.getSimpleName());
                final List<? extends Element> queryMethodCandidates = orIsNullRepository.getEnclosedElements()
                        .stream()
                        .filter(e -> e.getKind() == ElementKind.METHOD)
                        .collect(Collectors.toList());
                logger.debug(() -> "forund methods " + queryMethodCandidates.size());

                BetterClass orIsNullInterface = new AnnotatedBetterClass(orIsNullRepository);

                List<SearchMethod> methods = new ArrayList<>();

                for (Element queryMethodCandidate : queryMethodCandidates) {
                    logger.debug(() -> queryMethodCandidate.toString());
                    logger.debug(() -> queryMethodCandidate.getEnclosedElements().size());
                    logger.debug(() -> queryMethodCandidate.getKind());

                    final var typeMirror = (ExecutableType) queryMethodCandidate.asType();
                    if (typeMirror.getParameterTypes().size() != 1) {
                        continue;
                    }
                    final var methodSimpleName = queryMethodCandidate.getSimpleName().toString();
                    final var query = queryMethodCandidate.getAnnotation(OrIsNullQuery.class);

                    if (query == null) {
                        continue;
                    }

                    logger.debug(() -> query.value());

                    final TypeMirror paratemeter = typeMirror.getParameterTypes().iterator().next();
                    BetterClass filter = new AnnotatedBetterClass(processingEnv.getTypeUtils().asElement(paratemeter));

                    filter.fields().forEach(f -> {
                        logger.debug(() -> f.name());
                        logger.debug(() -> f.type());
                    });

                    logger.debug(() -> typeMirror.getReturnType());

                    final var returnTypeName = typeMirror.getReturnType().toString();

                    if (!returnTypeName.startsWith("java.util.List<")) {
                        continue;
                    }

                    final var book = returnTypeName.replaceAll("^.*<(.*)>", "$1");
                    logger.debug(() -> book);
                    BetterClass entity = new SimpleBetterClass(book, Collections.emptyList());

                    methods.add(new SearchMethod(
                            filter,
                            entity,
                            methodSimpleName,
                            query.value()
                    ));
                }
                final String engine = orIsNullRepository.getAnnotation(OrIsNullRepository.class).engine();

                if ("".equals(engine)) {
                    final var searchGenerator = new SearchServiceGenerator(
                            orIsNullInterface,
                            methods
                    );
                    searchGenerator.toFiler(processingEnv.getFiler());
                } else {
                    final var searchGenerator = new TemplateSearchServiceGenerator(
                            orIsNullInterface,
                            methods
                    );
                    searchGenerator.toFiler(processingEnv.getFiler());
                }
            }
        }
        return true;
    }
}
