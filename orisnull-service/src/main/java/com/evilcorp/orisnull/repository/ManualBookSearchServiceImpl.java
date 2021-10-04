package com.evilcorp.orisnull.repository;

import com.evilcorp.orisnull.annotation.OrIsNullQuery;
import com.evilcorp.orisnull.entity.Book;
import com.evilcorp.orisnull.filter.BookFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManualBookSearchServiceImpl implements ManualBookSearchService {
    @Autowired
    private EntityManagerFactory emf;

    @Override
    public List<Book> findBooks(BookFilter filter) {
        if (filter.isEmpty())  {
            return Collections.emptyList();
        }
        String sql = null;
        try {
            sql = ManualBookSearchService.class.getMethod("findBooks", BookFilter.class)
                    .getAnnotation(OrIsNullQuery.class).value();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("name", filter.getName());
        params.put("country", filter.getCountry());
        params.put("author", filter.getAuthor());
        params.put("rating", filter.getRating());

        final var orisnull = new OrIsNullReplace(params);
        final var em= emf.createEntityManager();
        final String result = orisnull.transform(sql);
        final var jpqlQuery = em.createQuery(result, Book.class);
        params.forEach(jpqlQuery::setParameter);
        return jpqlQuery.getResultList();
    }
}
