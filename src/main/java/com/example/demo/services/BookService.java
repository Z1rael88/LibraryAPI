package com.example.demo.services;

import com.example.demo.dtos.BookDTO;
import com.example.demo.dtos.BookWithAuthorIdDTO;
import com.example.demo.dtos.BookWithoutAuthorIdDTO;
import com.example.demo.dtos.UpdateBookDTO;
import com.example.demo.helpers.Mapper;
import com.example.demo.interfaces.IBookRepository;
import com.example.demo.interfaces.IBookService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.helpers.Mapper.*;

@Service
public class BookService implements IBookService {

    private final IBookRepository bookRepository;

    public BookService(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookWithAuthorIdDTO addBook(BookWithAuthorIdDTO bookDTO) {
        var book = mapBookWithoutAuthorToBookEntity(bookDTO);
        var createdBook = bookRepository.addBook(book);
        return mapToBookWithoutAuthorDTO(createdBook);
    }

    public BookDTO updateBook(UpdateBookDTO bookDTO, Long bookId) {
        var bookToUpdate = mapUpdateBookDTOToBookEntity(bookDTO);
        var updatedBook = bookRepository.updateBook(bookToUpdate,bookId);
        return mapToBookDTO(updatedBook);
    }

    public List<BookDTO> getAllBooks() {
        var books = bookRepository.getAllBooks();
        return books.stream()
                .map(Mapper::mapToBookDTO)
                .toList();
    }

    public BookWithoutAuthorIdDTO getBookByIdWithoutAuthor(Long id) {
        var book = bookRepository.getBookWithoutAuthor(id);
        return mapToBookWithoutAuthorIdDTO(book);
    }

    public BookDTO getBookByIdWithAuthor(Long id) {
        var bookWithAuthor = bookRepository.getBookWithAuthor(id);
        return mapToBookDTO(bookWithAuthor);
    }
@Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteBook(id);
    }


}
