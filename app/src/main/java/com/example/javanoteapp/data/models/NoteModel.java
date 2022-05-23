package com.example.javanoteapp.data.models;

import java.util.Calendar;

public class NoteModel {
    int id;
    String title, body;
    String date;
    boolean isSelected = false;

    public NoteModel(int id, String title, String date, String body){
        this.id = id;
        this.title = title;
        this.date = date;
        this.body = body;
    }

    public static NoteModel getEmptyNote() {
        return new NoteModel(-1,
                "",
                Calendar.getInstance().getTime().toString(),
                "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
