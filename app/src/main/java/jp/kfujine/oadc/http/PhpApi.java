package jp.kfujine.oadc.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import jp.kfujine.oadc.object.Info;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by fuji on 2015/11/22.
 */
public interface PhpApi {
    public static final String HOST = "http://175.184.17.82";
    @GET("/list.php")
    void getList(Callback<List<Info>> cb);

    @POST("/save.php")
    void  postSave(String res);

}
