package comnikitc.github.mobdev_hw_3;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

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

    private final int COUNT_TO_ADD = 100000;
    private final String FILENAME = "itemlist.ili";
    private ArrayList<NoteModel> listNotes;
    private DatabaseHelper dbHelper;
    private SettingsNotes settings = new SettingsNotes();
    private IOHandlerThread ioHandlerThread;
    private RetrofitHelper retrofitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitHelper = new RetrofitHelper();
        ioHandlerThread = new IOHandlerThread();
        ioHandlerThread.start();
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
        switch (requestCode) {
            case SORT_RULE:
                String order = data.getStringExtra(Constants.KEY_ORDER);
                String rule = data.getStringExtra(Constants.KEY_RULE);
                settings.setRule(rule);
                settings.setOrder(order);
                break;
            case FILTER_RULE:
                String filter = data.getStringExtra(Constants.KEY_FILTER);
                String dateFull = data.getStringExtra(Constants.KEY_DATE);
                settings.setFilter(filter);
                settings.setDateFull(dateFull);
                break;
        }
    }

    private void createListView() {
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        final ListView notesList = (ListView) findViewById(R.id.notesListView);
        final NotesAdapter notesAdapter = new NotesAdapter(this, dbHelper);
        Thread sortFilterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                notesAdapter.setListNotes(settings.setSettingListNotes(notesAdapter.getListNotes()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notesAdapter.notifyDataSetChanged();
                        listNotes = notesAdapter.getListNotes();
                    }
                });
            }
        });
        sortFilterThread.start();
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
                saveNotesToFile(FILENAME);
                return true;
            case R.id.sync:
                Thread syncThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<NoteModel> notes = retrofitHelper.synchronizedFromServer();
                            dbHelper.addNotesToDataBase(notes);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                syncThread.start();
                return true;
            case R.id.download:
                readNotesFromFile(FILENAME);
                return true;
            case R.id.add100k:
                Thread addNotesThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addOneHundredThousandNotes();
                    }
                });
                addNotesThread.start();
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

    private void addOneHundredThousandNotes() {
        final String nameForExample = "5";
        final String descrForExample = "5";
        final int colorForExample = -6230;
        final String urlForExample = "http://";
        for (int i = 0; i < COUNT_TO_ADD; i++) {
            Thread addNoteThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dbHelper.saveNewNote(nameForExample, descrForExample,
                            colorForExample, urlForExample, 1);
                }
            });
            addNoteThread.start();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createListView();
            }
        });

    }

    public void saveNotesToFile(final String filename)  {
        Runnable saveNotesRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String content = JSONHelper.toJson(listNotes);
                    File file = new File(getFilesDir(), filename);
                    Writer writer = new BufferedWriter(new FileWriter(file));
                    writer.write(content);
                    writer.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    R.string.notes_export, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        ioHandlerThread.getIoHandler().post(saveNotesRunnable);
    }

    public void readNotesFromFile(final String filename) {
        Runnable readNotesRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String result = "";
                    File file = new File(getFilesDir(), filename);
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                    br.close();
                    ArrayList<NoteModel> notes = JSONHelper.fromJson(result);
                    dbHelper.addNotesToDataBase(notes);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.notes_import,
                                    Toast.LENGTH_SHORT).show();
                            createListView();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        ioHandlerThread.getIoHandler().post(readNotesRunnable);
    }
}