package comnikitc.github.mobdev_hw_3;



import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "notes.db";
    static final int SCHEMA = 1;
    static final String TABLE = "notes";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_DESCR = "description";
    static final String COLUMN_COLOR = "color";
    static final String COLUMN_DATE_CREATE = "date_create";
    static final String COLUMN_DATE_EDIT = "date_edit";
    static final String COLUMN_DATE_VIEW = "date_view";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCR + " TEXT, " +
                COLUMN_COLOR + " INTEGER," +
                COLUMN_DATE_CREATE + " INTEGER," +
                COLUMN_DATE_EDIT + " INTEGER," +
                COLUMN_DATE_VIEW + " INTEGER" +");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
