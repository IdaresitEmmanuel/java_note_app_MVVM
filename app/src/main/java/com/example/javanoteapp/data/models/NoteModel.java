package com.example.javanoteapp.data.models;

public class NoteModel {
    String title, body;
    String date;
    boolean isSelected = false;

    public NoteModel(String title, String body, String date){
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsSelected() {
        return title;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
