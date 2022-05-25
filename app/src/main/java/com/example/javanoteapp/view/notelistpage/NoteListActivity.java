package com.example.javanoteapp.view.notelistpage;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.javanoteapp.R;
import com.example.javanoteapp.data.models.NoteModel;
import com.example.javanoteapp.view.editnotepage.EditNoteActivity;
import com.example.javanoteapp.view.notelistpage.adapters.ItemClickListener;
import com.example.javanoteapp.view.notelistpage.adapters.ItemLongClickListener;
import com.example.javanoteapp.view.notelistpage.adapters.NoteListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteListActivity extends AppCompatActivity {
    NoteListViewModel viewModel;
    RecyclerView noteListRV;
    FloatingActionButton fab;
    NoteListAdapter adapter;
    LinearLayoutCompat searchBar, actionBar;
    ImageButton close_action_bar_ibn, delete_btn;

    @Override
    public void onBackPressed() {
        if(viewModel.isActionMode){
            setActionMode(false);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list_activity);
        Toast.makeText(this, "has been called onCreate()", Toast.LENGTH_SHORT).show();
        viewModel = new ViewModelProvider(this).get(NoteListViewModel.class);

        setUpUI();
    }

    public void setUpActionMode(){
        if(viewModel.isActionMode){
            searchBar.setVisibility(View.GONE);
            actionBar.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            close_action_bar_ibn.setVisibility(View.VISIBLE);
        }else{

            searchBar.setVisibility(View.VISIBLE);
            actionBar.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            close_action_bar_ibn.setVisibility(View.GONE);
        }
    }

    public void setActionMode(boolean b){
        viewModel.setIsActionMode(b);
        setUpActionMode();
        setUpRecyclerAdapter();
    }

    public void setUpRecyclerAdapter(){
        adapter = new NoteListAdapter(viewModel.isActionMode, viewModel.noteList, new ItemClickListener() {
            @Override
            public void itemClick(NoteModel model, int position) {
                if(viewModel.isActionMode){
                    adapter.selectNote(position);
                    if(viewModel.getSelectedList().isEmpty()){
                        setActionMode(false);
                    }

                }else{
                    Intent intent = new Intent(NoteListActivity.this, EditNoteActivity.class);
                    intent.putExtra("id", model.getId());
                    intent.putExtra("title", model.getTitle());
                    intent.putExtra("date", model.getDate());
                    intent.putExtra("body", model.getBody());
                    someActivityResultLauncher.launch(intent);
                }
            }
        }, new ItemLongClickListener(){
            @Override
            public void onItemLongClick(View v, NoteModel model, int position) {
                if(!viewModel.isActionMode){
                    setActionMode(true);
                    adapter.selectNote(position);
                }else{
                    v.callOnClick();
                }
            }
        });
        noteListRV.setAdapter(adapter);
        if(!viewModel.isActionMode){
            adapter.unselectAll();
        }
    }

    public void setUpUI(){
        close_action_bar_ibn = findViewById(R.id.close_action_bar_ibn);
        close_action_bar_ibn.setOnClickListener(view -> setActionMode(false));
        searchBar = findViewById(R.id.search_bar);
        actionBar = findViewById(R.id.action_bar);

        noteListRV = findViewById(R.id.noteListRV);
        noteListRV.setLayoutManager(new LinearLayoutManager(this));

        setUpRecyclerAdapter();

        fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(NoteListActivity.this, EditNoteActivity.class);
            someActivityResultLauncher.launch(intent);
        });
        setUpActionMode();

        delete_btn = findViewById(R.id.delete_ibn);
        delete_btn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to delete " + viewModel.getSelectedList().size() + " items?")
                    .setPositiveButton("yes", (dialogInterface, i) -> {
                        final boolean result = viewModel.deleteSelectedNotes();
                        if(result){
                            Toast.makeText(NoteListActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(NoteListActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                        }
                        dialogInterface.dismiss();
                        viewModel.refreshNotes();
                        setActionMode(false);
                    })
                    .setNegativeButton("no", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alert = builder.create();
            if(!viewModel.getSelectedList().isEmpty()){
                alert.show();
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        viewModel.refreshNotes();
                        setUpUI();
                    }
                }
            });

}