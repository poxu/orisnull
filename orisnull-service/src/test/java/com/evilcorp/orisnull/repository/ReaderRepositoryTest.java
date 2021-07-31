package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.Book;
import com.evilcorp.orisnull.entity.BookRead;
import com.evilcorp.orisnull.entity.BookReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.evilcorp.orisnull.filter.BookFilterWithReaderBuilder.builder;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReaderRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookReaderRepository bookReaderRepository;

    @Autowired
    BookReadRepository bookReadRepository;

    private Book lotr;
    private Book harryPotter;
    private Book generationP;
    private Book solInvictus;
    private Book wiseGuy;

    @Test
    void findBooksByReaderName() {
        //final var sarahBooks = bookReadRepository.findBooksByReaderName(Collections.singleton("Sarah Connor"));
        //assertThat(sarahBooks).containsExactlyInAnyOrder(lotr, harryPotter, generationP, solInvictus);
        final var booksByReaderName = bookReadRepository.findBooksByReaderName(
                builder()
                .readers(List.of("Sarah connor", "Kyle Reese"))
                .minRating(5)
                .maxRating(5)
                .build()
                );
        assertThat(booksByReaderName)
                .containsExactlyInAnyOrder(lotr, harryPotter, generationP);
    }

    @Test
    void findBooksByReaderNameCriteria() {
        final var booksByReaderName = bookReadRepository.findBooksByReaderNameCriteriaApi(
                builder()
                        .readers(List.of("Sarah connor", "Kyle ReEse"))
                        .rating(5)
                        .maxRating(5)
                        .minRating(4)
                        .build()
        );
        assertThat(booksByReaderName)
                .containsExactlyInAnyOrder(lotr, harryPotter, generationP);
    }

    @Test
    void findBooksByReaderNameNative() {
        final var booksByReaderName = bookReadRepository.findBooksByReaderNameNative(
                builder()
                        .readers(List.of("Sarah connor", "Kyle ReEse"))
                        .rating(5)
                        .maxRating(5)
                        .minRating(4)
                        .build()
        );
        assertThat(booksByReaderName)
                .containsExactlyInAnyOrder(lotr, harryPotter, generationP, solInvictus);
    }

    @BeforeAll
    public void setUp() {
        lotr = new Book(
                "Lord of the rings",
                "Tolkien",
                5,
                "Great Britain"
        );
        harryPotter = new Book(
                "Harry Potter",
                "Rowling",
                5,
                "Great Britain"
        );
        generationP = new Book(
                "Generation P",
                "Pelevin",
                5,
                "Russia"
        );
        solInvictus = new Book(
                "Sol Invictus",
                "Pelevin",
                4,
                "Russia"
        );
        wiseGuy = new Book(
                "Вор в Законе",
                "Лютый",
                3,
                "Russia"
        );
        final var books = List.of(
                lotr,
                harryPotter,
                solInvictus,
                wiseGuy,
                generationP
        );
        bookRepository.saveAll(books);

        final var johnConnor = new BookReader("John Connor");
        final var sarahConnor = new BookReader("Sarah Connor");
        final var kyleReese = new BookReader("Kyle Reese");
        final var bookReaders = List.of(
                johnConnor,
                sarahConnor,
                kyleReese
        );
        bookReaderRepository.saveAll(bookReaders);

        final var booksRead = Stream.of(
                read(kyleReese, lotr, harryPotter, wiseGuy),
                read(sarahConnor, lotr, harryPotter, solInvictus, generationP),
                read(johnConnor, lotr, harryPotter)
        )
                .flatMap(s -> s)
                .collect(Collectors.toList());
        bookReadRepository.saveAll(booksRead);
    }

    public static Stream<BookRead> read(final BookReader reader, Book... books) {
        return Arrays.stream(books)
                .map(b -> new BookRead(b.getId(), reader.getId()));
    }
}
