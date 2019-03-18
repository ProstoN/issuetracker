package com.axmor;

public class Comment {
    private int id;
    private String text;
    private int issueId;
    private String date;
    private String author;

    public String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIssueId() {
        return issueId;
    }

    void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }

}
