package jp.kfujine.oadc.object;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by fuji on 2015/11/22.
 */
public class Info {
    @SerializedName("id")
    private int mId;
    @SerializedName("image")
    private String mImageName;
    @SerializedName("store")
    private String mStoreName;
    @SerializedName("lat")
    private float mLat;
    @SerializedName("lon")
    private float mLon;
    @SerializedName("time")
    private String mDate;

    public Info(int id, String imageName, String storeName, float lat, float lon, String date) {
        mId = id;
        mImageName = imageName;
        mStoreName = storeName;
        mLat = lat;
        mLon = lon;
        mDate = date;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String imageName) {
        mImageName = imageName;
    }

    public String getStoreName() {
        return mStoreName;
    }

    public void setStoreName(String storeName) {
        mStoreName = storeName;
    }

    public float getLat() {
        return mLat;
    }

    public void setLat(float lat) {
        mLat = lat;
    }

    public float getLon() {
        return mLon;
    }

    public void setLon(float lon) {
        mLon = lon;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }
}
