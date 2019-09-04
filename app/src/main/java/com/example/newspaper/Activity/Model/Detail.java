package com.example.newspaper.Activity.Model;

public class Detail {
    String text;
    String imageLink;
    Boolean bl;

    public Detail(String text, String imageLink, Boolean bl) {
        this.text = text;
        this.imageLink = imageLink;
        this.bl = bl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Boolean getBl() {
        return bl;
    }

    public void setBl(Boolean bl) {
        this.bl = bl;
    }
}
