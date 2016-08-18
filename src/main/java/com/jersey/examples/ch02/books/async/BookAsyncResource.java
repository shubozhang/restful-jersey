package com.jersey.examples.ch02.books.async;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.codec.digest.DigestUtils;
import org.glassfish.jersey.server.ManagedAsync;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.util.Collection;

@Path("/asyncbooks")
//@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({"application/json;qs=1", "application/xml;qs=0.5"}) // server prefer to return json format
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class BookAsyncResource {

    @Context
    Request request;

    @Autowired
    BookAsyncDao bookAsyncDao;

    @GET
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


    /*
    * Client passes additional headers to server allowing server to response intelligently
    * depending on the resources be requested has been changed since last time called retrieved it.
    *
    * Conditional get request: If-None-Match and If-Modified-Since headers supported
    * */
    @PoweredBy("BRYAN")
    @Path("/{id}")
    @GET
    @ManagedAsync
    public void getBook(@PathParam("id") String id, @Suspended final AsyncResponse response) {
        ListenableFuture<Book> bookFuture = bookAsyncDao.getBookAsync(id);
        Futures.addCallback(bookFuture, new FutureCallback<Book>() {
            public void onSuccess(Book book) {
                // response.resume(book);
                EntityTag entityTag = generateEntityTag(book);
                Response.ResponseBuilder rb = request.evaluatePreconditions(entityTag);
                if (rb != null) {
                    response.resume(rb.build());
                } else {
                    response.resume(Response.ok().tag(entityTag).entity(book).build());
                }
            }
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    @POST
    @ManagedAsync
    public void addBook(@Valid @NotNull Book book, @Suspended final AsyncResponse response) {
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

    @Path("/{id}")
    @PATCH
    @ManagedAsync
    public void updateBook(@PathParam("id") final String id, final Book book, @Suspended final AsyncResponse response) {
        ListenableFuture<Book> getBookFuture = bookAsyncDao.getBookAsync(id);
        Futures.addCallback(getBookFuture, new FutureCallback<Book>() {
            public void onSuccess(Book originalBook) {
                Response.ResponseBuilder rb = request.evaluatePreconditions(generateEntityTag(originalBook));
                if (rb != null) {
                    response.resume(rb.build());
                } else {
                    ListenableFuture<Book> bookFuture = bookAsyncDao.updateBookAsync(id, book);
                    Futures.addCallback(bookFuture, new FutureCallback<Book>() {
                        public void onSuccess(Book updatedBook) {
                            response.resume(updatedBook);
                        }
                        public void onFailure(Throwable thrown) {
                            response.resume(thrown);
                        }
                    });
                }
            }
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });

    }

    EntityTag generateEntityTag(Book book) {
        return(new EntityTag(DigestUtils.md5Hex(book.getAuthor()+
                book.getTitle()+book.getPublished()+book.getExtras())));
    }
}
