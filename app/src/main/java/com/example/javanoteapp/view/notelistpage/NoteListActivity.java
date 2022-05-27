package com.example.javanoteapp.view.notelistpage;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.javanoteapp.R;
import com.example.javanoteapp.data.models.NoteModel;
import com.example.javanoteapp.view.editnotepage.EditNoteActivity;
import com.example.javanoteapp.view.notelistpage.adapters.clicklisteners.ItemClickListener;
import com.example.javanoteapp.view.notelistpage.adapters.clicklisteners.ItemLongClickListener;
import com.example.javanoteapp.view.notelistpage.adapters.NoteListAdapter;
import com.example.javanoteapp.view.notelistpage.dialogs.DeleteDialog;
import com.example.javanoteapp.view.notelistpage.dialogs.FilterDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class NoteListActivity extends AppCompatActivity implements FilterDialog.FilterDialogListener, DeleteDialog.DeleteDialogListener {
    NoteListViewModel viewModel;
    RecyclerView noteListRV;
    FloatingActionButton fab;
    NoteListAdapter adapter;
    LinearLayoutCompat searchBar, actionBar;
    ImageButton close_action_bar_ibn, delete_btn, filter_btn;
    TextView selected_count_tv;

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
        setContentView(R.layout.activity_note_list);
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
        adapter = new NoteListAdapter(viewModel.isActionMode,
                viewModel.noteList,
                new ItemClickListener() {
                    @Override
                    public void itemClick(NoteModel model, int position) {
                        if(viewModel.isActionMode){
                            adapter.selectNote(position);
                            int count = viewModel.getSelectedList().size();
                            String countText = "";
                            if(count == 1){
                                countText = count + " item selected";
                            }else{
                                countText = count + " items selected";
                            }
                            selected_count_tv.setText(countText);
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
                },
                new ItemLongClickListener(){
                    @Override
                    public void onItemLongClick(View v, NoteModel model, int position) {
                        if(!viewModel.isActionMode){
                            setActionMode(true);
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

        selected_count_tv = findViewById(R.id.selected_count_tv);

        noteListRV = findViewById(R.id.noteListRV);
        noteListRV.setLayoutManager(new LinearLayoutManager(this));

        setUpRecyclerAdapter();

        fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(NoteListActivity.this, EditNoteActivity.class);
            someActivityResultLauncher.launch(intent);
        });
        setUpActionMode();

        filter_btn = findViewById(R.id.filter_button);
        filter_btn.setOnClickListener(view -> {
            FilterDialog dialog = new FilterDialog();
            dialog.show(getSupportFragmentManager(), "filter dialog");
        });

        delete_btn = findViewById(R.id.delete_ibn);
        delete_btn.setOnClickListener(view -> {
            int count = viewModel.getSelectedList().size();
            String msg = "";
            if(count == 1){
                msg = "Are you sure you want to delete " + count + " item?";
            }else{
                msg = "Are you sure you want to delete " + count + " items?";
            }
            DeleteDialog dialog = new DeleteDialog(msg);
            dialog.show(getSupportFragmentManager(), "delete dialog");
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    reload();
                }
            });


    @Override
    public void reload() {
        viewModel.refreshNotes();
        setUpRecyclerAdapter();
    }

    @Override
    public void deleteSelectedNotes() {
        final boolean result = viewModel.deleteSelectedNotes();
        if(result){
            Toast.makeText(NoteListActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(NoteListActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
        }
        viewModel.refreshNotes();
        setActionMode(false);
    }
}