package comnikitc.github.mobdev_hw_3;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ListView notesList = (ListView) findViewById(R.id.notesListView);
        NotesAdapter notesAdapter = new NotesAdapter(this, databaseHelper);
        notesList.setAdapter(notesAdapter);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addNoteFab);
        addButton.setOnClickListener(this);
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
