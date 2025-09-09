package com.bookgoblin.server.controller;


import com.bookgoblin.server.dao.ReadingLogDao;
import com.bookgoblin.server.exception.DaoException;
import com.bookgoblin.server.model.ReadingLog;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/reading-logs")
public class ReadingLogController {

    private final ReadingLogDao readingLogDao;

    public ReadingLogController(ReadingLogDao readingLogDao) {
        this.readingLogDao = readingLogDao;
    }

    @GetMapping("/user-book/{userBookId}")
    @PreAuthorize("isAuthenticated()")
    public List<ReadingLog> getReadingLogsByUserBookId(@PathVariable int userBookId, Principal principal) {
        try {
            // Verify the user has access to this user book
            if (!readingLogDao.hasUserAccessToUserBook(principal.getName(), userBookId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }

            return readingLogDao.getReadingLogsByUserBookId(userBookId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ReadingLog getReadingLogById(@PathVariable int id, Principal principal) {
        try {
            ReadingLog readingLog = readingLogDao.getReadingLogById(id);
            if (readingLog == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reading log not found");
            }

            // Verify the user has access to this reading log
            if (!readingLogDao.hasUserAccessToReadingLog(principal.getName(), id)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }

            return readingLog;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ReadingLog createReadingLog(@Valid @RequestBody ReadingLog readingLog, Principal principal) {
        try {
            // Verify the user has access to the user book
            if (!readingLogDao.hasUserAccessToUserBook(principal.getName(), readingLog.getUserBookId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }

            return readingLogDao.createReadingLog(readingLog);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ReadingLog updateReadingLog(@PathVariable int id, @Valid @RequestBody ReadingLog readingLog, Principal principal) {
        try {
            // Verify the user has access to this reading log
            if (!readingLogDao.hasUserAccessToReadingLog(principal.getName(), id)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }

            readingLog.setLogId(id);
            ReadingLog updatedReadingLog = readingLogDao.updateReadingLog(readingLog);

            if (updatedReadingLog == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reading log not found");
            }

            return updatedReadingLog;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteReadingLog(@PathVariable int id, Principal principal) {
        try {
            // Verify the user has access to this reading log
            if (!readingLogDao.hasUserAccessToReadingLog(principal.getName(), id)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }

            if (!readingLogDao.deleteReadingLog(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reading log not found");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }
}
