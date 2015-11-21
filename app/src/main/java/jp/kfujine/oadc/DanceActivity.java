package jp.kfujine.oadc;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DanceActivity extends Activity{
    private static final String TAG = DanceActivity.class.getSimpleName();
    private MediaPlayer mPlayer;
    private AudioManager mManager;
    private double mVolPer = 0.1;

    @Bind(R.id.imageView_dance)
    ImageView mImageViewDance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm);
        mPlayer.setLooping(true);
        mManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);


    }

    @OnClick(R.id.button_play)
    public void onClickButton(View v) {
        startBGM();

        Drawable drawable= mImageViewDance.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }
    }

    @OnClick(R.id.button_plus)
    public void onClickButtonPlus() {
        if (mVolPer < 1) {
            mVolPer +=0.1;
            setVolume();
        }


    }

    @OnClick(R.id.button_minus)
    public void onClickButtonMinus() {
        if (mVolPer > 0) {
            mVolPer -=0.1;
            setVolume();
        }

    }

    private void startBGM() {
        mPlayer.start();
    }

    private void setVolume() {
        int vol = mManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (vol * mVolPer), 0);
    }


}
