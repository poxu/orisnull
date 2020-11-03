package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.annotation.OrIsNullQuery;
import com.evilcorp.orisnull.annotation.OrIsNullRepository;
import com.evilcorp.orisnull.entity.Book;
import com.evilcorp.orisnull.filter.BookFilter;

import java.util.List;

@OrIsNullRepository
public interface BookSearchService {
    //language=HQL
    @OrIsNullQuery(value = "" +
            " select                           " + "\n" +
            "   b                              " + "\n" +
            " from                             " + "\n" +
            "   Book b                         " + "\n" +
            " where                            " + "\n" +
            "       1=1                        " + "\n" +
            "   and b.author = :author   -- op " + "\n" +
            "   and b.country = :country -- op " + "\n" +
            "   and b.rating = :rating   -- op " + "\n" +
            "   and b.name = :name       -- op "
    )
    List<Book> findBooks(BookFilter filter);

    //language=HQL
    @OrIsNullQuery(value = "" +
            " select                           " + "\n" +
            "   b                              " + "\n" +
            " from                             " + "\n" +
            "   Book b                         " + "\n" +
            " where                            " + "\n" +
            "       1=1                        " + "\n" +
            "   and b.author = :author   -- op " + "\n" +
            "   and b.country = :country -- op " + "\n" +
            "   and b.rating = :rating   -- op " + "\n" +
            "   and b.name = :name       -- op "
    )
    List<Book> findAllBooks(BookFilter filter);
}
