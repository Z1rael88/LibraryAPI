package com.example.demo.repositories;

import com.example.demo.interfaces.IBookRepository;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private IBookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        entityManager.createQuery("DELETE FROM Book").executeUpdate();
        entityManager.flush();
    }

    @Test
    public void testAddBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setGenre("Fiction");

        Book savedBook = bookRepository.addBook(book);

        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
        assertEquals("Test Book", savedBook.getTitle());
    }

    @Test
    public void testGetBookWithoutAuthor() {
        Book book = new Book();
        book.setTitle("Book without Author");
        book.setGenre("Non-Fiction");
        entityManager.persist(book);

        Book foundBook = bookRepository.getBookWithoutAuthor(book.getId());

        assertNotNull(foundBook);
        assertEquals("Book without Author", foundBook.getTitle());
    }
    @Test
    @Transactional
    public void testGetBookWithAuthor() {
        Author author = new Author();
        author.setName("Author 1");
        author.setBio("Bio 1");
        author.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setTitle("Book without Author");
        book.setGenre("Non-Fiction");
        book.setAuthor(author);
        author.getBooks().add(book);

        entityManager.persist(author);
        entityManager.persist(book);

        entityManager.flush();
        entityManager.clear();

        Book foundBook = bookRepository.getBookWithAuthor(book.getId());

        assertNotNull(foundBook);
        assertEquals("Book without Author", foundBook.getTitle());
        assertNotNull(foundBook.getAuthor());
        assertEquals("Author 1", foundBook.getAuthor().getName());
    }


    @Test
    public void testUpdateBook() {
        Author author1 = new Author();
        author1.setName("Author1");
        author1.setBio("Bio1");
        entityManager.persist(author1);

        Author author2 = new Author();
        author2.setName("Author2");
        author2.setBio("Bio2");
        entityManager.persist(author2);

        Book book = new Book();
        book.setTitle("Old Title");
        book.setGenre("Mystery");
        book.setAuthor(author1);
        entityManager.persist(book);
        entityManager.flush();

        Book existingBook = entityManager.find(Book.class, book.getId());

        existingBook.setTitle("New Title");
        existingBook.setGenre("Thriller");
        existingBook.setAuthor(author2);

        Book result = entityManager.merge(existingBook);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("Thriller", result.getGenre());
        assertEquals(author2, result.getAuthor());
    }


    @Test
    public void testGetAllBooks() {

        Author author1 = new Author();
        author1.setName("Author1");
        author1.setBio("Bio1");
        entityManager.persist(author1);

        Author author2 = new Author();
        author1.setName("Author2");
        author1.setBio("Bio2");
        entityManager.persist(author2);


        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setGenre("Fiction");
        book1.setAuthor(author1);
        entityManager.persist(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setGenre("Horror");
        book2.setAuthor(author2);
        entityManager.persist(book2);


        List<Book> books = bookRepository.getAllBooks();

        assertNotNull(books);
        assertEquals(2, books.size());
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book();
        book.setTitle("Book to Delete");
        book.setGenre("Fantasy");
        entityManager.persist(book);

        bookRepository.deleteBook(book.getId());
        entityManager.flush();

        Book foundBook = entityManager.find(Book.class, book.getId());
        assertNull(foundBook);
    }

    @Test
    public void testDeleteBookNotFound() {
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            bookRepository.deleteBook(100L);
        });
        assertEquals("Book not found", exception.getMessage());
    }
}
