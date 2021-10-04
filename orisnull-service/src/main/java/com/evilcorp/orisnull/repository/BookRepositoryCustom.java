package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.Book;
import com.evilcorp.orisnull.filter.BookFilter;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findByFilterJpql(BookFilter filter);

    List<Book> findByFilterJpqlWithHelper(BookFilter filter);

    List<Book> findByFilterNative(BookFilter filter);

    List<Book> findByFilterNativeOrIsNull(BookFilter filter);

    List<Book> findByFilterJpqlTypical(BookFilter filter);

    List<Book> findByFilterJpqlNaive(BookFilter filter);

    List<Book> findByFilterJpqlWithConvenientCommenting(BookFilter filter);
}
