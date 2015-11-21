package jp.kfujine.oadc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = MenuActivity.class.getSimpleName();
    private static int REQ_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ButterKnife.bind(this);

        Log.d(TAG, "dpi" + getResources().getDisplayMetrics().densityDpi);
    }

    @OnClick(R.id.button_okinawa)
    public void buttonOkinawa(View view) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, REQ_CAMERA);
    }

    @OnClick(R.id.button_travel)
    public void buttonTravel(View view) {

        Intent intent = new Intent();
        intent.setClass(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_CAMERA == requestCode) {
            Log.d(TAG, "camera result");
            Bundle b = PreviewActivity.createIntentBundle(data.getData());
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtras(b);

            startActivity(intent);

        }
    }
}
