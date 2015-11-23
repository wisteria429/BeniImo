package jp.kfujine.oadc;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.kfujine.oadc.http.PhpApi;
import jp.kfujine.oadc.http.RequestCallback;
import jp.kfujine.oadc.http.RequestListener;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.mime.TypedFile;

public class PreviewActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RequestListener {
    private static final String TAG = PreviewActivity.class.getSimpleName();
    private static final String BUNDLE_KEY_IMAGE_URL= "image_url";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private double mLat = 0;
    private double mLon = 0;

    private PhpApi mPhpService;

    private Uri mImageUri;

    @Bind(R.id.imageView_preview)
    ImageView mImageViewPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(BUNDLE_KEY_IMAGE_URL);

        asyncAdjustPhoto(uri);

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(PhpApi.HOST).build();
        mPhpService = adapter.create(PhpApi.class);



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

        String scheme = mImageUri.getScheme();

        String path = null;
        if ("file".equals(scheme)) {
            path = mImageUri.getPath();
        } else if("content".equals(scheme)) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(mImageUri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                path = cursor.getString(0);
                cursor.close();
            }
        }
        File file = null == path ? null : new File(path);

        TypedFile tfile = new TypedFile("image/jpeg", file);

        mPhpService.postSave(tfile, "ABC", mLat, mLon, new RequestCallback<>(new RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "postSave onSuccess");
                Intent intent = new Intent();
                intent.setClass(PreviewActivity.this, DanceActivity.class);
                Bundle b = DanceActivity.createIntentBundle(mLat, mLon);
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void onFailure(RetrofitError error) {
                Log.d(TAG, "postSave onFailure");
                Toast.makeText(getApplicationContext(), "登録に失敗しました.", Toast.LENGTH_SHORT).show();
            }
        }));



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


    public void asyncAdjustPhoto(Uri uri) {
        AsyncTask<Uri, Void, Uri> asyncTask = new AsyncTask<Uri, Void, Uri>() {
            @Override
            protected Uri doInBackground(Uri... uris) {
                //画像の調整(リサイズ・向き調整)
                int degree = PhotoUtil.getOrientation(uris[0]);
                Bitmap bmp = PhotoUtil.createBitmapFromUri(PreviewActivity.this, uris[0]);

                Matrix mat = new Matrix();
                mat.postRotate(degree);
                bmp = Bitmap.createBitmap(bmp, 0,0,bmp.getWidth(), bmp.getHeight(), mat, true);


                //保存先作成
                File imageFileFolder = new File(getCacheDir(),"beniimo");
                if( !imageFileFolder.exists() ){
                    imageFileFolder.mkdir();
                }

                FileOutputStream out = null;

                File imageFileName = new File(imageFileFolder, "avatar-" + System.currentTimeMillis() + ".jpg");
                try {
                    out = new FileOutputStream(imageFileName);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to convert image to JPEG", e);
                    return null;
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to close output stream", e);
                    }
                }


                return Uri.fromFile(imageFileName);
            }

            @Override
            protected void onPostExecute(Uri uri) {
                super.onPostExecute(uri);

                if (uri == null) {
                    return;
                }
                mImageUri = uri;
                mImageViewPreview.setImageURI(uri);
            }
        };
        asyncTask.execute(uri);
    }

}
