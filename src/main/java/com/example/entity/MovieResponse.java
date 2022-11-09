package com.example.entity;

public class MovieResponse {
    private String title;
    private String creator;

    public MovieResponse(String title, String creator) {
        this.title = title;
        this.creator = creator;
    }

    public MovieResponse() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
