package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.Book;
import com.evilcorp.orisnull.entity.BookRead;
import com.evilcorp.orisnull.entity.BookReadId_;
import com.evilcorp.orisnull.entity.BookRead_;
import com.evilcorp.orisnull.entity.BookReader;
import com.evilcorp.orisnull.entity.BookReader_;
import com.evilcorp.orisnull.entity.Book_;
import com.evilcorp.orisnull.filter.BookFilterWithReader;
import com.evilcorp.orisnull.filter.FindBooksWithReaderHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BookReadRepositoryCustomImpl implements BookReadRepositoryCustom {

    @Autowired
    EntityManagerFactory emf;

    @Override
    public List<Book> findBooksByReaderName(BookFilterWithReader filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }
        FindBooksWithReaderHelper absent = new FindBooksWithReaderHelper(filter);
        //@formatter:off
        String sql =
        " select distinct b                                   " +
        " from                                                " +
        "        Book b                                       " + (absent.readers() ? "" :
        "   join BookRead bkr on bkr.id.bookId = b.id         " +
        "   join BookReader br on br.id = bkr.id.bookReaderId ") +
        " where                                               " +
        "       1=1                                           " + (absent.author() ? "" :
        "   and b.author = :author                            ") + (absent.country() ? "" :
        "   and b.country = :country                          ") + (absent.minRating() ? "" :
        "   and b.rating >= :minRating                        ") + (absent.maxRating() ? "" :
        "   and b.rating <= :maxRating                        ") + (absent.name() ? "" :
        "   and lower(b.name) = lower(:name)                  ") + (absent.readers() ? "" :
        "   and lower(br.name) in :readers                    ");
        //@formatter:on
        final var em = emf.createEntityManager();
        final var query = em.createQuery(sql, Book.class);

        if (!absent.author()) {
            query.setParameter("author", filter.getAuthor());
        }
        if (!absent.country()) {
            query.setParameter("country", filter.getCountry());
        }
        if (!absent.name()) {
            query.setParameter("name", filter.getName());
        }
        if (!absent.minRating()) {
            query.setParameter("minRating", filter.getMinRating());
        }
        if (!absent.maxRating()) {
            query.setParameter("maxRating", filter.getMaxRating());
        }
        if (!absent.readers()) {
            query.setParameter("readers", filter.getReaders()
                    .stream().map(String::toLowerCase).collect(Collectors.toList()));
        }
        return query.getResultList();
    }

    @Override
    public List<Book> findBooksByReaderNameCriteriaApi(BookFilterWithReader filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }
        FindBooksWithReaderHelper absent = new FindBooksWithReaderHelper(filter);
        final var em = emf.createEntityManager();
        final var cb = em.getCriteriaBuilder();
        final var query = cb.createQuery(Book.class);
        final var bookRoot = query.from(Book.class);
        final Root<BookReader> readerRoot = query.from(BookReader.class);
        final var bookReadRoot = query.from(BookRead.class);

        List<Predicate> preds = new ArrayList<>();
        if (!absent.readers()) {
            final CriteriaBuilder.In<String> readersIn = cb.in(cb.lower(readerRoot.get(BookReader_.name)));
            filter.getReaders().stream().map(String::toLowerCase).forEach(readersIn::value);
            final var join = cb.and(
                    cb.equal(bookRoot.get(Book_.id), bookReadRoot.get(BookRead_.id).get(BookReadId_.bookId)),
                    cb.equal(readerRoot.get(BookReader_.id), bookReadRoot.get(BookRead_.id).get(BookReadId_.bookReaderId)),
                    readersIn
            );
            preds.add(join);
        }
        if (!absent.name()) {
            preds.add(cb.equal(cb.lower(bookRoot.get(Book_.name)), filter.getName().toLowerCase()));
        }
        if (!absent.country()) {
            preds.add(cb.equal(cb.lower(bookRoot.get(Book_.country)), filter.getCountry().toLowerCase()));
        }
        if (!absent.rating()) {
            preds.add(cb.equal(bookRoot.get(Book_.rating), filter.getRating()));
        }
        if (!absent.author()) {
            preds.add(cb.equal(cb.lower(bookRoot.get(Book_.author)), filter.getAuthor().toLowerCase()));
        }
        query.select(bookRoot).distinct(true)
                .where(cb.and(preds.toArray(Predicate[]::new)));
        final var actualQuery = em.createQuery(query);
        return actualQuery.getResultList();
    }

    @Override
    public List<Book> findBooksByReaderNameNative(BookFilterWithReader filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }
        FindBooksWithReaderHelper absent = new FindBooksWithReaderHelper(filter);
        //@formatter:off
        String sql = "" +
        " select                                                           " +
        "   distinct b.*                                                   " +
        " from                                                             " +
        "        books b                                                   " + (absent.readers() ? "" :
        "   join books_readers bkr on bkr.book_id = b.book_id              " +
        "   join book_readers br on br.book_reader_id = bkr.book_reader_id ") +
        " where                                                            " +
        "       1=1                                                        " + (absent.author() ? "" :
        "   and b.author = :author                                         ") + (absent.country() ? "" :
        "   and b.country = :country                                       ") + (absent.minRating() ? "" :
        "   and b.rating >= :minRating                                     ") + (absent.maxRating() ? "" :
        "   and b.rating <= :maxRating                                     ") + (absent.name() ? "" :
        "   and lower(b.name) = lower(:name)                               ") + (absent.readers() ? "" :
        "   and lower(br.name) in :readers                                 ");
        //@formatter:on
        final var em = emf.createEntityManager();
        final var query = em.createNativeQuery(sql, Book.class);

        if (!absent.author()) {
            query.setParameter("author", filter.getAuthor());
        }
        if (!absent.country()) {
            query.setParameter("country", filter.getCountry());
        }
        if (!absent.name()) {
            query.setParameter("name", filter.getName());
        }
        if (!absent.minRating()) {
            query.setParameter("minRating", filter.getMinRating());
        }
        if (!absent.maxRating()) {
            query.setParameter("maxRating", filter.getMaxRating());
        }
        if (!absent.readers()) {
            query.setParameter("readers", filter.getReaders()
                    .stream().map(String::toLowerCase).collect(Collectors.toList()));
        }
        return query.getResultList();
    }
}
