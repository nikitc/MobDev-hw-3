package comnikitc.github.mobdev_hw_3;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        final ListView notesList = (ListView) findViewById(R.id.notesListView);
        final NotesAdapter notesAdapter = new NotesAdapter(this, databaseHelper);
        notesList.setAdapter(notesAdapter);

        final FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addNoteFab);
        addButton.setOnClickListener(this);

        notesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isEndListView(scrollState, notesList, notesAdapter)) {
                    addButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                addButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean isEndListView(int scrollState, ListView notesList, NotesAdapter notesAdapter) {
        int maxCountOnActivity = 5;
        int count =  notesList.getLastVisiblePosition() - notesList.getHeaderViewsCount() -
                notesList.getFooterViewsCount();

        return  (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) &&
                (count >= (notesAdapter.getCount() - 1)) &&
                (notesAdapter.getCount() > maxCountOnActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNoteFab:
                Intent intent = new Intent(this, CreateNoteActivity.class);
                startActivity(intent);
                break;
        }
    }
}
