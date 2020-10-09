package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.entity.Book;
import com.evilcorp.orisnull.filter.BookFilter;
import com.evilcorp.orisnull.filter.FindBookHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.List;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    @Autowired
    EntityManagerFactory emf;

    @Override
    public List<Book> findByFilterJpql(BookFilter filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }
        String sql =
                " select b from Book b where 1=1  " + (filter.getAuthor() == null ? "" :
                        " and b.author = :author  ") + (filter.getCountry() == null ? "" :
                        " and b.country = :country") + (filter.getRating() == null ? "" :
                        " and b.rating = :rating  ") + (filter.getName() == null ? "" :
                        " and b.name = :name      ");

        final var em = emf.createEntityManager();
        final var query = em.createQuery(sql, Book.class);

        if (filter.getAuthor() != null) {
            query.setParameter("author", filter.getAuthor());
        }
        if (filter.getCountry() != null) {
            query.setParameter("country", filter.getCountry());
        }
        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getRating() != null) {
            query.setParameter("rating", filter.getRating());
        }

        return query.getResultList();
    }

    @Override
    public List<Book> findByFilterJpqlWithHelper(BookFilter filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }
        FindBookHelper p = new FindBookHelper(filter);
        String sql =
                " select b from Book b where 1=1  "  + (p.author() ? "" :
                        " and b.author = :author  ") + (p.country() ? "" :
                        " and b.country = :country") + (p.rating() ? "" :
                        " and b.rating = :rating  ") + (p.name() ? "" :
                        " and b.name = :name      ");

        final var em = emf.createEntityManager();
        final var query = em.createQuery(sql, Book.class);

        if (filter.getAuthor() != null) {
            query.setParameter("author", filter.getAuthor());
        }
        if (filter.getCountry() != null) {
            query.setParameter("country", filter.getCountry());
        }
        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getRating() != null) {
            query.setParameter("rating", filter.getRating());
        }

        return query.getResultList();
    }

    @Override
    public List<Book> findByFilterJpqlTypical(BookFilter filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }
        String sql =
                " select b from Book b where 1=1  "
                        + (filter.getAuthor() == null ? "" :
                        " and b.author = :author  "
                )
                        + (filter.getCountry() == null ? "" :
                        " and b.country = :country"
                )
                        + (filter.getRating() == null ? "" :
                        " and b.rating = :rating  "
                )
                        + (filter.getName() == null ? "" :
                        " and b.name = :name      "
                );

        final var em = emf.createEntityManager();
        final var query = em.createQuery(sql, Book.class);

        if (filter.getAuthor() != null) {
            query.setParameter("author", filter.getAuthor());
        }
        if (filter.getCountry() != null) {
            query.setParameter("country", filter.getCountry());
        }
        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getRating() != null) {
            query.setParameter("rating", filter.getRating());
        }

        return query.getResultList();
    }

    @Override
    public List<Book> findByFilterJpqlNaive(BookFilter filter) {
        if (filter.isEmpty()) {
            return Collections.emptyList();
        }

        final var em = emf.createEntityManager();
        StringBuilder queryBuilder = new StringBuilder("select b from Book b where 1=1 ");
        if (filter.getAuthor() != null) {
            queryBuilder.append(" and b.author = :author ");
        }
        if (filter.getCountry() != null) {
            queryBuilder.append(" and b.country = :country ");
        }
        if (filter.getName() != null) {
            queryBuilder.append(" and b.rating = :rating ");
        }
        if (filter.getRating() != null) {
            queryBuilder.append(" and b.name = :name ");
        }

        final var query = em.createQuery(queryBuilder.toString(), Book.class);

        if (filter.getAuthor() != null) {
            query.setParameter("author", filter.getAuthor());
        }
        if (filter.getCountry() != null) {
            query.setParameter("country", filter.getCountry());
        }
        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getRating() != null) {
            query.setParameter("rating", filter.getRating());
        }

        return query.getResultList();
    }

    @Override
    public List<Book> findByFilterJpqlWithConvenientCommenting(BookFilter filter) {
        String sql =
                " select b from Book b where 1=1  "
                        + (filter.getAuthor() == null ? "" :
                        " "
                                + " and b.author = :author  "
                )
                        + (filter.getCountry() == null ? "" :
                        " "
                                + " and b.country = :country"
                )
                        + (filter.getRating() == null ? "" :
                        " "
                                + " and b.rating = :rating  "
                )
                        + (filter.getName() == null ? "" :
                        " "
                                + " and b.name = :name      ");

        final var em = emf.createEntityManager();
        final var query = em.createQuery(sql, Book.class);

        if (filter.getAuthor() != null) {
            query.setParameter("author", filter.getAuthor());
        }
        if (filter.getCountry() != null) {
            query.setParameter("country", filter.getCountry());
        }
        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getRating() != null) {
            query.setParameter("rating", filter.getRating());
        }

        return query.getResultList();
    }
}
