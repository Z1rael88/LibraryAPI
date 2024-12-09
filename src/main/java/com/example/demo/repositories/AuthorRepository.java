package com.example.demo.repositories;

import com.example.demo.interfaces.IAuthorRepository;
import com.example.demo.models.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AuthorRepository implements IAuthorRepository {

    private final EntityManager entityManager;
    public AuthorRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Author addAuthor(Author author) {
        entityManager.persist(author);
        return author;
    }
    @Transactional
    public Author updateAuthor(Author author, Long authorId) {
        var foundAuthor = getAuthorWithoutBooks(authorId);
        foundAuthor.setName(author.getName());
        foundAuthor.setBio(author.getBio());
        foundAuthor.setBooks(author.getBooks());
        entityManager.merge(foundAuthor);
        return foundAuthor;
    }

    public Author getAuthorWithoutBooks(Long authorId) {
        return entityManager.find(Author.class,authorId);
    }
    public Author getAuthorWithBooks(Long authorId) {
        TypedQuery<Author> query = entityManager.createQuery(
                "SELECT a FROM Author a JOIN FETCH a.books WHERE a.id = :authorId",
                Author.class
        );
        query.setParameter("authorId", authorId);
        return query.getSingleResult();
    }
    public List<Author> getAllAuthors() {
        TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a LEFT JOIN FETCH a.books", Author.class);
        return query.getResultList();
    }

    public void deleteAuthor(Long authorId) {
       var author = entityManager.find(Author.class,authorId);
       if(author != null)
       {
           entityManager.remove(author);
       }
       else{
           throw new IllegalArgumentException("Author not found");
       }
    }
}
