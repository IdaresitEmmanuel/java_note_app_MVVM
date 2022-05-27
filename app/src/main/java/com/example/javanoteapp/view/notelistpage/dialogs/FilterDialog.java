package com.example.javanoteapp.view.notelistpage.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.javanoteapp.R;
import com.example.javanoteapp.constants.NoteFilter;
import com.example.javanoteapp.data.repository.AppPreferences;

public class FilterDialog extends AppCompatDialogFragment{
    RadioButton none, alpha, bDate;
    FilterDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.fragment_filter_dialog, null);

        none = view.findViewById(R.id.filter_none);
        alpha = view.findViewById(R.id.filter_alphabetical);
        bDate = view.findViewById(R.id.filter_by_date);

        none.setOnClickListener(v ->checkFilter(NoteFilter.none));
        alpha.setOnClickListener(v -> checkFilter(NoteFilter.alphabetical));
        bDate.setOnClickListener(v -> checkFilter(NoteFilter.byDate));
        setChecked();
        builder.setView(view);
        return builder.create();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.setupDialog(dialog, style);
    }

    private void setChecked(){
        NoteFilter filter = AppPreferences.getInstance().getFilter();
        switch (filter){
            case none:
                none.setChecked(true);
                break;
            case alphabetical:
                alpha.setChecked(true);
                break;
            case byDate:
                bDate.setChecked(true);
                break;
        }
    }

    public void checkFilter(NoteFilter filter){
         AppPreferences.getInstance().setFilter(filter);
         listener.reload();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (FilterDialogListener) context;
        }catch(Exception e){
            Log.d("FilterDialogFragment:", e.toString());
            throw new ClassCastException(context
                    + " must implement FilterDialogListener");
        }
    }

    public interface FilterDialogListener{
        void reload();
    }
}
