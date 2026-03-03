package com.pictet.adventurebookbackend.service;

import com.pictet.adventurebookbackend.model.Book;
import com.pictet.adventurebookbackend.model.Option;
import com.pictet.adventurebookbackend.model.Section;
import com.pictet.adventurebookbackend.model.enums.SectionType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookValidator {

    public List<String> validate(Book book) {
        List<String> errors = new ArrayList<>();

        if (book.getSections() == null || book.getSections().isEmpty()) {
            errors.add("Book has no sections.");
            return errors;
        }

        validateBeginnings(book, errors);
        validateEndings(book, errors);
        validateNonEndingSectionsHaveOptions(book, errors);
        validateGotoIds(book, errors);

        return errors;
    }

    private void validateBeginnings(Book book, List<String> errors) {
        long beginCount = book.getSections().stream()
                .filter(s -> s.getType() == SectionType.BEGIN)
                .count();

        if (beginCount == 0) {
            errors.add("Book has no beginning section.");
        } else if (beginCount > 1) {
            errors.add("Book has more than one beginning section (" + beginCount + " found).");
        }
    }

    private void validateEndings(Book book, List<String> errors) {
        long endCount = book.getSections().stream()
                .filter(s -> s.getType() == SectionType.END)
                .count();

        if (endCount == 0) {
            errors.add("Book has no ending section.");
        }
    }

    private void validateNonEndingSectionsHaveOptions(Book book, List<String> errors) {
        for (Section section : book.getSections()) {
            if (section.getType() != SectionType.END) {
                if (section.getOptions() == null || section.getOptions().isEmpty()) {
                    errors.add("Non-ending section " + section.getSectionNumber() + " has no options.");
                }
            }
        }
    }

    private void validateGotoIds(Book book, List<String> errors) {
        Set<Integer> validSectionNumbers = book.getSections().stream()
                .map(Section::getSectionNumber)
                .collect(Collectors.toSet());

        for (Section section : book.getSections()) {
            if (section.getOptions() == null) continue;

            for (Option option : section.getOptions()) {
                if (!validSectionNumbers.contains(option.getGotoId())) {
                    errors.add("Section " + section.getSectionNumber()
                            + " has option pointing to invalid section id " + option.getGotoId() + ".");
                }
            }
        }
    }
}
