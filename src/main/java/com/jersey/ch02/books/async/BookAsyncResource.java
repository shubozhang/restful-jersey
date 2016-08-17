package com.jersey.ch02.books.async;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.glassfish.jersey.server.ManagedAsync;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import java.util.Collection;

@Path("/asyncbooks")
public class BookAsyncResource {

    @Context
    Request request;

    @Autowired
    BookAsyncDao bookAsyncDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getBooks(@Suspended final AsyncResponse response) {
        //response.resume(bookAsyncDao.getBooks());
        ListenableFuture<Collection<Book>> booksFuture = bookAsyncDao.getBooksAsync();
        Futures.addCallback(booksFuture, new FutureCallback<Collection<Book>>() {
            public void onSuccess(Collection<Book> books) {
                response.resume(books);
            }
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getBook(@PathParam("id") String id, @Suspended final AsyncResponse response) {
        ListenableFuture<Book> bookFuture = bookAsyncDao.getBookAsync(id);
        Futures.addCallback(bookFuture, new FutureCallback<Book>() {
            public void onSuccess(Book book) {
                response.resume(book);
            }
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void addBook(Book book, @Suspended final AsyncResponse response) {
        ListenableFuture<Book> bookFuture = bookAsyncDao.addBookAsync(book);
        Futures.addCallback(bookFuture, new FutureCallback<Book>() {
            public void onSuccess(Book addedBook) {
                response.resume(addedBook);
            }
            public void onFailure(Throwable thrown) {
                System.out.println(thrown.getMessage());
                response.resume(thrown);
            }
        });
    }

}
