package com.example.javanoteapp.data.repository;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.javanoteapp.MyApplication;
import com.example.javanoteapp.data.models.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class DBProvider extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "noteDatabase";
    private static final String NOTE_TABLE = "noteTable";
    private static final String NOTE_ID = "id";
    private static final String NOTE_TITLE = "title";
    private static final String NOTE_DATE = "noteDate";
    private static final String NOTE_BODY = "noteBody";

    private static final String CREATE_NOTE_TABLE = "CREATE TABLE " + NOTE_TABLE +"(" + NOTE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT, "
            + NOTE_DATE + " TEXT, " + NOTE_BODY + " TEXT)";

    private SQLiteDatabase db;

    private static DBProvider instance;

    private DBProvider() {
        super(MyApplication.getAppContext(), DB_NAME, null, VERSION);
    }

    public static DBProvider getInstance(){
        if(instance == null){
            instance = new DBProvider();
        }
        return instance;
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void closeDatabase(){db.close();}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            sqLiteDatabase.execSQL(CREATE_NOTE_TABLE);
        }catch (android.database.SQLException exception){
            System.out.println(exception);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //drop table if already exist
        try{
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
        }catch (android.database.SQLException exception){
            System.out.println(exception);
        }
        //create new table
        onCreate(sqLiteDatabase);
    }

    public NoteModel getNote(int noteId){
        openDatabase();
        NoteModel noteModel = null;
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(NOTE_TABLE, null, "where " + NOTE_ID + " = ?", new String[]{""+noteId+""}, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                        int id = cur.getInt(cur.getColumnIndexOrThrow(NOTE_ID));
                        String title = cur.getString(cur.getColumnIndexOrThrow(NOTE_TITLE));
                        String date = cur.getString(cur.getColumnIndexOrThrow(NOTE_DATE));
                        String body = cur.getString(cur.getColumnIndexOrThrow(NOTE_BODY));
                        noteModel = new NoteModel(id,title, body, date);
                }
            }

        }catch (Exception e){
            System.out.println(e);
        }finally {
            cur.close();
            db.endTransaction();
            closeDatabase();
        }
        return noteModel;
    }

    public List<NoteModel> getNotes(){
        openDatabase();
        List<NoteModel> noteModelList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(NOTE_TABLE, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        int id = cur.getInt(cur.getColumnIndexOrThrow(NOTE_ID));
                        String title = cur.getString(cur.getColumnIndexOrThrow(NOTE_TITLE));
                        String date = cur.getString(cur.getColumnIndexOrThrow(NOTE_DATE));
                        String body = cur.getString(cur.getColumnIndexOrThrow(NOTE_BODY));
                        noteModelList.add(new NoteModel(id,title, body, date));
                    }while(cur.moveToNext());
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }finally{
            db.endTransaction();
            cur.close();
            closeDatabase();
        }
        return noteModelList;
    }

    public boolean addNote(NoteModel noteModel){
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TITLE, noteModel.getTitle());
        cv.put(NOTE_DATE, noteModel.getDate());
        cv.put(NOTE_BODY, noteModel.getBody());

        long result = db.insert(NOTE_TABLE, null, cv);

        if(result > -1) { return true; }else{ return false;}
    }

    public int getLastNoteId(){
        openDatabase();
        int id = -1;
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(NOTE_TABLE, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToLast()){
                    id = cur.getInt(cur.getColumnIndexOrThrow(NOTE_ID));
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }finally{
            db.endTransaction();
            cur.close();
            closeDatabase();
        }
        return id;
    }

    public boolean updateNote(int id){
        openDatabase();
        int result = db.update(NOTE_TABLE, null, NOTE_ID +"=?", new String[]{String.valueOf(id)});
        closeDatabase();
        if(result > -1) { return true; }else{ return false;}
    }

    public boolean deleteNote(int id){
        openDatabase();
        int result = db.delete(NOTE_TABLE,NOTE_ID +"=?", new String[]{String.valueOf(id)});
        closeDatabase();
        if(result > -1) { return true; }else{ return false;}
    }

    public boolean deleteAllNotes(){
        openDatabase();
        int result = db.delete(NOTE_TABLE, null, null);
        closeDatabase();
        if(result > -1) { return true; }else{ return false;}
    }
}
