package com.bookgoblin.server.service;

import com.bookgoblin.server.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenLibraryService {

    private static final String OPEN_LIBRARY_SEARCH_URL = "https://openlibrary.org/search.json?q={query}&fields=title,author_name,isbn,first_publish_year,cover_i,edition_key&limit=20";

    private final RestTemplate restTemplate;

    public OpenLibraryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Book> searchBooks(String query) {
        List<Book> books = new ArrayList<>();

        try {
            // Make the GET request to the Open Library API
            ResponseEntity<OpenLibraryResponse> response = restTemplate.getForEntity(
                    OPEN_LIBRARY_SEARCH_URL,
                    OpenLibraryResponse.class,
                    query // This replaces the {query} placeholder in the URL
            );

            OpenLibraryResponse body = response.getBody();

            // Check if we got a response and map the docs to our Book model
            if (body != null && body.getDocs() != null) {
                for (OpenLibraryBook olBook : body.getDocs()) {
                    Book book = mapOpenLibraryBookToBook(olBook);
                    books.add(book);
                }
            }

        } catch (RestClientException e) {
            // Handle errors from the API (e.g., 4xx, 5xx, network issues)
            System.err.println("Error calling Open Library API: " + e.getMessage());
            // You could throw a custom exception here to be handled by the controller
            // throw new OpenLibraryException("Failed to search Open Library: " + e.getMessage(), e);
        }

        return books;
    }

    /**
     * Maps a book object from the Open Library API format to our internal Book model.
     * @param olBook The book object from the Open Library response
     * @return A Book object for our application
     */
    private Book mapOpenLibraryBookToBook(OpenLibraryBook olBook) {
        Book book = new Book();

        book.setTitle(olBook.getTitle());
        // Open Library returns a list of authors, take the first one if available
        book.setAuthor(olBook.getAuthor_name() != null && !olBook.getAuthor_name().isEmpty()
                ? olBook.getAuthor_name().get(0)
                : "Unknown Author");
        // Take the first ISBN from the list if available
        book.setIsbn(olBook.getIsbn() != null && !olBook.getIsbn().isEmpty()
                ? olBook.getIsbn().get(0)
                : null);
        book.setPublicationYear(olBook.getFirst_publish_year());

        // Build the cover image URL using the cover ID (cover_i)
        if (olBook.getCover_i() != null) {
            String coverUrl = "https://covers.openlibrary.org/b/id/" + olBook.getCover_i() + "-M.jpg";
            book.setCoverImageUrl(coverUrl);
        }
        // Alternatively, use the OLID from edition_key for a more stable URL
        // if (olBook.getEdition_key() != null && !olBook.getEdition_key().isEmpty()) {
        //     String coverUrl = "https://covers.openlibrary.org/b/olid/" + olBook.getEdition_key().get(0) + "-M.jpg";
        //     book.setCoverImageUrl(coverUrl);
        // }

        return book;
    }

    // --- Inner Classes to Model the Open Library API Response ---
    // These classes are used by Jackson to deserialize the JSON response.

    /**
     * The top-level response object from Open Library search.
     */
    public static class OpenLibraryResponse {
        private List<OpenLibraryBook> docs;

        public List<OpenLibraryBook> getDocs() {
            return docs;
        }

        public void setDocs(List<OpenLibraryBook> docs) {
            this.docs = docs;
        }
    }

    /**
     * Represents an individual book in the Open Library response.
     * Only includes fields we care about.
     */
    public static class OpenLibraryBook {
        private String title;
        private List<String> author_name;
        private List<String> isbn;
        private Integer first_publish_year;
        private Integer cover_i;
        private List<String> edition_key;

        // Getters and Setters are REQUIRED for Jackson to work
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public List<String> getAuthor_name() { return author_name; }
        public void setAuthor_name(List<String> author_name) { this.author_name = author_name; }

        public List<String> getIsbn() { return isbn; }
        public void setIsbn(List<String> isbn) { this.isbn = isbn; }

        public Integer getFirst_publish_year() { return first_publish_year; }
        public void setFirst_publish_year(Integer first_publish_year) { this.first_publish_year = first_publish_year; }

        public Integer getCover_i() { return cover_i; }
        public void setCover_i(Integer cover_i) { this.cover_i = cover_i; }

        public List<String> getEdition_key() { return edition_key; }
        public void setEdition_key(List<String> edition_key) { this.edition_key = edition_key; }
    }
}