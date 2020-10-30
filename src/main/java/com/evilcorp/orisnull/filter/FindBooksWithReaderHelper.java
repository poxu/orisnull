package com.evilcorp.orisnull.filter;

public class FindBooksWithReaderHelper {
    private final BookFilterWithReader filter;

    public FindBooksWithReaderHelper(BookFilterWithReader filter) {
        this.filter = filter;
    }

    public boolean name() {
        return filter.getName() == null;
    }

    public boolean country() {
        return filter.getCountry() == null;
    }

    public boolean author() {
        return filter.getAuthor() == null;
    }

    public boolean rating() {
        return filter.getRating() == null;
    }

    public boolean minRating() {
        return filter.getMinRating() == null;
    }

    public boolean maxRating() {
        return filter.getMaxRating() == null;
    }

    public boolean readers() {
        return filter.isEmpty();
    }
}
