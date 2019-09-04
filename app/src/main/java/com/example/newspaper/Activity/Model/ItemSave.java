package com.example.newspaper.Activity.Model;

public class ItemSave {
    private String title;
    private String document;

    public ItemSave(String title, String document) {
        this.title = title;
        this.document = document;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
