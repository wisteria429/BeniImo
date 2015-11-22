package jp.kfujine.oadc.http;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by fuji on 2015/11/22.
 */
public interface NodeApi {
    @GET("/post.json")
    void postLocation(@Query("id") int id , @Query("lat") double lat, @Query("lon") double lon, Callback<JSONObject> cb);
}
