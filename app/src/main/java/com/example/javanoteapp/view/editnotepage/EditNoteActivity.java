package com.example.javanoteapp.view.editnotepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javanoteapp.R;
import com.example.javanoteapp.data.models.NoteModel;
import com.google.android.material.textfield.TextInputEditText;

public class EditNoteActivity extends AppCompatActivity implements LifecycleOwner {
    private EditNoteViewModel viewModel;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String body = intent.getStringExtra("body");
        NoteModel model = new NoteModel(id, title, date, body);
        setContentView(R.layout.activity_edit_note);

        viewModel = new ViewModelProvider(this).get(EditNoteViewModel.class);
        if(id != -1){
            viewModel.setNote(model);
        }

        TextView dateTv = findViewById(R.id.date_tv);
        EditText titleEt = findViewById(R.id.title_et);
        TextInputEditText bodyTet = findViewById(R.id.body_tet);
        ImageButton save_image_btn = findViewById(R.id.save_note);
        ImageButton options_btn = findViewById(R.id.note_options);
        ImageButton back_btn = findViewById(R.id.back_ib);

        dateTv.setText(viewModel.getDate());
        titleEt.setText(viewModel.getTitle());
        bodyTet.setText(viewModel.getNoteBody());

        back_btn.setOnClickListener(view -> onBackPressed());

        options_btn.setOnClickListener(this::showPopUp);

        save_image_btn.setOnClickListener(view -> {
            View v = getCurrentFocus();
            if(v instanceof EditText || v instanceof TextInputEditText){
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            String result = viewModel.saveNote();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            if(viewModel.getId() > -1){
                options_btn.setVisibility(View.VISIBLE);
                save_image_btn.setVisibility(View.GONE);
            }
        });

        titleEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    options_btn.setVisibility(View.GONE);
                    save_image_btn.setVisibility(View.VISIBLE);
                }

            }
        });
        bodyTet.setOnFocusChangeListener((view, b) -> {
            if(b){
                options_btn.setVisibility(View.GONE);
                save_image_btn.setVisibility(View.VISIBLE);
            }
        });

        titleEt.addTextChangedListener(getTitleTextWatcher());

        bodyTet.addTextChangedListener(getNoteTextWatcher());

    }

    private void showPopUp(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.edit_note_menu);
        popup.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId() == R.id.delete_note){
                viewModel.deleteFromDatabase();

                onBackPressed();
            }
            return true;
        });
        popup.show();
    }

    private TextWatcher getTitleTextWatcher(){
        return new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private TextWatcher getNoteTextWatcher(){
        return new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.setNoteBody(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}