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
    private static final String CREATED = "created";
    private static final String EDITED = "edited";
    private static final String VIEWED = "viewed";

    static String toJson(ArrayList<NoteModel> listNotes) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (NoteModel note: listNotes) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", note.getName());
                jsonObject.put("description", note.getText());
                jsonObject.put("color", note.getColor());
                jsonObject.put("created", note.getDateCreate());
                jsonObject.put("edited", note.getDateEdit());
                jsonObject.put("viewed", note.getDateView());

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
                String dateCreate = jsonObject.getString(CREATED);
                String dateEdit = jsonObject.getString(EDITED);
                String dateView = jsonObject.getString(VIEWED);

                NoteModel note = new NoteModel(i, title, description, color,
                        dateCreate, dateEdit, dateView);
                listNotes.add(note);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listNotes;
    }
}
