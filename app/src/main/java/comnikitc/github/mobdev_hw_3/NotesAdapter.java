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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

class NotesAdapter extends BaseAdapter{
    private final Context context;
    private ArrayList<NoteModel> listNotes;
    private LayoutInflater lInflater;
    final private String KEY_ID = "id";

    NotesAdapter(Context context, DatabaseHelper dbHelper) {
        this.context = context;
        listNotes = dbHelper.getNotesFromDB();
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    NotesAdapter(Context context, ArrayList<NoteModel> listNotes) {
        this.context = context;
        this.listNotes = listNotes;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    ArrayList<NoteModel> getListNotes() {
        return this.listNotes;
    }

    void setListNotes(ArrayList<NoteModel> listNotes) {
        this.listNotes = listNotes;
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


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        final NoteModel currentNote = listNotes.get(position);

        ((TextView) view.findViewById(R.id.name)).setText(currentNote.getName());
        ((TextView) view.findViewById(R.id.description)).setText(currentNote.getText());

        ImageView colorView = (ImageView) view.findViewById(R.id.color);
        Drawable drawable = context.getResources().getDrawable(R.drawable.circle);
        drawable.setColorFilter(currentNote.getColor(), PorterDuff.Mode.SRC);
        colorView.setBackground(drawable);

        LinearLayout item = (LinearLayout) view.findViewById(R.id.itemLayout);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateNoteActivity.class);
                intent.putExtra(KEY_ID, currentNote.getId());
                intent.putExtra(Constants.KEY_SERVER_ID, currentNote.getServerNoteId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
