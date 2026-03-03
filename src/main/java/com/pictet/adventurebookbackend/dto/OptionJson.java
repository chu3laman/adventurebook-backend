package com.pictet.adventurebookbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptionJson {

    private String description;
    private Object gotoId;
    private ConsequenceJson consequence;

    public Integer getParsedGotoId() {
        if (gotoId instanceof Integer) return (Integer) gotoId;
        if (gotoId instanceof String) return Integer.parseInt((String) gotoId);
        if (gotoId instanceof Number) return ((Number) gotoId).intValue();
        throw new IllegalArgumentException("Cannot parse gotoId: " + gotoId);
    }

}
