package com.example.javanoteapp.view.editnotepage;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.javanoteapp.data.models.NoteModel;
import com.example.javanoteapp.data.repository.DBProvider;

public class EditNoteViewModel extends ViewModel {
    private NoteModel note = NoteModel.getEmptyNote();

    public EditNoteViewModel(){
        Log.i("EditNoteViewModel", "ViewModel has been created");
    }

    public void setNote(NoteModel model){
        note = model;
    }

    public int getId(){
        return note.getId();
    }

    public String getDate(){
        return note.getDate();
    }

    public void setTitle(String title){
        note.setTitle(title);
    }

    public String getTitle(){
        return note.getTitle();
    }

    public void setNoteBody(String body){
        note.setBody(body);
    }

    public String getNoteBody(){
        return note.getBody();
    }

    String saveNote(){
        String tempString;
        if(note.getId() > -1){
            final boolean result = DBProvider.getInstance().updateNote(note);
            if(result){
                tempString = "note updated!";
            }else{
                tempString = "something went wrong!";
            }
        }else{
            final boolean result = DBProvider.getInstance().addNote(note);
            setNewId();
            if(result){
                tempString = "note successfully added to database!";
            }else{
                tempString = "something went wrong!, try again";
            }
        }
        return tempString;
    }

    public void setNewId(){
        note.setId(DBProvider.getInstance().getLastNoteId());
    }

    boolean deleteFromDatabase(){
        return DBProvider.getInstance().deleteNote(note.getId());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i("EditNoteViewModel", "ViewModel has been destroyed");
    }
}
