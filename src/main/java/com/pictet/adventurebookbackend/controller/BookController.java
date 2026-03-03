package com.pictet.adventurebookbackend.controller;

import com.pictet.adventurebookbackend.dto.BookDetailDto;
import com.pictet.adventurebookbackend.dto.BookSummaryDto;
import com.pictet.adventurebookbackend.dto.CategoryRequest;
import com.pictet.adventurebookbackend.mapper.BookMapper;
import com.pictet.adventurebookbackend.model.Book;
import com.pictet.adventurebookbackend.model.enums.Difficulty;
import com.pictet.adventurebookbackend.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pictet.adventurebookbackend.mapper.BookMapper.toDetail;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/books")
public class BookController {

    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookSummaryDto>> getAllBooks() {
        List<BookSummaryDto> books = bookService.getAllBooks().stream()
                .map(BookMapper::toSummary)
                .toList();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookSummaryDto>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String category) {

        List<BookSummaryDto> books = bookService.searchBooks(title, author, difficulty, category)
                .stream()
                .map(BookMapper::toSummary)
                .toList();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDetailDto> getBookDetails(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(toDetail(book));
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<BookDetailDto> addCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest request) {
        Book book = bookService.addCategory(id, request.getCategory());
        return ResponseEntity.ok(toDetail(book));
    }

    @DeleteMapping("/{id}/categories/{category}")
    public ResponseEntity<BookDetailDto> removeCategory(
            @PathVariable Long id,
            @PathVariable String category) {
        Book book = bookService.removeCategory(id, category);
        return ResponseEntity.ok(toDetail(book));
    }
}
