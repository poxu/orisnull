package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.evilcorp.orisnull.filter.BookFilterBuilder.book;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Nested
    class SpecTest {
        @Test
        void nonExistingFilter() {
            final var pelevin = bookRepository.searchBySpecFilter(book()
                    .build());
            assertEquals(0, pelevin.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.searchBySpecFilter(book()
                    .author("Pelevin")
                    .rating(4)
                    .build());
            assertEquals(1, pelevin.size());
        }
    }

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
    class AlternativeQueryAnnotationTest {
        @Test
        void nonExistingFilter() {
            final var all = bookRepository.findByFilterOrIsNullIncorrectlyFixed(book().build());
            assertEquals(5, all.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterOrIsNullIncorrectlyFixed(book().author("Pelevin").build());
            assertEquals(2, pelevin.size());
        }
    }

    @Nested
    class FixedQueryAnnotationTest {
        @Test
        void nonExistingFilter() {
            final var all = bookRepository.findByFilterOrIsNullFixed(book().build());
            assertEquals(0, all.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterOrIsNullFixed(book().author("Pelevin").build());
            assertEquals(2, pelevin.size());
        }
    }

    @Nested
    class JpqlTest {
        @Test
        void nonExistingFilter() {
            final var pelevin = bookRepository.findByFilterJpql(book().build());
            assertEquals(0, pelevin.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterJpql(book()
                    .author("Pelevin")
                    .rating(5)
                    .build());
            assertEquals(1, pelevin.size());
        }
    }

    @Nested
    class NativeTest {
        @Test
        void nonExistingFilter() {
            final var pelevin = bookRepository.findByFilterNative(book().build());
            assertEquals(0, pelevin.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterNative(book()
                    .author("Pelevin")
                    .rating(5)
                    .build());
            assertEquals(1, pelevin.size());
        }
    }

    @Nested
    class OrIsNullNativeTest {
        @Test
        void nonExistingFilter() {
            final var pelevin = bookRepository.findByFilterNativeOrIsNull(book().build());
            assertEquals(0, pelevin.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterNativeOrIsNull(book()
                    .author("Pelevin")
                    .rating(5)
                    .build());
            assertEquals(1, pelevin.size());
        }
    }

    @Nested
    class JpqlWithCrazyHelperTest {
        @Test
        void nonExistingFilter() {
            final var pelevin = bookRepository.findByFilterNativeWithCrazyHelper(book().build());
            assertEquals(0, pelevin.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterNativeWithCrazyHelper(book()
                    .author("Pelevin")
                    .rating(5)
                    .build());
            assertEquals(1, pelevin.size());
        }
    }

    @Nested
    class JpqlWithHelperTest {
        @Test
        void nonExistingFilter() {
            final var pelevin = bookRepository.findByFilterJpqlWithHelper(book().build());
            assertEquals(0, pelevin.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterJpqlWithHelper(book()
                    .author("Pelevin")
                    .rating(5)
                    .build());
            assertEquals(1, pelevin.size());
        }
    }

    @Nested
    class SearchByExampleTest {
        @Test
        void nonExistingFilter() {
            final var pelevin = bookRepository.findByFilterExample(book().build());
            assertEquals(0, pelevin.size());
        }

        @Test
        void existingFilter() {
            final var pelevin = bookRepository.findByFilterExample(book().author("Pelevin").build());
            assertEquals(2, pelevin.size());
        }
    }

    @BeforeAll
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
        bookRepository.saveAll(books);
    }
}