package com.example.demo.interfaces;

import com.example.demo.models.Book;

import java.util.List;

public interface IBookRepository {
    Book addBook(Book book);
    Book getBookWithoutAuthor(Long bookId);
    Book updateBook(Book book,Long bookId);
    Book getBookWithAuthor(Long bookId);
    List<Book> getAllBooks();
    void deleteBook(Long bookId);
}
