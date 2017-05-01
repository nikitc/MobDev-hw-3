package comnikitc.github.mobdev_hw_3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import comnikitc.github.mobdev_hw_3.ColorPicker.ColorActivity;

public class CreateNoteActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private Boolean inEditMode = false;
    private int idItem = 0;
    private ImageView colorView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private final String CHOOSE_COLOR = "chooseColor";
    private final String KEY_ID = "id";
    private final String KEY_COLOR = "color";
    private final String FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        dbHelper = new DatabaseHelper(this);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        descriptionEditText = (EditText) findViewById(R.id.editTextDescr);
        colorView = (ImageView) findViewById(R.id.chooseColorEdit);
        Intent intent = getIntent();
        int id = intent.getIntExtra(KEY_ID, -1);
        if (id != -1) {
            inEditMode = true;
            idItem = id;
            setOptions(id);
            saveDateView();

            return;
        }

        int color = Color.RED;
        createChooseColorView(color);

    }

    private void saveDateView() {
        if (!inEditMode) {
            return;
        }

        ContentValues cv = new ContentValues();
        db = dbHelper.getWritableDatabase();

        Date curDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_ISO);
        String date = simpleDateFormat.format(curDate);

        cv.put(DatabaseHelper.COLUMN_DATE_VIEW, date);
        db.update(DatabaseHelper.TABLE, cv,
                DatabaseHelper.COLUMN_ID + "=" + String.valueOf(idItem), null);

        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        int color = data.getIntExtra(KEY_COLOR, Color.RED);
        createChooseColorView(color);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(CHOOSE_COLOR, (int) colorView.getTag());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int color = savedInstanceState.getInt(CHOOSE_COLOR);
        createChooseColorView(color);
    }

    private void createChooseColorView(int color) {
        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goChooseColor();
            }
        });
        Drawable drawable = getResources().getDrawable(R.drawable.circle);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC);
        colorView.setBackground(drawable);
        colorView.setTag(color);
    }

    private void setOptions(int id) {
        db = dbHelper.getReadableDatabase();

        Cursor notesCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        notesCursor.moveToFirst();

        nameEditText.setText(notesCursor.getString(1));
        descriptionEditText.setText(notesCursor.getString(2));

        int color = notesCursor.getInt(3);
        createChooseColorView(color);
        notesCursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteNote:
                deleteNote();
                finish();
                return true;
            case R.id.save:
                saveData();
                finish();
                return true;
            case R.id.chooseColor:
                goChooseColor();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteNote() {
        if (inEditMode) {
            db = dbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(idItem)});
        }

        Toast.makeText(getApplicationContext(), R.string.deleteChooseNote, Toast.LENGTH_SHORT).show();
    }

    private void goChooseColor() {
        Intent intent = new Intent(this, ColorActivity.class);
        startActivityForResult(intent, 1);
    }


    private void saveData() {
        String nameNote = nameEditText.getText().toString();
        String descriptionNote = descriptionEditText.getText().toString();

        if (nameNote.isEmpty() || descriptionNote.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.fillNote, Toast.LENGTH_SHORT).show();

            return;
        }
        int colorNote = (Integer) colorView.getTag();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameNote);
        cv.put(DatabaseHelper.COLUMN_DESCR, descriptionNote);
        cv.put(DatabaseHelper.COLUMN_COLOR, colorNote);

        db = dbHelper.getWritableDatabase();
        Date curDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_ISO);

        if (inEditMode) {
            String date = simpleDateFormat.format(curDate);

            cv.put(DatabaseHelper.COLUMN_DATE_EDIT, date);
            db.update(DatabaseHelper.TABLE, cv,
                    DatabaseHelper.COLUMN_ID + "=" + String.valueOf(idItem), null);
        } else {
            String date = simpleDateFormat.format(curDate);
            cv.put(DatabaseHelper.COLUMN_DATE_CREATE, date);
            cv.put(DatabaseHelper.COLUMN_DATE_EDIT, date);
            cv.put(DatabaseHelper.COLUMN_DATE_VIEW, date);
            db.insert(DatabaseHelper.TABLE, null, cv);
        }

        db.close();
        Toast.makeText(getApplicationContext(), R.string.saveNote, Toast.LENGTH_SHORT).show();
    }
}