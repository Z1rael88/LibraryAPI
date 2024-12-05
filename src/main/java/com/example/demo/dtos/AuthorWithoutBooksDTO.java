package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorWithoutBooksDTO {
    private Long id;
    private String name;
    private String bio;
}
