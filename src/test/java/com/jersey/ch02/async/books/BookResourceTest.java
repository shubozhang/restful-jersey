package com.jersey.ch02.async.books;


import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/*
* This test uses im-memory grizzly container to run unit testing, so you don't have to
* deploy the war file to tomcat.
* */

public class BookResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig().packages("com.jersey.ch02.async.books");
    }

    @Test
    public void testGetBooks() throws Exception {
        List<Book> bookList = target("books").request().get(new GenericType<List<Book>>() {});

        assertTrue(2 == bookList.size());
    }

    @Test
    public void testGetBooksResponse() throws Exception {
        Response response = target("books").request().get();

        assertTrue(200 == response.getStatus());

        List<Book> books = response.readEntity(new GenericType<List<Book>>() {});
        assertTrue(2 == books.size());
    }

    @Test
    public void testGetBook() throws Exception {

        Book book = target("books").path("1").request().get(Book.class);

        System.out.println(book.toString());
        assertNotNull(book.getId());
    }

    @Test
    public void testGetBookResponse() {
        Response response = target("books").path("1").request().get();

        assertTrue(200 == response.getStatus());

        Book book = response.readEntity(Book.class);
        System.out.println(book);
        assertNotNull(book.getId());
    }

    /*
    * Need to solve dependency injection issue, singleton or prototype
    * */
    @Test
    public void testDao() {
        Book response1 = target("books").path("1").request().get(Book.class);
        Book response2 = target("books").path("1").request().get(Book.class);

        assertTrue(response1.getPublished().getTime() == response2.getPublished().getTime());
    }
}