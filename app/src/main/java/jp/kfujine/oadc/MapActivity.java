package jp.kfujine.oadc;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jp.kfujine.oadc.http.NodeApi;
import jp.kfujine.oadc.http.RequestCallback;
import jp.kfujine.oadc.http.RequestListener;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RequestListener {
    private final static String TAG = MapActivity.class.getSimpleName();
    private final static String BUNDLE_KEY_ID = "id";

    @Bind(R.id.imageView_moji)
    ImageView mImageViewMoji;

    @Bind(R.id.imageView_dance)
    ImageView mImageViewDance;

    private Socket mSocket;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private NodeApi mNodeService;

    private int mId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);



        Intent intent =  getIntent();
        mId= intent.getIntExtra(BUNDLE_KEY_ID, -1);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://36.55.240.249:8888")
                .build();
        mNodeService = adapter.create(NodeApi.class);

        Drawable drawable = mImageViewDance.getDrawable();
        if(drawable instanceof AnimationDrawable) {
            Log.d(TAG, "anim_drawable");
            if (((AnimationDrawable) drawable).isRunning()) {
                Log.d(TAG, "anim_stop");
                ((AnimationDrawable) drawable).stop();
            }
        }
    }

    public static Bundle createIntentBundle(int id) {
        Bundle b = new Bundle();
        b.putInt(BUNDLE_KEY_ID, id);

        return b;
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

    @OnClick(R.id.imageView_dance)
    public void onClick(View v) {
        setMoji();
        startDanceAnim();
    }

    private void setMoji() {
        mImageViewMoji.setImageDrawable(getResources().getDrawable(R.drawable.dejitikaiyassa));
    }

    private void startDanceAnim() {
        Drawable drawable = mImageViewDance.getDrawable();
        if(drawable instanceof AnimationDrawable) {
            Log.d(TAG ,"anim_drawable");
            if (((AnimationDrawable) drawable).isRunning()) {
                Log.d(TAG ,"anim_stop");
                ((AnimationDrawable) drawable).stop();
            } else {
                Log.d(TAG ,"anim_start");
                ((AnimationDrawable) drawable).start();
            }
        }
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
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double altitude = location.getAltitude();

        Log.d(TAG, "lat" + latitude + " lon" + longitude + " alt" + altitude);

        mNodeService.postLocation(mId, latitude, longitude, new RequestCallback<JSONObject>(this));

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
