package com.example.javanoteapp.data.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.javanoteapp.MyApplication;
import com.example.javanoteapp.constants.NoteFilter;
import com.example.javanoteapp.data.models.NoteModel;

import java.util.ArrayList;
import java.util.Collections;

public class DBProvider extends SQLiteOpenHelper {

    private static final int VERSION = 3;
    private static final String DB_NAME = "noteDatabase";
    private static final String NOTE_TABLE = "noteTable";
    private static final String NOTE_ID = "id";
    private static final String NOTE_TITLE = "title";
    private static final String NOTE_DATE = "noteDate";
    private static final String NOTE_BODY = "noteBody";

    private static final String CREATE_NOTE_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)", NOTE_TABLE, NOTE_ID, NOTE_TITLE, NOTE_DATE, NOTE_BODY);

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
            Log.e("error getting notes", exception.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //drop table if already exist
        try{
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
        }catch (android.database.SQLException exception){
            Log.e("error getting notes", exception.toString());
        }
        //create new table
        onCreate(sqLiteDatabase);
    }

//    public NoteModel getNote(int noteId){
//        openDatabase();
//        NoteModel noteModel = null;
//        Cursor cur = null;
//        db.beginTransaction();
//        try{
//            cur = db.query(NOTE_TABLE, null, "where " + NOTE_ID + " = ?", new String[]{""+noteId+""}, null, null, null);
//            if(cur != null){
//                if(cur.moveToFirst()){
//                        int id = cur.getInt(cur.getColumnIndexOrThrow(NOTE_ID));
//                        String title = cur.getString(cur.getColumnIndexOrThrow(NOTE_TITLE));
//                        String date = cur.getString(cur.getColumnIndexOrThrow(NOTE_DATE));
//                        String body = cur.getString(cur.getColumnIndexOrThrow(NOTE_BODY));
//                        noteModel = new NoteModel(id,title, body, date);
//                }
//            }
//
//        }catch (Exception e){
//            Log.e("error getting notes", e.toString());
//        }finally {
//            assert cur != null;
//            cur.close();
//            db.endTransaction();
//            closeDatabase();
//        }
//        return noteModel;
//    }

    public ArrayList<NoteModel> getNotes(){
        openDatabase();
        ArrayList<NoteModel> noteModelList = new ArrayList<>();
        NoteFilter filter = AppPreferences.getInstance().getFilter();
        String orderString = "";
        Cursor cur = null;
        db.beginTransaction();
        if(filter.equals(NoteFilter.byDate)){
            orderString = NOTE_DATE + " DESC";
        }else{
            orderString = null;
        }
        try{
            cur = db.query(NOTE_TABLE, null, null, null, null, null, orderString);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        int id = cur.getInt(cur.getColumnIndexOrThrow(NOTE_ID));
                        String title = cur.getString(cur.getColumnIndexOrThrow(NOTE_TITLE));
                        String date = cur.getString(cur.getColumnIndexOrThrow(NOTE_DATE));
                        String body = cur.getString(cur.getColumnIndexOrThrow(NOTE_BODY));
                        noteModelList.add(new NoteModel(id,title, date, body));
                    }while(cur.moveToNext());
                }
            }
        }catch (Exception e){
            Log.e("error getting notes", e.toString());
        }finally{
            db.endTransaction();
            closeDatabase();
            assert cur != null;
            cur.close();
        }
        if(filter.equals(NoteFilter.alphabetical)){
            Collections.sort(noteModelList, (noteModel, t1) -> {
                return noteModel.getTitle().toLowerCase().compareTo(t1.getTitle().toLowerCase());
            });
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

        return result > -1;
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
            Log.e("error getting notes", e.toString());
        }finally{
            db.endTransaction();
            assert cur != null;
            cur.close();
            closeDatabase();
        }
        return id;
    }

    public boolean updateNote(NoteModel noteModel){
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TITLE, noteModel.getTitle());
        cv.put(NOTE_BODY, noteModel.getBody());
        int result = db.update(NOTE_TABLE, cv, NOTE_ID +"=?", new String[]{String.valueOf(noteModel.getId())});
        closeDatabase();
        return result > -1;
    }

    public boolean deleteNote(int id){
        openDatabase();
        int result = db.delete(NOTE_TABLE,NOTE_ID +"=?", new String[]{String.valueOf(id)});
        closeDatabase();
        return result > -1;
    }

//    public boolean deleteAllNotes(){
//        openDatabase();
//        int result = db.delete(NOTE_TABLE, null, null);
//        closeDatabase();
//        return result > -1;
//    }
}
