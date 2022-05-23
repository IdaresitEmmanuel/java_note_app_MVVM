package com.example.javanoteapp.view.notelistpage;

import androidx.lifecycle.ViewModel;

import com.example.javanoteapp.data.models.NoteModel;
import com.example.javanoteapp.data.repository.DBProvider;

import java.util.ArrayList;

public class NoteListViewModel extends ViewModel {
    ArrayList<NoteModel> noteList = new ArrayList<NoteModel>();

    public NoteListViewModel(){
        getNotes();
    }

    private void getNotes(){
        noteList.addAll(DBProvider.getInstance().getNotes());
    }
}
