package com.example.demo.controllers;

import com.example.demo.dtos.*;
import com.example.demo.interfaces.IBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateBook() throws Exception {
        BookWithAuthorIdDTO bookDTO = new BookWithAuthorIdDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Book Title");

        bookDTO.setAuthorId(1L);

        Mockito.when(bookService.addBook(any(BookWithAuthorIdDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.authorId").value(1));
    }

    @Test
    public void testUpdateBook() throws Exception {
        UpdateBookDTO updateBookDTO = new UpdateBookDTO();
        updateBookDTO.setTitle("Updated Book Title");

        BookDTO updatedBook = new BookDTO();
        updatedBook.setId(1L);
        updatedBook.setTitle("Updated Book Title");
        AuthorWithoutBooksDTO authorDTO = new AuthorWithoutBooksDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Author Name");
        updatedBook.setAuthor(authorDTO);

        Mockito.when(bookService.updateBook(any(UpdateBookDTO.class), eq(1L))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Book Title"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.name").value("Author Name"));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        BookDTO book1 = new BookDTO();
        book1.setId(1L);
        book1.setTitle("Book 1");
        AuthorWithoutBooksDTO author1 = new AuthorWithoutBooksDTO();
        author1.setId(1L);
        author1.setName("Author 1");
        book1.setAuthor(author1);

        BookDTO book2 = new BookDTO();
        book2.setId(2L);
        book2.setTitle("Book 2");
        AuthorWithoutBooksDTO author2 = new AuthorWithoutBooksDTO();
        author2.setId(2L);
        author2.setName("Author 2");
        book2.setAuthor(author2);

        Mockito.when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[0].author.id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Book 2"))
                .andExpect(jsonPath("$[1].author.id").value(2));
    }

    @Test
    public void testGetBookByIdWithAuthor() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Book Title");
        AuthorWithoutBooksDTO authorDTO = new AuthorWithoutBooksDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Author Name");
        bookDTO.setAuthor(authorDTO);

        Mockito.when(bookService.getBookByIdWithAuthor(1L)).thenReturn(bookDTO);

        mockMvc.perform(get("/books/withAuthor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.name").value("Author Name"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());

        Mockito.verify(bookService).deleteBook(1L);
    }
}
