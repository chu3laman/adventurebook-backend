package com.pictet.adventurebookbackend.mapper;

import com.pictet.adventurebookbackend.dto.BookJson;
import com.pictet.adventurebookbackend.model.Book;
import com.pictet.adventurebookbackend.repository.BookRepository;
import com.pictet.adventurebookbackend.service.BookValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class BookDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BookDataLoader.class);

    private final BookRepository bookRepository;
    private final BookValidator bookValidator;
    private final BookMapper bookMapper;
    private final ObjectMapper objectMapper;

    public BookDataLoader(BookRepository bookRepository,
            BookValidator bookValidator,
            BookMapper bookMapper,
            ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.bookValidator = bookValidator;
        this.bookMapper = bookMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() > 0) {
            log.info("Books already loaded, skipping initialization");
            return;
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:books/*.json");

        for (Resource resource : resources) {
            String filename = resource.getFilename();
            try {
                BookJson bookJson = objectMapper.readValue(resource.getInputStream(), BookJson.class);
                Book book = bookMapper.toEntity(bookJson);

                List<String> errors = bookValidator.validate(book);
                if (!errors.isEmpty()) {
                    log.warn("Book '{}' from file {} is invalid:", book.getTitle(), filename);
                    errors.forEach(e -> log.warn("  - {}", e));
                    log.warn("Skipping this book.");
                    continue;
                }

                bookRepository.save(book);
                log.info("Loaded book: '{}' by {} ({} sections)",
                        book.getTitle(), book.getAuthor(), book.getSections().size());

            } catch (Exception e) {
                log.error("Failed to load book from file {}: {}", filename, e.getMessage());
            }
        }

        log.info("Book loading complete. {} valid books in database.", bookRepository.count());
    }
}
