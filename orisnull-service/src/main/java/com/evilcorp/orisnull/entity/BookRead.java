package com.evilcorp.orisnull.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "books_readers")
public class BookRead {
    @EmbeddedId
    private BookReadId id;

    public BookRead(BookReadId id) {
        this.id = id;
    }

    public BookRead(UUID bookId, UUID readerId) {
        this.id = new BookReadId(bookId, readerId);
    }

    public BookRead() {
    }

    public BookReadId getId() {
        return id;
    }

    public void setId(BookReadId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookRead bookRead = (BookRead) o;
        return id.equals(bookRead.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
