package com.evilcorp.orisnull.filter;

import java.util.Collection;

public class BookFilterWithReader {
    private final String name;
    private final String country;
    private final String author;
    private final Integer rating;
    private final Integer maxRating;
    private final Integer minRating;
    private final Collection<String> readers;

    public BookFilterWithReader(String name, String country, String author, Integer rating, Integer maxRating, Integer minRating, Collection<String> readers) {
        this.name = name;
        this.country = country;
        this.author = author;
        this.rating = rating;
        this.maxRating = maxRating;
        this.minRating = minRating;
        this.readers = readers;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getRating() {
        return rating;
    }

    public Collection<String> getReaders() {
        return readers;
    }

    public boolean isEmpty() {
        return rating == null
                && author == null
                && country == null
                && name == null
                && readers.isEmpty()
                ;
    }

    public Integer getMaxRating() {
        return maxRating;
    }

    public Integer getMinRating() {
        return minRating;
    }
}
