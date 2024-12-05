package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookWithoutAuthorIdDTO {
    private Long id;
    private String title;
    private String genre;
}
