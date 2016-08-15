package com.jersey.ch01.server.messenger.exception;

/**
 * Created by Shubo on 7/25/2015.
 */
public class DataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    public DataNotFoundException(String error){
        super(error);
    }
}
