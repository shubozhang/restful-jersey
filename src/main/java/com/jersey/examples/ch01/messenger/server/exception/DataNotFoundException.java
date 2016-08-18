package com.jersey.examples.ch01.messenger.server.exception;

/**
 * Created by Shubo on 7/25/2015.
 */
public class DataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    public DataNotFoundException(String error){
        super(error);
    }
}
