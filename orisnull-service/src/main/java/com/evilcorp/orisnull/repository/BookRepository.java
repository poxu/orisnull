package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.Book;
import com.evilcorp.orisnull.entity.Book_;
import com.evilcorp.orisnull.filter.BookFilter;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, UUID>, BookRepositoryCustom {
    @Query("" +
            " select b from Book b where                                           " +
            " 1=1                                                                  " +
            " and (b.author = :#{#filter.author}   or :#{#filter.author} is null)  " +
            " and (b.rating = :#{#filter.rating}   or :#{#filter.rating} is null)  " +
            " and (b.country = :#{#filter.country} or :#{#filter.country} is null) " +
            " and (b.name = :#{#filter.name}       or :#{#filter.name} is null)    "
    )
    List<Book> findByFilterOrIsNull(@Param("filter") BookFilter filter);

    @Query("" +
            " select b from Book b where                                           " +
            " 1=1                                                                  " +
            " and (b.author = :#{#filter.author}   or :#{#filter.author} is null)  " +
            " and (b.rating = :#{#filter.rating}   or :#{#filter.rating} is null)  " +
            " and (b.country = :#{#filter.country} or :#{#filter.country} is null) " +
            " and (b.name = :#{#filter.name}       or :#{#filter.name} is null)    " +
            " and (                                                                " +
            "          :#{#filter.author} is not null                              " +
            "       or :#{#filter.rating} is not null                              " +
            "       or :#{#filter.country} is not null                             " +
            "       or :#{#filter.name} is not null                                " +
            " )                                                                    "
    )
    List<Book> findByFilterOrIsNullFixed(@Param("filter") BookFilter filter);

    @Query("" +
            " select b from Book b where                                                    " +
            "        1=1                                                                    " +
            "   and (b.author = :#{#filter.author}   or :#{#filter.author == null} = true)  " +
            "   and (b.rating = :#{#filter.rating}   or :#{#filter.rating == null} = true)  " +
            "   and (b.country = :#{#filter.country} or :#{#filter.country == null} = true) " +
            "   and (b.name = :#{#filter.name}       or :#{#filter.name == null} = true)    "
    )
    List<Book> findByFilterOrIsNullIncorrectlyFixed(@Param("filter") BookFilter filter);

    boolean existsByName(String name);

    default List<Book> findByFilterExample(BookFilter filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }
        final Book book = new Book(
                filter.getName(),
                filter.getAuthor(),
                filter.getRating(),
                filter.getCountry()
        );
        final var example = Example.of(book);
        return findAll(example);
    }

    default List<Book> searchBySpecFilter(BookFilter filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }
        return findAll(filterSpec(filter));
    }

    List<Book> findAll(Specification<Book> spec);

    static Specification<Book> filterSpec(BookFilter filter) {
        return new Specification<Book>() {
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                return builder.and(Stream.of(
                        with(filter::getAuthor, a -> builder.equal(root.get(Book_.author), a)),
                        with(filter::getName, a -> builder.equal(root.get(Book_.name), a)),
                        with(filter::getCountry, a -> builder.equal(root.get(Book_.country), a)),
                        with(filter::getRating, a -> builder.equal(root.get(Book_.rating), a))
                ).filter(Objects::nonNull).toArray(Predicate[]::new));
            }
        };
    }

    static <T> Predicate with(Supplier<T> method, Function<T, Predicate> lambda) {
        final var value = method.get();
        if (value != null) {
            return lambda.apply(value);
        }
        return null;
    }
}
