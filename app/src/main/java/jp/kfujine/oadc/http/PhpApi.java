package jp.kfujine.oadc.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import jp.kfujine.oadc.object.Info;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by fuji on 2015/11/22.
 */
public interface PhpApi {
    public static final String HOST = "http://175.184.17.82";
    @GET("/list.php")
    void getList(Callback<List<Info>> cb);
    @Multipart
    @POST("/save.php")
    void  postSave(@Part("file_data1") TypedFile file, @Part("store_name") String storeName,@Part("lat") double lat, @Part("lon") double lon, Callback<String> cb);

}
