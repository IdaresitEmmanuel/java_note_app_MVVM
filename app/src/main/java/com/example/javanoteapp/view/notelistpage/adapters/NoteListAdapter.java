package com.example.javanoteapp.view.notelistpage.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javanoteapp.R;
import com.example.javanoteapp.data.models.NoteModel;

import java.util.ArrayList;

public class NoteListAdapter
        extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    ArrayList<NoteModel> noteList = new ArrayList<NoteModel>();
    public NoteListAdapter(){
        NoteModel note = new NoteModel("Note Title", "Lorem Ipsum has been the industry\\'s standard text ever\n" +
                "since the 1500s, when an unknown printer of type\n" +
                "and scrambled it to also make a type specimen. It has a\n" +
                "survived not only five centuries, but also in the leap into…",
                "Jan 20th 2020 03:04 PM");
        for(int i = 0; i < 10; i++){
            noteList.add(note);
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView noteTitle, noteDate, noteBody;
        private CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDate = itemView.findViewById(R.id.note_date);
            noteBody = itemView.findViewById(R.id.note_body);
            checkBox = itemView.findViewById(R.id.note_checkbox);
        }
    }

    @NonNull
    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_model, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.ViewHolder holder, int position) {
        NoteModel model = noteList.get(position);
        holder.noteTitle.setText(model.getTitle());
        holder.noteDate.setText(model.getDate());
        holder.noteBody.setText(model.getBody());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
