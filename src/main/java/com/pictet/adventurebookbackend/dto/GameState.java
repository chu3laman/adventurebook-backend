package com.pictet.adventurebookbackend.dto;

import com.pictet.adventurebookbackend.model.enums.SectionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameState {

    private Long playerId;
    private String bookTitle;
    private Integer health;
    private Boolean alive;
    private Boolean finished;
    private Integer currentSectionId;
    private String sectionText;
    private SectionType sectionType;
    private List<OptionInfo> options;
    private String consequenceMessage;
    private String deathMessage;

    @Getter
    public static class OptionInfo {
        private final String description;
        private final Integer gotoId;

        public OptionInfo(String description, Integer gotoId) {
            this.description = description;
            this.gotoId = gotoId;
        }
    }
}
