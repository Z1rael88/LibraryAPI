package com.example.demo.controllers;

import com.example.demo.dtos.*;
import com.example.demo.interfaces.IAuthorService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorWithoutBooksDTO authorDTO = new AuthorWithoutBooksDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Author Name");
        authorDTO.setBio("Author Bio");
        when(authorService.addAuthor(any(AuthorWithoutBooksDTO.class))).thenReturn(authorDTO);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Author Name"))
                .andExpect(jsonPath("$.bio").value("Author Bio"));
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        UpdateAuthorDTO updateAuthorDTO = new UpdateAuthorDTO();
        updateAuthorDTO.setName("J.R. Tokien");
        updateAuthorDTO.setBio("10/10, incredible");

        BookWithAuthorIdDTO bookDTO = new BookWithAuthorIdDTO();
        bookDTO.setId(129L);
        bookDTO.setTitle("Tops");
        bookDTO.setGenre("Criminal");
        bookDTO.setAuthorId(85L);

        BookWithoutAuthorIdDTO bookWithoutAuthorIdDTO = new BookWithoutAuthorIdDTO();
        bookWithoutAuthorIdDTO.setId(bookDTO.getId());
        bookWithoutAuthorIdDTO.setGenre(bookDTO.getGenre());
        bookWithoutAuthorIdDTO.setTitle(bookDTO.getTitle());

        updateAuthorDTO.setBooks(List.of(bookDTO));

        AuthorWithoutBooksDTO authorWithoutBooksDTO = new AuthorWithoutBooksDTO();
        authorWithoutBooksDTO.setName(updateAuthorDTO.getName());
        authorWithoutBooksDTO.setBio(updateAuthorDTO.getBio());
        authorService.addAuthor(authorWithoutBooksDTO);

        AuthorDTO updatedAuthor = new AuthorDTO();
        updatedAuthor.setId(1L);
        updatedAuthor.setName("Updated Name");
        updatedAuthor.setBio("Updated Bio");
        updatedAuthor.setBooks(List.of(bookWithoutAuthorIdDTO));

        when(authorService.updateAuthor(any(UpdateAuthorDTO.class), eq(1L)))
                .thenReturn(updatedAuthor);

        mockMvc.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAuthorDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.bio").value("Updated Bio"));
    }

    @Test
    public void testGetAllAuthors() throws Exception {
        AuthorDTO author1 = new AuthorDTO();
        author1.setId(1L);
        author1.setName("Author1");
        author1.setBio("Bio1");
        author1.setBooks(List.of());

        AuthorDTO author2 = new AuthorDTO();
        author2.setId(2L);
        author2.setName("Author2");
        author2.setBio("Bio2");
        author2.setBooks(List.of());

        List<AuthorDTO> authors = List.of(author1, author2);
        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Author1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Author2"));
    }

    @Test
    public void testGetAuthorByIdWithBooks() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Author Name");
        authorDTO.setBio("Author Bio");
        authorDTO.setBooks(List.of());
        when(authorService.getAuthorByIdWithBooks(1L)).thenReturn(authorDTO);

        mockMvc.perform(get("/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Author Name"))
                .andExpect(jsonPath("$.bio").value("Author Bio"));
    }

    @Test
    public void testGetAuthorByIdWithoutBooks() throws Exception {
        AuthorWithoutBooksDTO authorDTO = new AuthorWithoutBooksDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Author Name");
        authorDTO.setBio("Author Bio");
        when(authorService.getAuthorByIdWithoutBooks(1L)).thenReturn(authorDTO);

        mockMvc.perform(get("/authors/WithoutBooks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Author Name"))
                .andExpect(jsonPath("$.bio").value("Author Bio"));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/authors/1"))
                .andExpect(status().isOk());

        Mockito.verify(authorService).deleteAuthor(1L);
    }
}
