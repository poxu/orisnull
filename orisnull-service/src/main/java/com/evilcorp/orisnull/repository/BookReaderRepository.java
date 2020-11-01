package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.BookReader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookReaderRepository extends JpaRepository<BookReader, UUID> {
}
