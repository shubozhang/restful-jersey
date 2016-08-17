package com.jersey.ch02.books.async;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import com.jersey.MyApplication;
import com.jersey.ch02.books.async.Book;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * Created by shubo.zhang on 8/17/2016.
 */
public class BookAsyncResourceTest extends JerseyTest{

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        JacksonJsonProvider json = new JacksonJsonProvider().
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).
                configure(SerializationFeature.INDENT_OUTPUT, true);

        JacksonXMLProvider xml = new JacksonXMLProvider().
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).
                configure(SerializationFeature.INDENT_OUTPUT, true);
        /*return new ResourceConfig()
                .packages("com.jersey.ch02.books.async")
                .register(JacksonFeature.class);*/

        return new MyApplication().packages("com.jersey").register(json).register(xml);
    }

    private String book1_id;
    private String book2_id;

    @Before
    public void setupBooks() {
        book1_id = addBook("author1", "title1", new Date(), "1234").readEntity(Book.class).getId();
        book2_id = addBook("author2", "title2", new Date(), "2345").readEntity(Book.class).getId();
    }

    protected Response addBook(String author, String title, Date published, String isbn) {
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setPublished(published);
        book.setIsbn(isbn);
        Entity<Book> bookEntity = Entity.entity(book, MediaType.APPLICATION_JSON_TYPE);
        return(target("asyncbooks").request().post(bookEntity));
    }

    /*
    * You have to remove jersey-media-moxy jar and add jackson for Object mapping
    * */
    @Test
    public void testGetBooks() throws Exception {
        List<Book> bookList = target("asyncbooks").request().get(new GenericType<List<Book>>() {});

        assertTrue(2 == bookList.size());

    }

    @Test
    public void testGetBook() throws Exception {

    }

    @Test
    public void testAddBook() throws Exception {

    }
}