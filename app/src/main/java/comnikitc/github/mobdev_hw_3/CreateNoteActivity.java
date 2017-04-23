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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import comnikitc.github.mobdev_hw_3.ColorPicker.ColorActivity;

public class CreateNoteActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private Boolean inEditMode = false;
    private int idItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if (id != -1) {
            inEditMode = true;
            idItem = id;
            SetOptions(id);

            return;
        }

        int color = Color.RED;
        ImageView colorView = (ImageView) findViewById(R.id.chooseColorEdit);
        Drawable drawable = getResources().getDrawable(R.drawable.circle);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC);
        colorView.setBackground(drawable);
        colorView.setTag(color);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        int color = data.getIntExtra("color", Color.RED);
        ImageView colorView = (ImageView) findViewById(R.id.chooseColorEdit);
        Drawable drawable = getResources().getDrawable(R.drawable.circle);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC);
        colorView.setBackground(drawable);
        colorView.setTag(color);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        ImageView colorView = (ImageView) findViewById(R.id.chooseColorEdit);

        savedInstanceState.putInt("chooseColor", (int) colorView.getTag());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int color = savedInstanceState.getInt("chooseColor");
        ImageView colorView = (ImageView) findViewById(R.id.chooseColorEdit);
        Drawable drawable = getResources().getDrawable(R.drawable.circle);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC);
        colorView.setBackground(drawable);
        colorView.setTag(color);
    }


    public void SetOptions(int id) {
        db = dbHelper.getReadableDatabase();

        Cursor notesCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        notesCursor.moveToFirst();

        EditText nameEditText = (EditText) findViewById(R.id.editTextName);
        EditText descriptionEditText = (EditText) findViewById(R.id.editTextDescr);
        nameEditText.setText(notesCursor.getString(1));
        descriptionEditText.setText(notesCursor.getString(2));

        int color = notesCursor.getInt(3);

        ImageView colorView = (ImageView) findViewById(R.id.chooseColorEdit);
        Drawable drawable = getResources().getDrawable(R.drawable.circle);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC);
        colorView.setBackground(drawable);
        colorView.setTag(color);
        notesCursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteNote:
                DeleteNote();
                return true;
            case R.id.save:
                SaveData();
                return true;
            case R.id.chooseColor:
                GoChooseColor();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void DeleteNote() {
        if (inEditMode) {
            db = dbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(idItem)});
        }

        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.deleteChooseNote, Toast.LENGTH_SHORT);
        toast.show();

        GoHome();
    }

    public void GoChooseColor() {
        Intent intent = new Intent(this, ColorActivity.class);
        startActivityForResult(intent, 1);
    }

    public void GoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void SaveData() {
        EditText nameEditText = (EditText) findViewById(R.id.editTextName);
        EditText descriptionEditText = (EditText) findViewById(R.id.editTextDescr);
        String nameNote = nameEditText.getText().toString();
        String descriptionNote = descriptionEditText.getText().toString();

        if (nameNote.isEmpty() || descriptionNote.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.fillNote, Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        ImageView imageViewNote = (ImageView) findViewById(R.id.chooseColorEdit);
        int colorNote = (Integer) imageViewNote.getTag();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameNote);
        cv.put(DatabaseHelper.COLUMN_DESCR, descriptionNote);
        cv.put(DatabaseHelper.COLUMN_COLOR, colorNote);

        db = dbHelper.getWritableDatabase();
        if (inEditMode) {
            db.update(DatabaseHelper.TABLE, cv,
                    DatabaseHelper.COLUMN_ID + "=" + String.valueOf(idItem), null);
        } else {
            db.insert(DatabaseHelper.TABLE, null, cv);
        }

        db.close();

        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.saveNote, Toast.LENGTH_SHORT);
        toast.show();
        GoHome();
    }
}