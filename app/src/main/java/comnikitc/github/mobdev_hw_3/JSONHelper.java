package comnikitc.github.mobdev_hw_3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

class JSONHelper {


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

                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                Integer color = jsonObject.getInt("color");
                String dateCreate = jsonObject.getString("created");
                String dateEdit = jsonObject.getString("edited");
                String dateView = jsonObject.getString("viewed");

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
