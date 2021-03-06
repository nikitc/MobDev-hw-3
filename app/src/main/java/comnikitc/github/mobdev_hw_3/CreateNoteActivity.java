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
    private DatabaseHelper dbHelper;
    private Boolean inEditMode = false;
    private int idItem = 0;
    private ImageView colorView;
    private EditText nameEditText;
    private EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        dbHelper = new DatabaseHelper(this);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        descriptionEditText = (EditText) findViewById(R.id.editTextDescr);
        colorView = (ImageView) findViewById(R.id.chooseColorEdit);
        Intent intent = getIntent();
        int id = intent.getIntExtra(Constants.KEY_ID, -1);
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
        dbHelper.saveDateView(idItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        int color = data.getIntExtra(Constants.KEY_COLOR, Color.RED);
        createChooseColorView(color);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(Constants.CHOOSE_COLOR, (int) colorView.getTag());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int color = savedInstanceState.getInt(Constants.CHOOSE_COLOR);
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
        Cursor notesCursor = dbHelper.getInfoNote(id);
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
            dbHelper.deleteNoteFromDB(idItem);
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
        if (inEditMode) {
            dbHelper.saveChangeNote(nameNote, descriptionNote, colorNote, idItem);
        } else {
            dbHelper.saveNewNote(nameNote, descriptionNote, colorNote);
        }
        Toast.makeText(getApplicationContext(), R.string.saveNote, Toast.LENGTH_SHORT).show();
    }
}