package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class AuthorDTO {
    private Long id;
    private String name;
    private String bio;
    private List<BookWithoutAuthorIdDTO> books;
}
