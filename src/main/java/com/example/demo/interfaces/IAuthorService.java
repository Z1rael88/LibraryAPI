package com.example.demo.interfaces;

import com.example.demo.dtos.AuthorDTO;
import com.example.demo.dtos.AuthorWithoutBooksDTO;
import com.example.demo.dtos.UpdateAuthorDTO;

import java.util.List;

public interface IAuthorService {

    AuthorWithoutBooksDTO addAuthor(AuthorWithoutBooksDTO authorDTO);
    AuthorDTO updateAuthor(UpdateAuthorDTO authorDTO,Long authorId);

    List<AuthorDTO> getAllAuthors();

    AuthorDTO getAuthorByIdWithBooks(Long id);
    AuthorWithoutBooksDTO getAuthorByIdWithoutBooks(Long id);

    void deleteAuthor(Long id);
}
