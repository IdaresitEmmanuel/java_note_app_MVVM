package com.example.javanoteapp.view.notelistpage;

import androidx.lifecycle.ViewModel;

import com.example.javanoteapp.data.models.NoteModel;
import com.example.javanoteapp.data.repository.DBProvider;

import java.util.ArrayList;

public class NoteListViewModel extends ViewModel {
    ArrayList<NoteModel> noteList = new ArrayList<>();
    ArrayList<NoteModel> noteListFiltered = new ArrayList<>();
    boolean isActionMode = false;

    public NoteListViewModel(){
        refreshAllNotes();

    }

    public void refreshNotes(){
        noteList = DBProvider.getInstance().getNotes();
        for(int i = 0; i < noteListFiltered.size(); i++){
            boolean shouldDelete = true;
            for(NoteModel model : noteList){
                if (model.getId() == noteListFiltered.get(i).getId()) {
                    shouldDelete = false;
                    break;
                }
            }
            if(shouldDelete){
                noteListFiltered.remove(noteListFiltered.get(i));
            }
        }
    }

    public void refreshAllNotes(){
        noteList = DBProvider.getInstance().getNotes();
        noteListFiltered.addAll(noteList);
    }

    public void setIsActionMode(boolean value){
        isActionMode = value;
    }

    public ArrayList<NoteModel> getSelectedList(){
        ArrayList<NoteModel> tempList = new ArrayList<>();
        for(int i = 0; i < noteList.size(); i++){
            if(noteList.get(i).getIsSelected()){
                tempList.add(noteList.get(i));
            }
        }
        return tempList;
    }

    public boolean deleteSelectedNotes(){
        ArrayList<NoteModel> selectedList = getSelectedList();
        boolean isDeleteSuccessful = false;
        for(int i = 0; i < selectedList.size(); i++){
            isDeleteSuccessful = DBProvider.getInstance().deleteNote(selectedList.get(i).getId());
        }
        return isDeleteSuccessful;
    }

    // Filter
    public void filterList(String query){
        noteListFiltered.clear();
        if(query.isEmpty()){
            noteListFiltered.addAll(noteList);
            return;
        }
        for(NoteModel note: noteList){
            if(note.getTitle().toLowerCase().contains(query.toLowerCase())){
                noteListFiltered.add(note);
            }
        }
    }
}
