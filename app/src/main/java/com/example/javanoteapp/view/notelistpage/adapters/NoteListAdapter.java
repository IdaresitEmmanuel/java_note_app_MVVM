package com.example.javanoteapp.view.notelistpage.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javanoteapp.R;
import com.example.javanoteapp.data.models.NoteModel;
import com.example.javanoteapp.view.notelistpage.adapters.clicklisteners.ItemClickListener;
import com.example.javanoteapp.view.notelistpage.adapters.clicklisteners.ItemLongClickListener;

import java.util.ArrayList;

public class NoteListAdapter
        extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>{
    public final ArrayList<NoteModel> noteList;
    ItemClickListener clickListener;
    ItemLongClickListener longClickListener;
    private boolean isActionMode;
    public NoteListAdapter(boolean isActionMode, ArrayList<NoteModel> noteList, ItemClickListener clickListener, ItemLongClickListener longClickListener){
        this.isActionMode = isActionMode;
        this.noteList = noteList;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public void setActionMode(boolean actionMode){
        this.isActionMode = actionMode;
        refreshList();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshList(){
        synchronized (this){
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView noteTitle;
        private final TextView noteDate;
        private final TextView noteBody;
        private final CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDate = itemView.findViewById(R.id.note_date);
            noteBody = itemView.findViewById(R.id.note_body);
            checkBox = itemView.findViewById(R.id.note_checkbox);
        }
    }

    public void selectNote(int position){
        noteList.get(position)
                .setIsSelected(!noteList.get(position).getIsSelected());
        notifyItemChanged(position);
    }

    public void unselectAll(){
        for(int i = 0; i < noteList.size(); i++){
            noteList.get(i).setIsSelected(false);
        }
    }

    @NonNull
    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.ViewHolder holder, int position) {
        NoteModel model = noteList.get(position);
        holder.noteTitle.setText(model.getTitle());
        holder.noteDate.setText(model.getDate());
        holder.noteBody.setText(model.getBody());
        if(isActionMode){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.checkBox.setChecked(model.getIsSelected());
        holder.checkBox.setClickable(false);
        holder.itemView.setOnClickListener(view -> clickListener.itemClick(model, position));

        holder.itemView.setOnLongClickListener(view -> {
            longClickListener.onItemLongClick(view, model, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
