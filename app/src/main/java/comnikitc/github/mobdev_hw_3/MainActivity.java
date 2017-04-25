package comnikitc.github.mobdev_hw_3;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

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

    private void createListView() {
        setContentView(R.layout.activity_main);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        final ListView notesList = (ListView) findViewById(R.id.notesListView);
        final NotesAdapter notesAdapter = new NotesAdapter(this, databaseHelper);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNoteFab:
                Intent intent = new Intent(this, CreateNoteActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }
}
