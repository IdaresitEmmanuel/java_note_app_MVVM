package com.example.javanoteapp.view.notelistpage.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.example.javanoteapp.MyApplication;
import com.example.javanoteapp.R;

public class DeleteDialog extends AppCompatDialogFragment {
    String msg;
    DeleteDialogListener listener;
    public DeleteDialog(String msg){
        this.msg = msg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(msg)
                .setPositiveButton("yes", ((dialogInterface, i) -> {
                    listener.deleteSelectedNotes();
                    dialogInterface.dismiss();
                }))
                .setNegativeButton("no", (dialogInterface, i) -> dialogInterface.dismiss());

        return builder.create();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(MyApplication.getAppContext(), R.drawable.bg_dialog));
        super.setupDialog(dialog, style);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (DeleteDialogListener) context;
        } catch(ClassCastException e){
            throw new ClassCastException(e + " \n must implement DeleteDialogListener");
        }
    }

    public interface DeleteDialogListener {
        void deleteSelectedNotes();
    }
}
