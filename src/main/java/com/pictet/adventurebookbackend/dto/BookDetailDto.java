package com.pictet.adventurebookbackend.dto;

import com.pictet.adventurebookbackend.model.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BookDetailDto {

    private Long id;
    private String title;
    private String author;
    private Difficulty difficulty;
    private Set<String> categories;
    private Integer sectionCount;

}
