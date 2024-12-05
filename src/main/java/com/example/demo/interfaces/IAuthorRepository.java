package com.example.demo.interfaces;

import com.example.demo.models.Author;

import java.util.List;

public interface IAuthorRepository {
    Author addAuthor(Author author);
    Author updateAuthor(Author author,Long authorId);
    Author getAuthorWithoutBooks(Long authorId);
    Author getAuthorWithBooks(Long authorId);
    List<Author> getAllAuthors();
    void deleteAuthor(Long authorId);
}