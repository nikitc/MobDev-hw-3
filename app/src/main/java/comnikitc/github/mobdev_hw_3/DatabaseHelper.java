package comnikitc.github.mobdev_hw_3;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class DatabaseHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "notes.db";
    static final int SCHEMA = 1;
    static final String TABLE = "notes";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_DESCR = "description";
    static final String COLUMN_COLOR = "color";
    static final String COLUMN_IMAGE_URL = "image_url";
    static final String COLUMN_DATE_CREATE = "date_create";
    static final String COLUMN_DATE_EDIT = "date_edit";
    static final String COLUMN_DATE_VIEW = "date_view";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCR + " TEXT, " +
                COLUMN_COLOR + " INTEGER, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_DATE_CREATE + " INTEGER, " +
                COLUMN_DATE_EDIT + " INTEGER, " +
                COLUMN_DATE_VIEW + " INTEGER" +");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    void addNotesToDataBase(ArrayList<NoteModel> notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (NoteModel note: notes) {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COLUMN_NAME, note.getName());
            cv.put(DatabaseHelper.COLUMN_DESCR, note.getText());
            cv.put(DatabaseHelper.COLUMN_COLOR, note.getColor());
            cv.put(DatabaseHelper.COLUMN_IMAGE_URL, note.getImageUrl());
            cv.put(DatabaseHelper.COLUMN_DATE_CREATE, note.getDateCreate());
            cv.put(DatabaseHelper.COLUMN_DATE_EDIT, note.getDateEdit());
            cv.put(DatabaseHelper.COLUMN_DATE_VIEW, note.getDateView());
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
    }

    ArrayList<NoteModel> getNotesFromDB() {
        ArrayList<NoteModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor notesCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        notesCursor.moveToFirst();
        if(notesCursor.moveToFirst()) {
            do {
                NoteModel note = new NoteModel(notesCursor.getInt(0), notesCursor.getString(1),
                        notesCursor.getString(2), notesCursor.getInt(3), notesCursor.getString(4),
                        notesCursor.getString(5), notesCursor.getString(6), notesCursor.getString(7));
                list.add(note);
            }
            while (notesCursor.moveToNext());
        }
        notesCursor.close();
        db.close();

        return list;
    }

    void deleteNoteFromDB(int idItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(idItem)});
        db.close();
    }

    void saveDateView(int idItem) {
        Date curDate = new Date();
        SimpleDateFormat dateFormatISO = new SimpleDateFormat(Constants.FORMAT_ISO);
        String date = dateFormatISO.format(curDate);

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put(DatabaseHelper.COLUMN_DATE_VIEW, date);
        db.update(DatabaseHelper.TABLE, cv,
                DatabaseHelper.COLUMN_ID + "=" + String.valueOf(idItem), null);
        db.close();
    }

    void saveChangeNote(String nameNote, String descriptionNote,
                        int colorNote, int idItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date curDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.FORMAT_ISO);
        String date = simpleDateFormat.format(curDate);

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameNote);
        cv.put(DatabaseHelper.COLUMN_DESCR, descriptionNote);
        cv.put(DatabaseHelper.COLUMN_COLOR, colorNote);
        cv.put(DatabaseHelper.COLUMN_DATE_EDIT, date);
        db.update(DatabaseHelper.TABLE, cv,
                DatabaseHelper.COLUMN_ID + "=" + String.valueOf(idItem), null);
        db.close();
    }

    synchronized void saveNewNote(String nameNote, String descriptionNote, int colorNote, String imageLink) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date curDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.FORMAT_ISO);
        String date = simpleDateFormat.format(curDate);

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameNote);
        cv.put(DatabaseHelper.COLUMN_DESCR, descriptionNote);
        cv.put(DatabaseHelper.COLUMN_COLOR, colorNote);
        cv.put(DatabaseHelper.COLUMN_IMAGE_URL, imageLink);
        cv.put(DatabaseHelper.COLUMN_DATE_CREATE, date);
        cv.put(DatabaseHelper.COLUMN_DATE_EDIT, date);
        cv.put(DatabaseHelper.COLUMN_DATE_VIEW, date);
        db.insert(DatabaseHelper.TABLE, null, cv);
        db.close();
    }

    Cursor getInfoNote(int idItem) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor notesCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(idItem)});
        notesCursor.moveToFirst();
        db.close();

        return notesCursor;
    }
}
