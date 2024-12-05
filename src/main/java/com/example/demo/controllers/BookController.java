package com.example.demo.controllers;

import com.example.demo.dtos.BookDTO;
import com.example.demo.dtos.BookWithAuthorIdDTO;
import com.example.demo.dtos.BookWithoutAuthorIdDTO;
import com.example.demo.dtos.UpdateBookDTO;
import com.example.demo.interfaces.IBookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final IBookService bookService;

    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookWithAuthorIdDTO createBook(@RequestBody BookWithAuthorIdDTO bookDTO) {
        return bookService.addBook(bookDTO);
    }
    @PutMapping("/{id}")
    public BookDTO updateBook (@RequestBody UpdateBookDTO bookDTO, @PathVariable Long id){
        return bookService.updateBook(bookDTO,id);
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookWithoutAuthorIdDTO getBookByIdWithoutAuthor(@PathVariable Long id) {
        return bookService.getBookByIdWithoutAuthor(id);
    }
    @GetMapping("/withAuthor/{id}")
    public BookDTO getBookByIdWithAuthor(@PathVariable Long id) {
        return bookService.getBookByIdWithAuthor(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}

