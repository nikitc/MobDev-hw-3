package comnikitc.github.mobdev_hw_3;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {
    private final String serverUrl = "https://notesbackend-yufimtsev.rhcloud.com/";
    private final String DATA = "data";
    private final int userId = 17;
    private Retrofit retrofit;
    private NotesApi notesApi;

    public RetrofitHelper() {
        retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        notesApi = retrofit.create(NotesApi.class);
    }


    ArrayList<NoteModel> synchronizedFromServer() throws IOException, JSONException {
        String notesString = notesApi.getUserNotes(userId).execute().body();
        JSONObject jsonObject = new JSONObject(notesString);

        return JSONHelper.fromJson(jsonObject.getString("data"));
    }

    void deleteNoteServer(int serverNoteId) {
        notesApi.deleteNote(userId, serverNoteId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                //Toast.makeText(MainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void editNoteToServer(int serverNoteId, final String nameNote,
                          final String descriptionNote, final int colorNote) {
        notesApi.getUserNote(userId, serverNoteId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    NoteModel editNote = JSONHelper.fromJsonNote(jsonObject.getString("data"));
                    editNote.setName(nameNote);
                    editNote.setText(descriptionNote);
                    editNote.setColor(colorNote);
                    notesApi.editNote(userId,
                            editNote.getServerNoteId(), JSONHelper.toJsonNote(editNote))
                            .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call,
                                               @NonNull Response<String> response) {
                        }
                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                //Toast.makeText(MainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    int addNoteToServer(String nameNote, String descriptionNote, int colorNote, String imageUrl) throws IOException, JSONException {
        Date curDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.FORMAT_ISO);
        String date = simpleDateFormat.format(curDate);

        NoteModel note = new NoteModel(nameNote, descriptionNote, colorNote,
                imageUrl, date, date, date);

        String noteModel = null;
        try {
            noteModel = JSONHelper.toJsonNote(note);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String b = notesApi.addNote(userId, noteModel).execute().body();
        JSONObject jsonObject = new JSONObject(b);

        return Integer.parseInt(jsonObject.getString("data"));
    }
}
