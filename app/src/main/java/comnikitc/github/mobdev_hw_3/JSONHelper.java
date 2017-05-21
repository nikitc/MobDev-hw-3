package comnikitc.github.mobdev_hw_3;

import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

class JSONHelper {

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String COLOR = "color";
    private static final String IMAGE_URL = "imageUrl";
    private static final String CREATED = "created";
    private static final String EDITED = "edited";
    private static final String VIEWED = "viewed";
    private static final String SERVER_ID = "id";

    static String toJsonNote(NoteModel note) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TITLE, note.getName());
        jsonObject.put(DESCRIPTION, note.getText());
        jsonObject.put(COLOR, note.getColor());
        jsonObject.put(IMAGE_URL, note.getImageUrl());
        jsonObject.put(CREATED, note.getDateCreate());
        jsonObject.put(EDITED, note.getDateEdit());
        jsonObject.put(VIEWED, note.getDateView());

        return jsonObject.toString();
    }

    static NoteModel fromJsonNote(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String title = jsonObject.getString(TITLE);
        String description = jsonObject.getString(DESCRIPTION);
        Integer color = jsonObject.getInt(COLOR);
        String imageUrl = jsonObject.getString(IMAGE_URL);
        String dateCreate = jsonObject.getString(CREATED);
        String dateEdit = jsonObject.getString(EDITED);
        String dateView = jsonObject.getString(VIEWED);
        int serverId = jsonObject.getInt(SERVER_ID);
        NoteModel note = new NoteModel(title, description, color, imageUrl,
                dateCreate, dateEdit, dateView);
        note.setServerNoteId(serverId);

        return note;
    }

    static String toJson(ArrayList<NoteModel> listNotes) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (NoteModel note: listNotes) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(TITLE, note.getName());
                jsonObject.put(DESCRIPTION, note.getText());
                jsonObject.put(COLOR, note.getColor());
                jsonObject.put(IMAGE_URL, note.getImageUrl());
                jsonObject.put(CREATED, note.getDateCreate());
                jsonObject.put(EDITED, note.getDateEdit());
                jsonObject.put(VIEWED, note.getDateView());
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray.toString();
    }

    static ArrayList<NoteModel> fromJson(String json)  {
        ArrayList<NoteModel> listNotes = new ArrayList<NoteModel>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString(TITLE);
                String description = jsonObject.getString(DESCRIPTION);
                Integer color = jsonObject.getInt(COLOR);
                String imageUrl = jsonObject.getString(IMAGE_URL);
                String dateCreate = jsonObject.getString(CREATED);
                String dateEdit = jsonObject.getString(EDITED);
                String dateView = jsonObject.getString(VIEWED);
                int serverId = jsonObject.getInt(SERVER_ID);

                NoteModel note = new NoteModel(i, title, description, color, imageUrl,
                        dateCreate, dateEdit, dateView, serverId);
                listNotes.add(note);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listNotes;
    }
}
