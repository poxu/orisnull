package com.evilcorp.orisnull.filter;

import java.util.Collection;

public class BookFilterWithReaderBuilder {
    private String name;
    private String country;
    private String author;
    private Integer rating;
    private Integer maxRating;
    private Integer minRating;
    private Collection<String> readers;

    public BookFilterWithReaderBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BookFilterWithReaderBuilder maxRating(Integer maxRating) {
        this.maxRating = maxRating;
        return this;
    }

    public BookFilterWithReaderBuilder minRating(Integer minRating) {
        this.minRating = minRating;
        return this;
    }

    public BookFilterWithReaderBuilder country(String country) {
        this.country = country;
        return this;
    }

    public BookFilterWithReaderBuilder author(String author) {
        this.author = author;
        return this;
    }

    public BookFilterWithReaderBuilder rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public BookFilterWithReaderBuilder readers(Collection<String> readers) {
        this.readers = readers;
        return this;
    }

    public static BookFilterWithReaderBuilder builder() {
        return new BookFilterWithReaderBuilder();
    }

    public BookFilterWithReader build() {
        return new BookFilterWithReader(name, country, author, rating, maxRating, minRating, readers);
    }
}