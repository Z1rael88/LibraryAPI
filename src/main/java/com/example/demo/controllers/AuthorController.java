package com.example.demo.controllers;

import com.example.demo.dtos.AuthorDTO;
import com.example.demo.dtos.AuthorWithoutBooksDTO;
import com.example.demo.dtos.UpdateAuthorDTO;
import com.example.demo.interfaces.IAuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final IAuthorService authorService;

    public AuthorController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public AuthorWithoutBooksDTO createAuthor(@RequestBody AuthorWithoutBooksDTO authorDTO) {
        return authorService.addAuthor(authorDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@RequestBody UpdateAuthorDTO authorDTO, @PathVariable Long id){
        var updatedAuthor = authorService.updateAuthor(authorDTO,id);
        if (updatedAuthor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAuthor);
    }

    @GetMapping
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public AuthorDTO getAuthorByIdWithBooks(@PathVariable Long id) {
        return authorService.getAuthorByIdWithBooks(id);
    }
    @GetMapping("/WithoutBooks/{id}")
    public AuthorWithoutBooksDTO getAuthorByIdWithoutBooks(@PathVariable Long id) {
        return authorService.getAuthorByIdWithoutBooks(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }
}
