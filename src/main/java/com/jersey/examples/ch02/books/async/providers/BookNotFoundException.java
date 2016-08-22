package com.jersey.examples.ch02.books.async.providers;

/**
 * Created by shubo.zhang on 8/18/2016.
 */
public class BookNotFoundException extends Exception {

    BookNotFoundException(String m) {
        super(m);
    }
}