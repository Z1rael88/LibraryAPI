package com.example.demo.services;

import com.example.demo.dtos.AuthorDTO;
import com.example.demo.dtos.AuthorWithoutBooksDTO;
import com.example.demo.dtos.UpdateAuthorDTO;
import com.example.demo.helpers.Mapper;
import com.example.demo.interfaces.IAuthorRepository;
import com.example.demo.interfaces.IAuthorService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.helpers.Mapper.*;

@Service
public class AuthorService implements IAuthorService {

    private final IAuthorRepository authorRepository;

    public AuthorService(IAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorWithoutBooksDTO addAuthor(AuthorWithoutBooksDTO authorDTO) {
        var author = mapAuthorWithoutBooksToAuthorEntity(authorDTO);
        var createdAuthor = authorRepository.addAuthor(author);
       return mapToAuthorWithoutBooksDTO(createdAuthor);
    }

    public AuthorDTO updateAuthor(UpdateAuthorDTO authorDTO, Long authorId) {
        var authorToUpdate = mapUpdateAuthorDTOToAuthorEntity(authorDTO);
        var updatedAuthor = authorRepository.updateAuthor(authorToUpdate,authorId);
        return mapToAuthorDTO(updatedAuthor);
    }

    public List<AuthorDTO> getAllAuthors() {
        var authors = authorRepository.getAllAuthors();
        return authors.stream()
                .map(Mapper::mapToAuthorDTO)
                .toList();
    }

    public AuthorWithoutBooksDTO getAuthorByIdWithoutBooks(Long id) {
        var authorWithoutBooks = authorRepository.getAuthorWithoutBooks(id);
        return mapToAuthorWithoutBooksDTO(authorWithoutBooks);
    }
    public AuthorDTO getAuthorByIdWithBooks(Long id) {
        var author = authorRepository.getAuthorWithBooks(id);
        return  mapToAuthorDTO(author);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        authorRepository.deleteAuthor(id);
    }


}
