package jp.kfujine.oadc;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.kfujine.oadc.http.RequestCallback;
import jp.kfujine.oadc.http.RequestListener;
import retrofit.RetrofitError;

public class PreviewActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RequestListener {
    private static final String TAG = PreviewActivity.class.getSimpleName();
    private static final String BUNDLE_KEY_IMAGE_URL= "image_url";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private double mLat = 0;
    private double mLon = 0;


    @Bind(R.id.imageView_preview)
    ImageView mImageViewPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(BUNDLE_KEY_IMAGE_URL);

        mImageViewPreview.setImageURI(uri);



    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        if (isLocationEnabled()) {
            connectGooglePlayServices();
        }



    }


    @Override
    public void onStop() {
        super.onStop();
        disConnectGooglePlayServices();
    }

    public static Bundle createIntentBundle(Uri imgUrl) {
        Bundle b = new Bundle();
        b.putParcelable(BUNDLE_KEY_IMAGE_URL, imgUrl);

        return b;
    }

    @OnClick(R.id.button_send)
    public void onClickSend(View v) {
        if (mLon == 0 && mLat == 0) {
            return;
        }



        Intent intent = new Intent();
        intent.setClass(this, DanceActivity.class);
        Bundle b = DanceActivity.createIntentBundle(mLat, mLon);
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * 現在地情報が取得可能な場合はtrue, 取得できない場合はfalse
     * @return
     */
    protected boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * GooglePlayServicesに接続する
     */
    protected void connectGooglePlayServices() {

        mLocationRequest = LocationRequest.create();
        // 10秒おきに位置情報を取得する
        mLocationRequest.setInterval(5000);
        // 精度優先
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // 最短で5秒おきに位置情報を取得する
        // mLocationRequest.setFastestInterval(5000);

//        mLocationClient = new LocationClient(getActivity().getApplicationContext(), connectionCallbacks, onConnectionFailedListener);
//        mLocationClient.connect();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (mGoogleApiClient != null) {
            // GoogleApiClient start
            mGoogleApiClient.connect();
        }

    }

    /**
     * GooglePlayServicesを切断する
     */
    protected void disConnectGooglePlayServices() {

        if (mGoogleApiClient.isConnected()) {
            // 位置情報の取得を停止
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            //mGoogleApiClient.removeLocationUpdates(locationListener);
            mGoogleApiClient.disconnect();
        }

    }


    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLat = location.getLatitude();
        mLon = location.getLongitude();

        Log.d(TAG, "mLat:" + mLat + " mLon:" + mLon);
        disConnectGooglePlayServices();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}


    /*-----
       APIリクエストリスナー
     */
    @Override
    public void onSuccess(Object response) {
        Log.d(TAG, "success");
    }

    @Override
    public void onFailure(RetrofitError error) {
        Log.d(TAG, "failure");
    }

}
