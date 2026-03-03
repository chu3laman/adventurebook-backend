package com.pictet.adventurebookbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookJson {

    private String title;
    private String author;
    private String difficulty;
    private List<SectionJson> sections;

}
