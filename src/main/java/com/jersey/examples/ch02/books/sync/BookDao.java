package com.jersey.examples.ch02.books.sync;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository("bookDao")
public class BookDao {

    private Map<String,Book> books;

    BookDao() {
        books = new ConcurrentHashMap<String,Book>();
    }


    Book getBook(String id) {
        return(books.get(id));
    }

    Collection<Book> getBooks() {
        return(books.values());
    }

    Book addBook(Book book) {
        book.setId(UUID.randomUUID().toString());
        books.put(book.getId(), book);
        return(book);
    }


}