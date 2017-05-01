package comnikitc.github.mobdev_hw_3;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
    static final private int ADD_NOTE = 2;
    static final private int READ_FILE = 3;
    static final private int WRITE_FILE = 4;

    private ArrayList<NoteModel> listNotes;
    private DatabaseHelper dbHelper;
    static final private String FILENAME = "itemlist.ili";
    private SettingsNotes settings = new SettingsNotes();

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
        if (data == null) {
            return;
        }

        if (requestCode == SORT_RULE) {
            String order = data.getStringExtra(Constants.KEY_ORDER);
            String rule = data.getStringExtra(Constants.KEY_RULE);
            settings.setRule(rule);
            settings.setOrder(order);
        }

        if (requestCode == FILTER_RULE) {
            String filter = data.getStringExtra(Constants.KEY_FILTER);
            String dateFull = data.getStringExtra(Constants.KEY_DATE);
            settings.setFilter(filter);
            settings.setDateFull(dateFull);
        }

        if (requestCode == READ_FILE) {
            String filePath = data.getData().getPath();
            readFile(filePath);
        }

        if (requestCode == WRITE_FILE) {
            String filePath = data.getData().getPath();
            saveNotesToFile(filePath);
        }
    }

    private void createListView() {
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        final ListView notesList = (ListView) findViewById(R.id.notesListView);

        final NotesAdapter notesAdapter = new NotesAdapter(this, dbHelper);
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
        createSearchView(menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void createSearchView(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<NoteModel> searchListNotes = new ArrayList<NoteModel>();
                for (NoteModel note: listNotes) {
                    if (isHasStr(note, newText)) {
                        searchListNotes.add(note);
                    }
                }
                final ListView notesList = (ListView) findViewById(R.id.notesListView);
                final NotesAdapter notesAdapter = new NotesAdapter(getApplicationContext(),
                        searchListNotes);
                notesAdapter.setListNotes(searchListNotes);
                notesList.setAdapter(notesAdapter);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            Boolean isHasStr(NoteModel note, String searchText) {
                return note.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                        note.getText().toLowerCase().contains(searchText.toLowerCase());
            }
        });

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
                getFile(WRITE_FILE);
                return true;
            case R.id.download:
                getFile(READ_FILE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNoteFab:
                Intent intent = new Intent(this, CreateNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE);
                break;
        }
    }

    public void saveNotesToFile(String filename)  {
        try {
            String content = JSONHelper.toJson(listNotes);
            File file = new File(filename);
            Log.d("file", file.toString());
            Writer writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();
            Toast.makeText(getApplicationContext(),
                    R.string.notes_export, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFile(int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, code);
    }

    public void readFile(String filename) {
        try {
            String result = "";
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();
            ArrayList<NoteModel> notes = JSONHelper.fromJson(result);
            dbHelper.addNotesToDataBase(notes);

            Toast.makeText(getApplicationContext(), R.string.notes_import,
                    Toast.LENGTH_SHORT).show();
            createListView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}