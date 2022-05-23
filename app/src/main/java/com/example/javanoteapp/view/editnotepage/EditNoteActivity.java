package com.example.javanoteapp.view.editnotepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javanoteapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity implements LifecycleOwner {
    private TextView dateTv;
    private EditNoteViewModel viewModel;
    private ImageButton save_image_btn, options_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        viewModel = new ViewModelProvider(this).get(EditNoteViewModel.class);


        dateTv = findViewById(R.id.date_tv);
        save_image_btn = findViewById(R.id.save_note);
        options_btn = findViewById(R.id.note_options);

        dateTv.setText(viewModel.note.getDate());

        save_image_btn.setOnClickListener(view -> {
            boolean result = viewModel.saveNote();
            if(result){
                Toast.makeText(this, "Note has been added to database", Toast.LENGTH_SHORT);
            }else{
                Toast.makeText(this, "A problem has occurred", Toast.LENGTH_SHORT);
            }
        });
    }
}