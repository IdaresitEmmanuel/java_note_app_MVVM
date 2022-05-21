package com.example.javanoteapp.view.notelistpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.javanoteapp.R;
import com.example.javanoteapp.view.notelistpage.adapters.NoteListAdapter;

public class NoteListActivity extends AppCompatActivity {
    RecyclerView noteListRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list_activity);

        noteListRV = findViewById(R.id.noteListRV);
        noteListRV.setLayoutManager(new LinearLayoutManager(this));
        NoteListAdapter adapter = new NoteListAdapter();
        noteListRV.setAdapter(adapter);
    }
}