package comnikitc.github.mobdev_hw_3;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

import comnikitc.github.mobdev_hw_3.ColorPicker.ColorActivity;

public class CreateNoteActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private Boolean inEditMode = false;
    private int idItem = 0;
    private int serverId;
    private ImageView colorView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText imageUrlEditText;
    private RetrofitHelper retrofitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        dbHelper = new DatabaseHelper(this);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        descriptionEditText = (EditText) findViewById(R.id.editTextDescr);
        imageUrlEditText = (EditText) findViewById(R.id.imageLink);
        colorView = (ImageView) findViewById(R.id.chooseColorEdit);
        Intent intent = getIntent();
        int id = intent.getIntExtra(Constants.KEY_ID, -1);
        retrofitHelper = new RetrofitHelper();
        if (id != -1) {
            inEditMode = true;
            serverId = intent.getIntExtra(Constants.KEY_SERVER_ID, -1);
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
        String url = notesCursor.getString(4);
        imageUrlEditText.setText(url);
        setImageFromUrl(url);
        createChooseColorView(color);
        notesCursor.close();
    }

    private void setImageFromUrl(String url) {
        Picasso.with(this)
                .load(url)
                .into((ImageView) findViewById(R.id.imageFromLink));
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
                Thread deleteNoteThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deleteNote();
                        retrofitHelper.deleteNoteServer(serverId);
                    }
                });
                deleteNoteThread.start();
                finish();
                return true;
            case R.id.save:
                Thread saveNoteThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            saveData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                saveNoteThread.start();
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),
                        R.string.deleteChooseNote, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goChooseColor() {
        Intent intent = new Intent(this, ColorActivity.class);
        startActivityForResult(intent, 1);
    }

    private void saveData() throws IOException, JSONException {
        String nameNote = nameEditText.getText().toString();
        String descriptionNote = descriptionEditText.getText().toString();
        String imageUrl = imageUrlEditText.getText().toString();
        if (nameNote.isEmpty() || descriptionNote.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            R.string.fillNote, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        int colorNote = (Integer) colorView.getTag();
        if (inEditMode) {
            retrofitHelper.editNoteToServer(serverId, nameNote, descriptionNote, colorNote);
            dbHelper.saveChangeNote(nameNote, descriptionNote, colorNote, idItem);
        } else {
            int serverNoteId =
                    retrofitHelper.addNoteToServer(nameNote, descriptionNote, colorNote, imageUrl);
            dbHelper.saveNewNote(nameNote, descriptionNote, colorNote, imageUrl, serverNoteId);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), R.string.saveNote, Toast.LENGTH_SHORT).show();
            }
        });
    }
}