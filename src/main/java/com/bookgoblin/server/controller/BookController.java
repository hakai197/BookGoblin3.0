package com.bookgoblin.server.controller;


import com.bookgoblin.server.dao.BookDao;
import com.bookgoblin.server.exception.DaoException;
import com.bookgoblin.server.model.Book;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/books")
public class BookController {

    private final BookDao bookDao;

    public BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        try {
            return bookDao.getBooks();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {
        try {
            Book book = bookDao.getBookById(id);
            if (book == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
            }
            return book;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author) {
        try {
            if (title != null && author != null) {
                return bookDao.getBooksByTitleAndAuthor(title, author);
            } else if (title != null) {
                return bookDao.getBooksByTitle(title);
            } else if (author != null) {
                return bookDao.getBooksByAuthor(author);
            } else {
                return bookDao.getBooks();
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Book createBook(@Valid @RequestBody Book book) {
        try {
            return bookDao.createBook(book);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Book updateBook(@PathVariable int id, @Valid @RequestBody Book book) {
        try {
            book.setBookId(id);
            Book updatedBook = bookDao.updateBook(book);
            if (updatedBook == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
            }
            return updatedBook;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(@PathVariable int id) {
        try {
            if (!bookDao.deleteBook(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }
}
