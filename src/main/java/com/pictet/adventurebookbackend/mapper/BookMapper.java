package com.pictet.adventurebookbackend.mapper;

import com.pictet.adventurebookbackend.dto.*;
import com.pictet.adventurebookbackend.model.*;
import com.pictet.adventurebookbackend.model.enums.ConsequenceType;
import com.pictet.adventurebookbackend.model.enums.Difficulty;
import com.pictet.adventurebookbackend.model.enums.SectionType;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookJson json) {
        Book book = new Book();
        book.setTitle(json.getTitle());
        book.setAuthor(json.getAuthor());
        book.setDifficulty(Difficulty.valueOf(json.getDifficulty().toUpperCase()));

        for (SectionJson sJson : json.getSections()) {
            Section section = toSection(sJson);
            section.setBook(book);
            book.getSections().add(section);
        }

        return book;
    }

    private Section toSection(SectionJson json) {
        Section section = new Section();
        section.setSectionNumber(json.getParsedId());
        section.setText(json.getText());
        section.setType(SectionType.valueOf(json.getType().toUpperCase()));

        if (json.getOptions() != null) {
            for (OptionJson oJson : json.getOptions()) {
                Option option = toOption(oJson);
                option.setSection(section);
                section.getOptions().add(option);
            }
        }

        return section;
    }

    private Option toOption(OptionJson json) {
        Option option = new Option();
        option.setDescription(json.getDescription());
        option.setGotoId(json.getParsedGotoId());

        if (json.getConsequence() != null) {
            option.setConsequence(toConsequence(json.getConsequence()));
        }

        return option;
    }

    private Consequence toConsequence(ConsequenceJson json) {
        Consequence consequence = new Consequence();
        consequence.setType(ConsequenceType.valueOf(json.getType().toUpperCase()));
        consequence.setValue(Integer.parseInt(json.getValue()));
        consequence.setText(json.getText());
        return consequence;
    }

    public static BookDetailDto toDetail(Book book) {
        BookDetailDto dto = new BookDetailDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDifficulty(book.getDifficulty());
        dto.setCategories(book.getCategories());
        dto.setSectionCount(book.getSections().size());
        return dto;
    }

    public static BookSummaryDto toSummary(Book book) {
        BookSummaryDto dto = new BookSummaryDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDifficulty(book.getDifficulty());
        dto.setCategories(book.getCategories());
        return dto;
    }
}
