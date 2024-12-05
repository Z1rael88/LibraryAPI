package com.example.demo.repositories;

import com.example.demo.interfaces.IAuthorRepository;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthorRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private IAuthorRepository authorRepository;

    @BeforeEach
    public void setUp() {
        entityManager.createQuery("DELETE FROM Book").executeUpdate();
        entityManager.flush();
    }

    @Test
    public void testAddAuthor() {
        Author author = new Author();
        author.setName("Author 1");
        author.setBio("Bio1");

        Author savedAuthor = authorRepository.addAuthor(author);

        assertNotNull(savedAuthor);
        assertNotNull(savedAuthor.getId());
        assertEquals("Author 1", savedAuthor.getName());
    }

    @Test
    public void testGetAuthorWithoutBooks() {
        Author author = new Author();
        author.setName("Author 1");
        author.setBio("Super cool author");
        entityManager.persist(author);

        Author foundAuthor = authorRepository.getAuthorWithoutBooks(author.getId());

        assertNotNull(foundAuthor);
        assertEquals("Super cool author", foundAuthor.getBio());
    }

    @Test
    public void testGetAuthorWithBooks() {

        Author author1 = new Author();
        author1.setName("Author1");
        author1.setBio("Bio1");

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setGenre("Fiction");
        book1.setAuthor(author1);
        author1.setBooks(List.of(book1));
        entityManager.persist(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setGenre("Horror");
        book2.setAuthor(author1);
        author1.setBooks(List.of(book2));
        entityManager.persist(book2);
        entityManager.persist(author1);
        entityManager.flush();
        entityManager.clear();

        Author foundAuthor = authorRepository.getAuthorWithBooks(author1.getId());

        assertNotNull(foundAuthor);
        assertEquals("Bio1", foundAuthor.getBio());
        assertNotNull(foundAuthor.getBooks());
        assertEquals(2, foundAuthor.getBooks().size());
        assertTrue(foundAuthor.getBooks().stream().anyMatch(b -> b.getTitle().equals("Book 1")));
        assertTrue(foundAuthor.getBooks().stream().anyMatch(b -> b.getTitle().equals("Book 2")));
    }

    @Test
    public void testUpdateAuthor() {
        Author author1 = new Author();
        author1.setName("Author1");
        author1.setBio("Bio1");
        entityManager.persist(author1);
        entityManager.flush();

        Author existingAuthor = entityManager.find(Author.class, author1.getId());

        existingAuthor.setName("Author 3");
        existingAuthor.setBio("Not Cool Author Bio");

        Author result = entityManager.merge(existingAuthor);

        assertNotNull(result);
        assertEquals("Author 3", result.getName());
        assertEquals("Not Cool Author Bio", result.getBio());
    }


    @Test
    @Transactional
    public void testGetAllAuthors() {
        Author author1 = new Author();
        author1.setName("Author1");
        author1.setBio("Bio1");

        Author author2 = new Author();
        author2.setName("Author2");
        author2.setBio("Bio2");

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setGenre("Fiction");
        book1.setAuthor(author1);
        author1.setBooks(List.of(book1));
        entityManager.persist(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setGenre("Horror");
        book2.setAuthor(author2);
        author2.setBooks(List.of(book2));
        entityManager.persist(book2);

        entityManager.persist(author1);
        entityManager.persist(author2);

        entityManager.flush();
        entityManager.clear();

        List<Author> authors = authorRepository.getAllAuthors();

        assertNotNull(authors);
        assertEquals(2, authors.size());
        assertTrue(authors.stream().anyMatch(a -> a.getName().equals("Author1")));
        assertTrue(authors.stream().anyMatch(a -> a.getName().equals("Author2")));
    }


    @Test
    public void testDeleteAuthor() {
        Author author = new Author();
        author.setName("Author 1");
        author.setBio("Very Cool author");
        entityManager.persist(author);

        authorRepository.deleteAuthor(author.getId());
        entityManager.flush();

        Author foundAuthor = entityManager.find(Author.class, author.getId());
        assertNull(foundAuthor);
    }

    @Test
    public void testDeleteAuthorNotFound() {
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            authorRepository.deleteAuthor(100L);
        });
        assertEquals("Author not found", exception.getMessage());
    }
}
