package com.bookgoblin.server.dao;

import com.bookgoblin.server.model.ReadingLog;

import java.util.List;

public interface ReadingLogDao {
    List<ReadingLog> getReadingLogs();
    ReadingLog getReadingLogById(int logId);
    List<ReadingLog> getReadingLogsByUserBookId(int userBookId);
    ReadingLog createReadingLog(ReadingLog readingLog);
    ReadingLog updateReadingLog(ReadingLog readingLog);
    boolean deleteReadingLog(int logId);
    boolean hasUserAccessToUserBook(String username, int userBookId);
    boolean hasUserAccessToReadingLog(String username, int logId);
}