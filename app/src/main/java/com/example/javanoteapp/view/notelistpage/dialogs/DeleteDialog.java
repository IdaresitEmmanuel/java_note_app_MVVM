package com.example.javanoteapp.view.notelistpage.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.javanoteapp.MyApplication;
import com.example.javanoteapp.R;

public class DeleteDialog extends AppCompatDialogFragment {
    String message;
    TextView msg_tv;
    DeleteDialogListener listener;
    public DeleteDialog(@NonNull String msg){
        message = msg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getAppContext());
        View view = getLayoutInflater().inflate(R.layout.fragment_delete_dialog, null);
        msg_tv = view.findViewById(R.id.msg_tv);
        msg_tv.setText(message);
        builder.setView(view)
                .setPositiveButton("yes", (dialogInterface, i) -> {
                    listener.deleteSelectedNotes();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("no", (dialogInterface, i) -> dialogInterface.dismiss());

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (DeleteDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(e + "\n must implement DeleteDialogListener");
        }
    }

    public interface DeleteDialogListener{
        void deleteSelectedNotes();
    }
}
