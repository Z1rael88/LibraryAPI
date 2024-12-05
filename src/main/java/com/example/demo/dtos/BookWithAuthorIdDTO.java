package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookWithAuthorIdDTO {
    private Long id;
    private String title;
    private String genre;
    private Long authorId;
}
