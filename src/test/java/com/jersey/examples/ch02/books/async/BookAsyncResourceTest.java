package com.jersey.examples.ch02.books.async;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.jersey.MyApplication;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.*;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;


/**
 * Created by shubo.zhang on 8/17/2016.
 */
public class BookAsyncResourceTest extends JerseyTest{

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        return new MyApplication().packages("com.jersey.examples.ch02.books");
    }


    @Override
    protected void configureClient(ClientConfig clientConfig) {
        JacksonJsonProvider json = new JacksonJsonProvider();
        json.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        // Jackson will not write null as map values. FOr testing AddBookNoAuthor().
        clientConfig.register(json);

        // To support PATCH request
        clientConfig.connectorProvider(new GrizzlyConnectorProvider());
    }

    private String book1_id;
    private String book2_id;

    @Before
    public void setupBooks() {
        book1_id = (String) toHashMap(addBook("author1", "title1", new Date(), "1234")).get("id");
        book2_id = (String) toHashMap(addBook("author2", "title2", new Date(), "2345")).get("id");
    }

    protected Response addBook(String author, String title, Date published, String isbn, String... extras) {
        HashMap<String,Object> book = new HashMap<String,Object>();
        book.put("author",author);
        book.put("title",title);
        book.put("published",published);
        book.put("isbn",isbn);
        if (extras != null) {
            int count = 1;
            for (String s : extras) {
                book.put("extra"+ count++, s);
            }
        }
        Entity<HashMap<String,Object>> bookEntity = Entity.entity(book, MediaType.APPLICATION_JSON_TYPE);
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
    public void testGetBooksWithResponse() {
        Collection<HashMap<String,Object>> response = target("asyncbooks").request()
                .get(new GenericType<Collection<HashMap<String,Object>>>() {});
        assertEquals(2, response.size());
    }

    @Test
    public void testGetBook() {
        HashMap<String,Object> response = toHashMap(target("asyncbooks").path(book1_id).request().get());
        assertNotNull(response);
    }

    @Test
    public void testAddBook() throws Exception {
        Date thisDate = new Date();

        Response response = addBook("author", "title", thisDate, "3456");
        assertEquals(200, response.getStatus());

        HashMap<String,Object> responseBook = toHashMap(response);
        assertNotNull(responseBook.get("id"));
        assertEquals("title",responseBook.get("title"));
        assertEquals("author",responseBook.get("author"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        assertEquals(thisDate, dateFormat.parse((String)responseBook.get("published")));
        assertEquals("3456",responseBook.get("isbn"));
    }


    @Test
    public void testAddExtraField() {
        Response response = addBook("author", "title", new Date(), "1111", "hello world");
        assertEquals(200, response.getStatus());

        HashMap<String,Object> book = toHashMap(response);
        assertNotNull(book.get("id"));
        assertEquals(book.get("extra1"),"hello world");
    }

    @Test
    public void getBooksAsXml() {
        String output = target("asyncbooks").request(MediaType.APPLICATION_XML).get().readEntity(String.class);
        XML xml = new XMLDocument(output);

        assertEquals("author1", xml.xpath("/books/book[@id='" + book1_id + "']/author/text()").get(0));
        assertEquals("title1", xml.xpath("/books/book[@id='" + book1_id + "']/title/text()").get(0));

        assertEquals(2, xml.xpath("//book/author/text()").size());
    }

    @Test
    public void AddBookNoAuthor() {
        Response response = addBook(null, "title1", new Date(), "1234");
        assertEquals(400, response.getStatus());
        String message = response.readEntity(String.class);
        System.out.println("message: " + message);

        /*
        * Need to setup property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true) in MyApplication.
        * */
        assertTrue(message.contains("author is a required field"));
    }

    @Test
    public void AddBookNoTitle() {
        Response response = addBook("author1", null, new Date(), "1234");
        assertEquals(400, response.getStatus());
        String message = response.readEntity(String.class);
        assertTrue(message.contains("title is a required field"));
    }

    @Test
    public void AddBookNoBook() {
        Response response = target("asyncbooks").request().post(null);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void BookNotFoundWithMessage() {
        Response response = target("asyncbooks").path("1").request().get();
        assertEquals(404, response.getStatus());

        String message = response.readEntity(String.class);
        assertTrue(message.contains("Book 1 is not found"));
    }

    @Test
    public void BookEntityTagNotModified() {
        EntityTag entityTag = target("asyncbooks").path(book1_id).request().get().getEntityTag();
        assertNotNull(entityTag);

        System.out.println(entityTag);

        Response response = target("asyncbooks").path(book1_id).request().header("If-None-Match", entityTag).get();
        assertEquals(304, response.getStatus());
    }

    @Test
    public void UpdateBookAuthor() {
        HashMap<String,Object> updates = new HashMap<String,Object>();
        updates.put("author","updatedAuthor");
        Entity<HashMap<String,Object>> updateEntity = Entity.entity(updates, MediaType.APPLICATION_JSON);
        Response updateResponse = target("asyncbooks").path(book1_id).request().build("PATCH", updateEntity).invoke();

        assertEquals(200, updateResponse.getStatus());

        Response getResponse = target("asyncbooks").path(book1_id).request().get();
        HashMap<String,Object> getResponseMap = toHashMap(getResponse);

        assertEquals("updatedAuthor", getResponseMap.get("author"));
    }

    @Test
    public void UpdateBookExtra() {
        HashMap<String,Object> updates = new HashMap<String,Object>();
        updates.put("hello","world");
        Entity<HashMap<String,Object>> updateEntity = Entity.entity(updates, MediaType.APPLICATION_JSON);
        Response updateResponse = target("asyncbooks").path(book1_id).request().build("PATCH", updateEntity).invoke();

        assertEquals(200, updateResponse.getStatus());

        Response getResponse = target("asyncbooks").path(book1_id).request().get();
        HashMap<String,Object> getResponseMap = toHashMap(getResponse);

        assertEquals("world", getResponseMap.get("hello"));
    }

    @Test
    public void UpdateIfMatch() {
        EntityTag entityTag = target("asyncbooks").path(book1_id).request().get().getEntityTag();

        HashMap<String,Object> updates = new HashMap<String,Object>();
        updates.put("author","updatedAuthor");
        Entity<HashMap<String,Object>> updateEntity = Entity.entity(updates, MediaType.APPLICATION_JSON);
        Response updateResponse = target("asyncbooks").path(book1_id).request().
                header("If-Match",entityTag).build("PATCH", updateEntity).invoke();

        assertEquals(200, updateResponse.getStatus());

        Response updateResponse2 = target("asyncbooks").path(book1_id).request().
                header("If-Match",entityTag).build("PATCH", updateEntity).invoke();

        assertEquals(412, updateResponse2.getStatus());
    }


    @Test
    public void PatchMethodOverride() {
        HashMap<String,Object> updates = new HashMap<String,Object>();
        updates.put("author","updatedAuthor");
        Entity<HashMap<String,Object>> updateEntity = Entity.entity(updates, MediaType.APPLICATION_JSON);
        Response updateResponse = target("asyncbooks").path(book1_id).queryParam("_method", "PATCH").
                request().post(updateEntity);

        assertEquals(200, updateResponse.getStatus());

        Response getResponse = target("asyncbooks").path(book1_id).request().get();
        HashMap<String,Object> getResponseMap = toHashMap(getResponse);

        assertEquals("updatedAuthor", getResponseMap.get("author"));
    }

    @Test
    public void ContentNegotiationExtensions() {
        Response xmlResponse = target("asyncbooks").path(book1_id+".xml").request().get();
        assertEquals(MediaType.APPLICATION_XML, xmlResponse.getHeaderString("Content-Type"));

        Response jsonResponse = target("asyncbooks").path(book1_id+".json").request().get();
        assertEquals(MediaType.APPLICATION_JSON, jsonResponse.getHeaderString("Content-Type"));
    }

    @Test
    public void PoweredByHeader() {
        // getBook() has @PoweredBy
        Response response = target("asyncbooks").path(book1_id).request().get();
        assertEquals("BRYAN", response.getHeaderString("X-Powered-By"));

        // getBooks() does not have @PoweredBy
        Response response2 = target("asyncbooks").request().get();
        assertNull(response2.getHeaderString("X-Powered-By"));
    }

    private HashMap<String,Object> toHashMap(Response response) {
        return(response.readEntity(new GenericType<HashMap<String,Object>>() {}));
    }


}