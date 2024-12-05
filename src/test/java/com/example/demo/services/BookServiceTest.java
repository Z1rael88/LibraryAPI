package com.example.demo.services;

import com.example.demo.dtos.BookDTO;
import com.example.demo.dtos.BookWithAuthorIdDTO;
import com.example.demo.dtos.UpdateBookDTO;
import com.example.demo.interfaces.IBookRepository;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.helpers.Mapper.mapUpdateBookDTOToBookEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private IBookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook() {

        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Name1");
        author1.setBio("Bio1");

        // Create the BookWithAuthorIdDTO (DTO)
        BookWithAuthorIdDTO bookWithAuthorIdDTO = new BookWithAuthorIdDTO();
        bookWithAuthorIdDTO.setId(1L);
        bookWithAuthorIdDTO.setTitle("Book Title");
        bookWithAuthorIdDTO.setGenre("Genre");
        bookWithAuthorIdDTO.setAuthorId(author1.getId());

        // Create the Book entity to be saved (we map from DTO to entity)
        Book bookEntity = new Book();
        bookEntity.setId(1L);
        bookEntity.setTitle("Book Title");
        bookEntity.setGenre("Genre");
        bookEntity.setAuthorId(author1.getId()); // Set the authorId for the Book entity

        // Mock the repository to return the same bookEntity when addBook is called
        when(bookRepository.addBook(any(Book.class))).thenReturn(bookEntity);

        // Call the service method
        BookWithAuthorIdDTO result = bookService.addBook(bookWithAuthorIdDTO);

        // Assertions to verify the behavior
        assertNotNull(result);
        assertEquals(1L, result.getId()); // Verify the ID
        assertEquals("Book Title", result.getTitle()); // Verify the title
        assertEquals("Genre", result.getGenre()); // Verify the genre
        assertEquals(1L, result.getAuthorId()); // Verify the authorId
        verify(bookRepository, times(1)).addBook(any(Book.class)); // Verify the repository method was called once
    }



    @Test
    void testUpdateBook() {

        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Name1");
        author1.setBio("Bio1");

        UpdateBookDTO updateBookDTO = new UpdateBookDTO();
        updateBookDTO.setTitle("Updated Title");
        updateBookDTO.setGenre("Updated Genre");

        Book bookEntity = mapUpdateBookDTOToBookEntity(updateBookDTO);
        bookEntity.setId(1L);

        Book updatedBookEntity = new Book();
        updatedBookEntity.setId(1L);
        updatedBookEntity.setTitle("Updated Title");
        updatedBookEntity.setGenre("Updated Genre");
        updatedBookEntity.setAuthorId(author1.getId());
        updatedBookEntity.setAuthor(author1);

        when(bookRepository.updateBook(any(Book.class), eq(1L))).thenReturn(updatedBookEntity);

        BookDTO result = bookService.updateBook(updateBookDTO, 1L);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Genre", result.getGenre());
        verify(bookRepository, times(1)).updateBook(any(Book.class), eq(1L));
    }

    @Test
    void testGetAllBooks() {

        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Name1");
        author1.setBio("Bio1");

        Author author2 = new Author();
        author1.setId(2L);
        author2.setName("Name2");
        author2.setBio("Bio2");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setGenre("Genre 1");
        book1.setAuthor(author1);
        book1.setAuthorId(author1.getId());

        author1.setBooks(Arrays.asList(book1));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setGenre("Genre 2");
        book2.setAuthor(author2);
        book2.setAuthorId(author2.getId());
        author2.setBooks(Arrays.asList(book2));

        when(bookRepository.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        List<BookDTO> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).getAllBooks();
    }

    @Test
    void testGetBookByIdWithoutAuthor() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");
        book.setGenre("Genre");

        when(bookRepository.getBookWithoutAuthor(1L)).thenReturn(book);

        var result = bookService.getBookByIdWithoutAuthor(1L);

        assertNotNull(result);
        assertEquals("Book Title", result.getTitle());
        verify(bookRepository, times(1)).getBookWithoutAuthor(1L);
    }

    @Test
    void testGetBookByIdWithAuthor() {

        Author author = new Author();
        author.setId(1L);
        author.setName("Name1");
        author.setBio("Bio1");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");
        book.setGenre("Genre");
        book.setAuthor(author);
        book.setAuthorId(author.getId());
        author.setBooks(Arrays.asList(book));

        when(bookRepository.getBookWithAuthor(1L)).thenReturn(book);

        BookDTO result = bookService.getBookByIdWithAuthor(1L);

        assertNotNull(result);
        assertEquals("Book Title", result.getTitle());
        verify(bookRepository, times(1)).getBookWithAuthor(1L);
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteBook(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteBook(1L);
    }
}
