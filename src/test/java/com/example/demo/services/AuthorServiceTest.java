package com.example.demo.services;

import com.example.demo.dtos.AuthorDTO;
import com.example.demo.dtos.AuthorWithoutBooksDTO;
import com.example.demo.dtos.BookWithAuthorIdDTO;
import com.example.demo.dtos.UpdateAuthorDTO;
import com.example.demo.interfaces.IAuthorRepository;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.helpers.Mapper.mapUpdateAuthorDTOToAuthorEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private IAuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAuthor() {
        AuthorWithoutBooksDTO authorDTO = new AuthorWithoutBooksDTO();
        authorDTO.setName("John Doe");
        authorDTO.setBio("Author bio");

        Author authorEntity = new Author();
        authorEntity.setName("John Doe");
        authorEntity.setBio("Author bio");
        authorEntity.setId(1L);

        when(authorRepository.addAuthor(any(Author.class))).thenReturn(authorEntity);

        var result = authorService.addAuthor(authorDTO);

        assertNotNull(result, "The result should not be null");
        assertEquals(1L, result.getId(), "The ID should match the mocked entity");
        assertEquals("John Doe", result.getName(), "The name should match");
        assertEquals("Author bio", result.getBio(), "The bio should match");
        verify(authorRepository, times(1)).addAuthor(any(Author.class));
    }


    @Test
    void testUpdateAuthor() {
        UpdateAuthorDTO updateAuthorDTO = new UpdateAuthorDTO();
        updateAuthorDTO.setName("Jane Doe");
        updateAuthorDTO.setBio("Updated bio");

        BookWithAuthorIdDTO bookDTO = new BookWithAuthorIdDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Book Title");
        bookDTO.setGenre("Genre");
        updateAuthorDTO.setBooks(Arrays.asList(bookDTO));

        Author authorEntity = mapUpdateAuthorDTOToAuthorEntity(updateAuthorDTO);
        authorEntity.setId(1L);

        Author updatedAuthorEntity = new Author();
        updatedAuthorEntity.setId(1L);
        updatedAuthorEntity.setName("Jane Doe");
        updatedAuthorEntity.setBio("Updated bio");
        updatedAuthorEntity.setBooks(new ArrayList<>());

        when(authorRepository.updateAuthor(any(Author.class), eq(1L))).thenReturn(updatedAuthorEntity);

        AuthorDTO result = authorService.updateAuthor(updateAuthorDTO, 1L);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("Updated bio", result.getBio());
        verify(authorRepository, times(1)).updateAuthor(any(Author.class), eq(1L));
    }


    @Test
    void testGetAllAuthors() {

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title1");
        book1.setGenre("Genre1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title2");
        book2.setGenre("Genre2");

        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Author 1");
        author1.setBio("Bio 1");
        author1.setBooks(Arrays.asList(book1));
        book1.setAuthorId(author1.getId());

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Author 2");
        author2.setBio("Bio 2");
        author2.setBooks(Arrays.asList(book2));
        book1.setAuthorId(author2.getId());

        when(authorRepository.getAllAuthors()).thenReturn(List.of(author1, author2));

        List<AuthorDTO> result = authorService.getAllAuthors();

        assertEquals(2, result.size());
        verify(authorRepository, times(1)).getAllAuthors();
    }

    @Test
    void testGetAuthorByIdWithoutBooks() {
        Author author = new Author();
        author.setId(1L);
        author.setName("John Doe");
        author.setBio("Author bio");

        when(authorRepository.getAuthorWithoutBooks(1L)).thenReturn(author);

        AuthorWithoutBooksDTO result = authorService.getAuthorByIdWithoutBooks(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(authorRepository, times(1)).getAuthorWithoutBooks(1L);
    }

    @Test
    void testGetAuthorByIdWithBooks() {

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title1");
        book1.setGenre("Genre1");

        Author author = new Author();
        author.setId(1L);
        author.setName("John Doe");
        author.setBio("Author bio");
        author.setBooks(Arrays.asList(book1));

        when(authorRepository.getAuthorWithBooks(1L)).thenReturn(author);

        AuthorDTO result = authorService.getAuthorByIdWithBooks(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(authorRepository, times(1)).getAuthorWithBooks(1L);
    }

    @Test
    void testDeleteAuthor() {
        doNothing().when(authorRepository).deleteAuthor(1L);

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).deleteAuthor(1L);
    }
}
