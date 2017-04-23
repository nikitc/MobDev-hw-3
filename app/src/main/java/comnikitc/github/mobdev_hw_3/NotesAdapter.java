package comnikitc.github.mobdev_hw_3;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotesAdapter extends BaseAdapter{

    private final Context context;
    private ArrayList<NoteModel> listNotes;
    private DatabaseHelper dbHelper;
    private LayoutInflater lInflater;

    public NotesAdapter(Context context, DatabaseHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
        listNotes = getNotesFromDB();
        lInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<NoteModel> getNotesFromDB() {
        ArrayList<NoteModel> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor notesCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        notesCursor.moveToFirst();

        if(notesCursor.moveToFirst()) {
            do {
                NoteModel note = new NoteModel(notesCursor.getString(1), notesCursor.getString(2),
                        notesCursor.getInt(3));
                list.add(note);
            }
            while (notesCursor.moveToNext());
        }

        notesCursor.close();
        db.close();

        return list;
    }

    @Override
    public int getCount() {
        return listNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return listNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String GetShortString(String text) {
        String shortLine = "";
        String[] arrayWords = text.split(" ");
        int maxLengthLine = 23;
        for (String arrayWord : arrayWords) {
            String currentLine = shortLine + " " + arrayWord;
            if (currentLine.length() < maxLengthLine) {
                shortLine += " " + arrayWord;
            }
        }

        if (text.length() > maxLengthLine) {
            if (arrayWords[0].length() > maxLengthLine) {
                shortLine = arrayWords[0].substring(maxLengthLine);
            }
            shortLine += "...";
        }

        return shortLine;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        NoteModel currentNote = listNotes.get(position);
        String shortName = GetShortString(currentNote.getName());
        String shortDescription = GetShortString(currentNote.getText());

        ((TextView) view.findViewById(R.id.name)).setText(shortName);
        ((TextView) view.findViewById(R.id.description)).setText(shortDescription);

        ImageView colorView = (ImageView) view.findViewById(R.id.color);
        Drawable drawable = context.getResources().getDrawable(R.drawable.circle);
        drawable.setColorFilter(currentNote.getColor(), PorterDuff.Mode.SRC);
        colorView.setBackground(drawable);

        Button editButton = (Button) view.findViewById(R.id.editButton);
        editButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateNoteActivity.class);
                intent.putExtra("id", position + 1);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
