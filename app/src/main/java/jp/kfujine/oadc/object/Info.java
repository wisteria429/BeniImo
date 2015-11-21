package jp.kfujine.oadc.object;

import java.util.Date;

/**
 * Created by fuji on 2015/11/22.
 */
public class Info {
    private String mImageName;
    private String mStoreName;
    private float mLat;
    private float mLon;
    private Date mDate;

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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
