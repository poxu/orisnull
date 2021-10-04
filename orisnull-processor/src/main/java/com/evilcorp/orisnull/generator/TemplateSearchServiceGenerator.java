package com.evilcorp.orisnull.generator;

import com.evilcorp.orisnull.generator.template.FreeKlass;
import com.evilcorp.orisnull.generator.template.FreeMethod;
import com.evilcorp.orisnull.model.BetterClass;
import com.evilcorp.orisnull.model.SearchMethod;
import com.evilcorp.orisnull.model.SimpleBetterClass;
import com.evilcorp.orisnull.model.SimpleField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateSearchServiceGenerator {

    private final BetterClass orIsNullSearchInterface;
    private final List<SearchMethod> methods;

    public TemplateSearchServiceGenerator(
            BetterClass orIsNullSearchInterface
            , List<SearchMethod> methods
    ) {
        this.orIsNullSearchInterface = orIsNullSearchInterface;
        this.methods = methods;
    }


    public void toFiler(Filer filer) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        try {
            cfg.setDirectoryForTemplateLoading(new File("/home/riptor/projects/orisnull/orisnull-processor/src/main/resources"));

            // Set the preferred charset template files are stored in. UTF-8 is
// a good choice in most applications:
            cfg.setDefaultEncoding("UTF-8");

// Sets how errors will appear.
// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
            cfg.setLogTemplateExceptions(false);


            Template temp = cfg.getTemplate("service.ftpl");
            Map<String, Object> root = new HashMap<>();
            root.put("packageName", orIsNullSearchInterface.packageName());
            root.put("klass", new FreeKlass(orIsNullSearchInterface));
            root.put("methods", methods.stream().map(m -> new FreeMethod(m)).collect(Collectors.toList()));
//            Writer out = new OutputStreamWriter(System.out);
            final JavaFileObject sourceFile = filer.createSourceFile(orIsNullSearchInterface.name() + "Impl");
//            Writer out = new OutputStreamWriter(System.out);
            Writer out = new OutputStreamWriter(sourceFile.openOutputStream());


            temp.process(root, out);
            out.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

// Wrap unchecked exceptions thrown during template processing into TemplateException-s:
//        cfg.wrap

// Do not fall back to higher scopes when reading a null loop variable:
//        cfg.setFallbackOnNullLoopVariable(false);

    }

    public static void main(String[] args) {
        BetterClass entity = new SimpleBetterClass( "com.evilcorp.Book",
                List.of(
                        new SimpleField("first", "String"),
                        new SimpleField("second", "Integer")
                )
        );

        BetterClass filter = new SimpleBetterClass( "com.evilcorp.BookFilter",
                List.of(
                        new SimpleField("first", "String"),
                        new SimpleField("second", "Integer")
                )
        );

        final SearchMethod findAll = new SearchMethod(filter, entity, "findAll", "");


        BetterClass klass = new SimpleBetterClass("test.ClassName",
                Collections.emptyList()
        );
        TemplateSearchServiceGenerator templateSearchServiceGenerator = new TemplateSearchServiceGenerator(klass, List.of(findAll));
        templateSearchServiceGenerator.toFiler(null);
    }
}
