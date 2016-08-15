package com.jersey.ch01.server.messenger.model;

/**
 * Created by Shubo on 7/25/2015.
 */
public class Link {

    private String link;
    private String rel;

    public Link() {
    }

    public Link(String link, String rel) {
        this.link = link;
        this.rel = rel;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
