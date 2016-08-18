package com.jersey.examples.ch02.books.sync;


import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/*
* This test uses im-memory grizzly container to run unit testing, so you don't have to
* deploy the war file to tomcat.
* */

public class BookResourceTest extends JerseyTest {

    private String book1_id;
    private String book2_id;

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig().packages("com.jersey.examples.ch02.books.sync");
    }

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
        return(target("books").request().post(bookEntity));
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

        Book book = target("books").path(book1_id).request().get(Book.class);

        System.out.println(book.toString());
        assertNotNull(book.getId());
    }

    @Test
    public void testGetBookResponse() {
        Response response = target("books").path(book1_id).request().get();

        assertTrue(200 == response.getStatus());

        Book book = response.readEntity(Book.class);
        System.out.println(book);
        assertNotNull(book.getId());
    }

    /*
    * check idempotent
    * */
    @Test
    public void testDao() {
        Book response1 = target("books").path(book1_id).request().get(Book.class);
        Book response2 = target("books").path(book1_id).request().get(Book.class);

        assertTrue(response1.getPublished().getTime() == response2.getPublished().getTime());
    }
}