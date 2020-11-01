package com.evilcorp.orisnull.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class BookReadId implements Serializable {

    @Column(name = "book_id")
    private UUID bookId;
    @Column(name = "book_reader_id")
    private UUID bookReaderId;

    public BookReadId(UUID bookId, UUID bookReaderId) {
        this.bookId = bookId;
        this.bookReaderId = bookReaderId;
    }

    public BookReadId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookReadId that = (BookReadId) o;
        return bookId.equals(that.bookId) &&
                bookReaderId.equals(that.bookReaderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, bookReaderId);
    }
}
