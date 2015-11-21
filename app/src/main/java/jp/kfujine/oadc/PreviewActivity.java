package jp.kfujine.oadc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviewActivity extends AppCompatActivity {
    private static final String BUNDLE_KEY_IMAGE_URL= "image_url";

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

    public static Bundle createIntentBundle(Uri imgUrl) {
        Bundle b = new Bundle();
        b.putParcelable(BUNDLE_KEY_IMAGE_URL, imgUrl);

        return b;
    }

    @OnClick(R.id.button_send)
    public void onClickSend(View v) {
        Intent intent = new Intent();
        intent.setClass(this, DanceActivity.class);
        startActivity(intent);
    }

}
