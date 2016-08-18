package com.jersey.examples.ch02.books.async;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;

@JsonPropertyOrder({"id"}) // Display id as the first field
@JsonInclude(JsonInclude.Include.NON_NULL) // Don't display null fields
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "book")
public class Book {

    @NotNull(message="title is a required field")
    private String title;
    @NotNull(message="author is a required field")
    private String author;
    //private String isbn; // this can be injected into extras
    private Date published;
    private String id;

    private HashMap<String, Object> extras = new HashMap<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

   /* public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }*/

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    @JacksonXmlProperty(isAttribute = true) //this filed is attribute, not element any more.
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @JsonAnyGetter
    public HashMap<String, Object> getExtras() {
        return extras;
    }

    @JsonAnySetter
    public void setExtras(String key, Object value) {
        this.extras.put(key, value);
    }


}
