package com.bookgoblin.server.controller;

import com.bookgoblin.server.model.Book;
import com.bookgoblin.server.service.OpenLibraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/openlibrary") // Distinct path for Open Library operations
public class OpenLibraryController {

    private final OpenLibraryService openLibraryService;

    public OpenLibraryController(OpenLibraryService openLibraryService) {
        this.openLibraryService = openLibraryService;
    }

    /**
     * Searches the Open Library API.
     * @param query The search query
     * @return A list of books from Open Library (not yet saved to our DB)
     */
    @GetMapping("/search")
    public List<Book> searchOpenLibrary(@RequestParam String query) {
        return openLibraryService.searchBooks(query);
    }
}