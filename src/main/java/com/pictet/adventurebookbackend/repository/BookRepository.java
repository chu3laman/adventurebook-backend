package com.pictet.adventurebookbackend.repository;

import com.pictet.adventurebookbackend.model.Book;
import com.pictet.adventurebookbackend.model.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByDifficulty(Difficulty difficulty);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.categories c WHERE UPPER(c) = UPPER(:category)")
    List<Book> findByCategory(@Param("category") String category);

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN b.categories c " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) " +
            "AND (:difficulty IS NULL OR b.difficulty = :difficulty) " +
            "AND (:category IS NULL OR UPPER(c) = UPPER(:category))")
    List<Book> searchBooks(@Param("title") String title,
            @Param("author") String author,
            @Param("difficulty") Difficulty difficulty,
            @Param("category") String category);

}
