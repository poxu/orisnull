package com.evilcorp.orisnull.generator.template;

import com.evilcorp.orisnull.model.OrIsNullClass;
import com.evilcorp.orisnull.model.OrIsNullSearchMethod;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateSearchServiceGenerator {

    private final OrIsNullClass orIsNullSearchInterface;
    private final List<OrIsNullSearchMethod> methods;

    public TemplateSearchServiceGenerator(
            OrIsNullClass orIsNullSearchInterface
            , List<OrIsNullSearchMethod> methods
    ) {
        this.orIsNullSearchInterface = orIsNullSearchInterface;
        this.methods = methods;
    }


    public void toFiler(Filer filer) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        try {
            cfg.setClassForTemplateLoading(this.getClass(), "/");

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
            root.put("methods", methods.stream().map(FreeMethod::new)
                    .collect(Collectors.toList()));
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
}
