package com.pictet.adventurebookbackend.repository;

import com.pictet.adventurebookbackend.model.Section;
import com.pictet.adventurebookbackend.model.enums.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByBookIdAndSectionNumber(Long bookId, Integer sectionNumber);

    List<Section> findByBookIdAndType(Long bookId, SectionType type);

}
