package com.example.javanoteapp.view.notelistpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.javanoteapp.R;
import com.example.javanoteapp.view.editnotepage.EditNoteActivity;
import com.example.javanoteapp.view.notelistpage.adapters.NoteListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteListActivity extends AppCompatActivity {
    NoteListViewModel viewModel;
    RecyclerView noteListRV;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list_activity);

        viewModel = new ViewModelProvider(this).get(NoteListViewModel.class);

        noteListRV = findViewById(R.id.noteListRV);
        noteListRV.setLayoutManager(new LinearLayoutManager(this));
        NoteListAdapter adapter = new NoteListAdapter(viewModel.noteList);
        noteListRV.setAdapter(adapter);

        fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(NoteListActivity.this, EditNoteActivity.class);
            startActivity(intent);
        });
    }
}