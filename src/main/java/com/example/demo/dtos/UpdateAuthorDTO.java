package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateAuthorDTO {
    private String name;
    private String bio;
    private List<BookWithAuthorIdDTO> books;
}
