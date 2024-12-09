package com.example.demo.repositories;

import com.example.demo.interfaces.IAuthorRepository;
import com.example.demo.interfaces.IBookRepository;
import com.example.demo.models.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository implements IBookRepository {

    private final EntityManager entityManager;
    private final IAuthorRepository authorRepository;

    @Autowired
    public BookRepository(EntityManager entityManager,IAuthorRepository authorRepository) {
        this.entityManager = entityManager;
        this.authorRepository = authorRepository;
    }
    @Transactional
    public Book addBook(Book book) {

        if (book.getAuthorId() != null) {
            var author = authorRepository.getAuthorWithoutBooks(book.getAuthorId());
            book.setAuthor(author);
        }
        entityManager.persist(book);
        return book;
    }

    public Book getBookWithoutAuthor(Long bookId) {
        return entityManager.find(Book.class, bookId);

    }
    @Transactional
    public Book updateBook(Book book,Long bookId) {
        //book.setTitle("ABC");
        var foundBook = getBookWithAuthor(bookId);
        var bookAuthor = authorRepository.getAuthorWithoutBooks(book.getAuthorId());
        foundBook.setTitle(book.getTitle());
        foundBook.setGenre(book.getGenre());
        foundBook.setAuthor(bookAuthor);
        entityManager.merge(foundBook);
        return foundBook;
    }

    public Book getBookWithAuthor(Long bookId) {
        try {
            TypedQuery<Book> query = entityManager.createQuery(
                    "SELECT b FROM Book b JOIN FETCH b.author WHERE b.id = :bookId",
                    Book.class
            );
            query.setParameter("bookId", bookId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalArgumentException("Book with id " + bookId + " not found.", e);
        }
    }


    public List<Book> getAllBooks() {
        TypedQuery<Book> query = entityManager.createQuery(
                "SELECT b FROM Book b JOIN FETCH b.author", Book.class
        );
        return query.getResultList();
    }


    public void deleteBook(Long bookId) {
        Book book = entityManager.find(Book.class, bookId);
        if (book != null) {
            entityManager.remove(book);
        }
       else{
        throw new IllegalArgumentException("Book not found");
       }
    }
}
