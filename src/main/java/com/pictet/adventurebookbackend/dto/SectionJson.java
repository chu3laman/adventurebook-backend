package com.pictet.adventurebookbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectionJson {


    private Object id;
    private String text;
    private String type;
    private List<OptionJson> options;

    public Integer getParsedId() {
        if (id instanceof Integer) return (Integer) id;
        if (id instanceof String) return Integer.parseInt((String) id);
        if (id instanceof Number) return ((Number) id).intValue();
        throw new IllegalArgumentException("Cannot parse section id: " + id);
    }

}
