package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.Book;
import com.evilcorp.orisnull.filter.BookFilterWithReader;

import java.util.List;

public interface BookReadRepositoryCustom {
    List<Book> findBooksByReaderName(BookFilterWithReader filter);

    List<Book> findBooksByReaderNameCriteriaApi(BookFilterWithReader filter);

    List<Book> findBooksByReaderNameNative(BookFilterWithReader filter);
}
