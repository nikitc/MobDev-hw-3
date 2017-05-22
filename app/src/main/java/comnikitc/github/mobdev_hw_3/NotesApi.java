package comnikitc.github.mobdev_hw_3;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

public interface NotesApi {

    @GET("/user/{userId}/notes")
    Call<String> getUserNotes(@Path("userId") int userId);

    @GET("/user/{userId}/note/{noteId}")
    Call<String> getUserNote(@Path("userId") int userId, @Path("noteId") int noteId);

    @POST("/user/{userId}/notes/")
    Call<String> addNote(@Path("userId") int userId, @Body final String note);

    @POST("/user/{userId}/note/{noteId}")
    Call<String> editNote(@Path("userId") int userId,
                          @Path("noteId") int noteId, @Body final String note);

    @DELETE("/user/{userId}/note/{noteId}")
    Call<String> deleteNote(@Path("userId") int userId, @Path("noteId") int noteId);
}
