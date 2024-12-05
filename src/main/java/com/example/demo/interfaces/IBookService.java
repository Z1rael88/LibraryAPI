package com.example.demo.interfaces;

import com.example.demo.dtos.BookDTO;
import com.example.demo.dtos.BookWithAuthorIdDTO;
import com.example.demo.dtos.BookWithoutAuthorIdDTO;
import com.example.demo.dtos.UpdateBookDTO;

import java.util.List;

public interface IBookService {

    BookWithAuthorIdDTO addBook(BookWithAuthorIdDTO bookDTO);
    BookDTO updateBook(UpdateBookDTO bookDTO, Long bookId);

    List<BookDTO> getAllBooks();

    BookWithoutAuthorIdDTO getBookByIdWithoutAuthor(Long id);
    BookDTO getBookByIdWithAuthor(Long id);

    void deleteBook(Long id);
}
