package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.evilcorp.orisnull.filter.BookFilterBuilder.book;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;


    @Nested
    class QueryAnnotationTest {
        @Test
        void nonExistingFilter() {
            final var all = bookRepository.findByFilterOrIsNull(book().build());
            assertEquals(5, all.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterOrIsNull(book().author("Pelevin").build());
            assertEquals(2, pelevin.size());
        }
    }

    @Nested
    class TypicalJpqlTest {
        @Test
        void nonExistingFilter() {
            final var all = bookRepository.findByFilterJpqlTypical(book().build());
            assertEquals(0, all.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterJpqlTypical(book().author("Pelevin").build());
            assertEquals(2, pelevin.size());
        }
    }

    @BeforeAll
//    @PostConstruct
    public void setUp() {
        final var books = List.of(
                new Book(
                        "Lord of the rings",
                        "Tolkien",
                        5,
                        "Great Britain"
                ),
                new Book(
                        "Harry Potter",
                        "Rowling",
                        5,
                        "Great Britain"
                ),
                new Book(
                        "Sol Invictus",
                        "Pelevin",
                        4,
                        "Russia"
                ),
                new Book(
                        "Вор в Законе",
                        "Лютый",
                        3,
                        "Russia"
                ),
                new Book(
                        "Generation P",
                        "Pelevin",
                        5,
                        "Russia"
                )
        );
        for (Book book : books) {
            if (!bookRepository.existsByName(book.getName())) {
                bookRepository.save(book);

            }
        }
    }
}