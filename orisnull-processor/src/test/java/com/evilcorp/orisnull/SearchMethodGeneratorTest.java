package com.evilcorp.orisnull;

import com.evilcorp.orisnull.generator.SearchMethodGenerator;
import com.evilcorp.orisnull.model.SearchMethod;
import com.evilcorp.orisnull.model.SimpleBetterClass;
import com.evilcorp.orisnull.model.SimpleField;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class SearchMethodGeneratorTest {
    @Test
    void generate() {
        final var filter = new SimpleBetterClass("entities.TestFilter",
                List.of(
                        new SimpleField("author", "String")
                )
        );
        final var entity = new SimpleBetterClass("entities.Book", Collections.emptyList());
        SearchMethodGenerator searchMethodGenerator = new SearchMethodGenerator(
                filter,
                new SearchMethod(
                        filter,
                        entity,
                        "findBooks",
                        "bookre"
                )
        );

        System.out.println(searchMethodGenerator.body());
    }
}
