package com.evilcorp.orisnull.filter;

public class FindBookHelper {
    private final BookFilter bookFilter;

    public FindBookHelper(BookFilter bookFilter) {
        this.bookFilter = bookFilter;
    }

    public boolean name() {
        return bookFilter.getName() == null;
    }

    public boolean country() {
        return bookFilter.getCountry() == null;
    }

    public boolean author() {
        return bookFilter.getAuthor() == null;
    }

    public boolean rating() {
        return bookFilter.getRating() == null;
    }
}
