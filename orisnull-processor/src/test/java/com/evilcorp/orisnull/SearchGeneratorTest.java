package com.evilcorp.orisnull;

import com.evilcorp.orisnull.generator.SearchGenerator;
import com.evilcorp.orisnull.model.SimpleBetterClass;
import com.evilcorp.orisnull.model.SimpleField;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class SearchGeneratorTest {
    @Test
    void generate() {
        SearchGenerator searchGenerator = new SearchGenerator(
                new SimpleBetterClass("entities.TestFilter",
                        List.of(
                                new SimpleField("author", "String")
                        )
                    )
                ,
                new SimpleBetterClass("entities.Book", Collections.emptyList())
        ,new SimpleBetterClass("entities.Book", Collections.emptyList())
                ,
                "findBooks"
                , "Bookre"
                , "com.evilcopr"

                );

        System.out.println(searchGenerator.body());
    }
}
