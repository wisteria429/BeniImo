package jp.kfujine.oadc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class DanceActivity extends Activity{
    private static final String TAG = DanceActivity.class.getSimpleName();
    private static final String BUNDLE_KEY_LAT = "lat";
    private static final String BUNDLE_KEY_LON = "lon";
    private MediaPlayer mPlayer;
    private AudioManager mManager;
    private Socket mSocket;

    private double mVolPer = 1.0;

    private double mPartnerLat =0;
    private double mPartnerLon =0;

    @Bind(R.id.imageView_uri)
    ImageView mImageViewUri;

    @Bind(R.id.imageView_dance)
    ImageView mImageViewDance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dooi_2);
        mPlayer.setLooping(true);
        mManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        Intent intent = getIntent();
        mPartnerLat = intent.getDoubleExtra(BUNDLE_KEY_LAT, 0);
        mPartnerLon= intent.getDoubleExtra(BUNDLE_KEY_LON, 0);


        try {
            mSocket = IO.socket("http://36.55.240.249:8888");
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d(TAG, "connect");
                }

            }).on("update", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d(TAG, "update");
                    try {
                        JSONObject obj = (JSONObject) args[0];
                        flow(obj.getDouble("lat"), obj.getDouble("lon"));
                    } catch (JSONException e) {
                        Log.d(TAG, "json error");
                    }
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d(TAG, "disconnect");
                }

            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            Log.d(TAG, "erro:");
        }

    }

    @OnClick(R.id.imageView_dance)
    public void onClickDance(View v) {
        startUriAnim();
        startDanceAnim();
        startBGM();
    }

    public static Bundle createIntentBundle(double lat, double lon) {

        Bundle b = new Bundle();
        b.putDouble(BUNDLE_KEY_LAT, lat);
        b.putDouble(BUNDLE_KEY_LON, lon);
        return b;
    }



    private void startBGM() {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }else {
            mPlayer.start();
        }
    }
    private void startUriAnim() {
        Drawable drawable = getResources().getDrawable(R.drawable.uri_anim);
        mImageViewUri.setImageDrawable(drawable);
        if(drawable instanceof AnimationDrawable) {
            if (((AnimationDrawable) drawable).isRunning()) {
                ((AnimationDrawable) drawable).stop();
            } else {
                ((AnimationDrawable) drawable).start();
            }
        }
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


    private void setVolume() {
        int vol = mManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (vol * mVolPer), 0);
    }



    private void flow(double lat, double lon) {
        Log.d(TAG, "flow");
        final float[] results = new float[3];
        try {
            Location.distanceBetween(
                    mPartnerLat,
                    mPartnerLon,
                    lat,
                    lon,
                    results);
            Log.d(TAG, "lat:" + lat + " lon:" + lon + " parLat:"+ mPartnerLat + " parLon" + mPartnerLon);
            if(results != null && results.length > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), ((double) results[0]) + "m", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d(TAG, String.valueOf((double)results[0]) + "m");
            }

        } catch (IllegalArgumentException ex) {

        }

    }


}
