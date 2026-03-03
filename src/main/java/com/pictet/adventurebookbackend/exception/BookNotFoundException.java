package com.pictet.adventurebookbackend.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) { super(message); }
}
