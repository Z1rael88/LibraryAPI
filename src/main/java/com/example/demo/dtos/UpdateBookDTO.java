package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookDTO {
    private String title;
    private String genre;
    private Long authorId;
}
