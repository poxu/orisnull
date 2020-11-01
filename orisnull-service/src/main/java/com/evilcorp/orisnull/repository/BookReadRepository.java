package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.BookRead;
import com.evilcorp.orisnull.entity.BookReadId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReadRepository extends JpaRepository<BookRead, BookReadId>, BookReadRepositoryCustom {
}
