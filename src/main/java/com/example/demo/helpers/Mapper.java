package com.example.demo.helpers;

import com.example.demo.dtos.*;
import com.example.demo.models.Author;
import com.example.demo.models.Book;

import java.util.stream.Collectors;

public class Mapper {
    public static AuthorDTO mapToAuthorDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setBio(author.getBio());
        dto.setName(author.getName());
        dto.setBooks(author.getBooks().stream()
                .map(Mapper::mapToBookWithoutAuthorIdDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public static AuthorWithoutBooksDTO mapToAuthorWithoutBooksDTO(Author author) {
        AuthorWithoutBooksDTO dto = new AuthorWithoutBooksDTO();
        dto.setId(author.getId());
        dto.setBio(author.getBio());
        dto.setName(author.getName());
        return dto;
    }
    public static Author mapAuthorWithoutBooksToAuthorEntity(AuthorWithoutBooksDTO dto) {
        Author author = new Author();
        author.setId(dto.getId());
        author.setBio(dto.getBio());
        author.setName(dto.getName());
        return author;
    }

    public static Author mapUpdateAuthorDTOToAuthorEntity(UpdateAuthorDTO dto) {
        Author author = new Author();
        author.setBio(dto.getBio());
        author.setName(dto.getName());
        author.setBooks(dto.getBooks().stream()
                .map(Mapper::mapBookWithoutAuthorToBookEntity)
                .collect(Collectors.toList()));
        return author;
    }

    public static Book mapUpdateBookDTOToBookEntity(UpdateBookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setGenre(bookDTO.getGenre());
        book.setAuthorId(bookDTO.getAuthorId());
        return book;
    }
    public static Book mapBookWithoutAuthorToBookEntity(BookWithAuthorIdDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setGenre(bookDTO.getGenre());
        book.setAuthorId(bookDTO.getAuthorId());
        return book;
    }

    public static BookDTO mapToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(book.getTitle());
        bookDTO.setId(book.getId());
        bookDTO.setGenre(book.getGenre());
        bookDTO.setAuthor(mapToAuthorWithoutBooksDTO(book.getAuthor()));
        return bookDTO;
    }
    public static BookWithAuthorIdDTO mapToBookWithoutAuthorDTO(Book book) {
        BookWithAuthorIdDTO dto = new BookWithAuthorIdDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setGenre(book.getGenre());
        dto.setAuthorId(book.getAuthorId());
        return dto;
    }
    public static BookWithoutAuthorIdDTO mapToBookWithoutAuthorIdDTO(Book book){
        BookWithoutAuthorIdDTO dto = new BookWithoutAuthorIdDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setGenre(book.getGenre());
        return dto;
    }
}
