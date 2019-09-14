package com.example.newspaper.Activity.Model;

public class ItemRelated {
    private String title;
    private String link;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ItemRelated(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
