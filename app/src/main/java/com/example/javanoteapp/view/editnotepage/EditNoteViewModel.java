package com.example.javanoteapp.view.editnotepage;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.javanoteapp.data.models.NoteModel;
import com.example.javanoteapp.data.repository.DBProvider;

public class EditNoteViewModel extends ViewModel {
    NoteModel note = NoteModel.getEmptyNote();

    public EditNoteViewModel(){
        Log.i("EditNoteViewModel", "ViewModel has been created");
    }

    void setNote(NoteModel model){
        note = model;
    }

    void setTitle(String title){
        note.setTitle(title);
    }

    void setNoteBody(String body){
        note.setBody(body);
    }

    boolean saveNote(){
        return DBProvider.getInstance().addNote(note);
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
