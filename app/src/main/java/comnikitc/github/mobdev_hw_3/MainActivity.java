package comnikitc.github.mobdev_hw_3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static final private int SORT_RULE = 0;
    static final private int FILTER_RULE = 1;
    private ArrayList<NoteModel> listNotes;
    static final private String FILENAME = "itemlist.ili";
    private SettingsNotes settings = new SettingsNotes();
    final private String KEY_ORDER = "order";
    final private String KEY_RULE = "rule";
    final private String KEY_FILTER = "filter";
    final private String KEY_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SORT_RULE && data != null) {
            String order = data.getStringExtra(KEY_ORDER);
            String rule = data.getStringExtra(KEY_RULE);
            settings.setRule(rule);
            settings.setOrder(order);
        }

        if (requestCode == FILTER_RULE && data != null) {
            String filter = data.getStringExtra(KEY_FILTER);
            String dateFull = data.getStringExtra(KEY_DATE);
            settings.setFilter(filter);
            settings.setDateFull(dateFull);
        }
    }

    private void createListView() {
        setContentView(R.layout.activity_main);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        final ListView notesList = (ListView) findViewById(R.id.notesListView);

        final NotesAdapter notesAdapter = new NotesAdapter(this, databaseHelper);
        notesAdapter.setListNotes(settings.setSettingListNotes(notesAdapter.getListNotes()));
        listNotes = notesAdapter.getListNotes();
        notesList.setAdapter(notesAdapter);

        final FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addNoteFab);
        addButton.setOnClickListener(this);

        notesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if(mLastFirstVisibleItem < firstVisibleItem) {
                    addButton.hide();
                }
                if(mLastFirstVisibleItem > firstVisibleItem) {
                    addButton.show();
                }

                mLastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                Intent intentSort = new Intent(this, SortActivity.class);
                startActivityForResult(intentSort, SORT_RULE);
                return true;
            case R.id.filter:
                Intent intentFilter = new Intent(this, FilterActivity.class);
                startActivityForResult(intentFilter, FILTER_RULE);
                return true;
            case R.id.upload:
                saveNotesToFile();
                return true;
            case R.id.download:
                readFile();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNoteFab:
                Intent intent = new Intent(this, CreateNoteActivity.class);
                startActivityForResult(intent, 2);
                break;
        }
    }

    public void saveNotesToFile()  {
        try {
            String content = JSONHelper.toJson(listNotes); 
            File file = new File(getFilesDir() + "/" + FILENAME);
            Writer writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();
            Toast.makeText(getApplicationContext(),
                    R.string.notes_export, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        try {
            String result = "";
            File file = new File(getFilesDir(), FILENAME);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();

            ArrayList<NoteModel> notes = JSONHelper.fromJson(result);

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            for (int i = 0; i < notes.size(); i++) {
                NoteModel currentNote = notes.get(i);
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.COLUMN_NAME, currentNote.getName());
                cv.put(DatabaseHelper.COLUMN_DESCR, currentNote.getText());
                cv.put(DatabaseHelper.COLUMN_COLOR, currentNote.getColor());
                cv.put(DatabaseHelper.COLUMN_DATE_CREATE, currentNote.getDateCreate());
                cv.put(DatabaseHelper.COLUMN_DATE_EDIT, currentNote.getDateEdit());
                cv.put(DatabaseHelper.COLUMN_DATE_VIEW, currentNote.getDateView());
                db.insert(DatabaseHelper.TABLE, null, cv);
            }

            Toast.makeText(getApplicationContext(), R.string.notes_import,
                    Toast.LENGTH_SHORT).show();
            createListView();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}