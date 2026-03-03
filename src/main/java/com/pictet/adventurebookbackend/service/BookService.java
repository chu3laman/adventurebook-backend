package com.pictet.adventurebookbackend.service;

import com.pictet.adventurebookbackend.exception.BookNotFoundException;
import com.pictet.adventurebookbackend.exception.CategoryNotFoundException;
import com.pictet.adventurebookbackend.model.Book;
import com.pictet.adventurebookbackend.model.enums.Difficulty;
import com.pictet.adventurebookbackend.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(String title, String author, Difficulty difficulty, String category) {
        return bookRepository.searchBooks(title, author, difficulty, category);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    public Book addCategory(Long bookId, String category) {
        Book book = getBookById(bookId);
        book.getCategories().add(category.toUpperCase());
        return bookRepository.save(book);
    }

    public Book removeCategory(Long bookId, String category) {
        Book book = getBookById(bookId);
        boolean removed = book.getCategories().remove(category.toUpperCase());
        if (!removed) {
            throw new CategoryNotFoundException("Category '" + category + "' not found on book: " + bookId);
        }
        return bookRepository.save(book);
    }
}
