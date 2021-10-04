package com.evilcorp.orisnull;

import com.evilcorp.orisnull.generator.SearchServiceGenerator;
import com.evilcorp.orisnull.model.SearchMethod;
import com.evilcorp.orisnull.model.SimpleBetterClass;
import com.evilcorp.orisnull.model.SimpleField;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchServiceGeneratorTest {
    @Test
    void generate() {
        final var filter = new SimpleBetterClass("entities.TestFilter",
                List.of(
                        new SimpleField("author", "String")
                )
        );
        final var entity = new SimpleBetterClass("entities.Book", Collections.emptyList());
        SearchServiceGenerator searchServiceGenerator = new SearchServiceGenerator(
                filter,
                List.of(
                new SearchMethod(
                        filter,
                        entity,
                        "findBooks",
                        "bookre"
                )
                )
        );

        System.out.println(searchServiceGenerator.body());

        Map<String, String> map = new HashMap<>();
        map.put("notNull", "exists");
        map.put("null", null);
        System.out.println(map);
        System.out.println(map.keySet());
    }
}
