package jp.kfujine.oadc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.kfujine.oadc.http.PhpApi;
import jp.kfujine.oadc.http.RequestCallback;
import jp.kfujine.oadc.http.RequestListener;
import jp.kfujine.oadc.object.Info;
import jp.kfujine.oadc.view.ListViewAdapter;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = ListActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ListView mListView;

    private PhpApi mPhpService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        RestAdapter adapter = new RestAdapter.Builder()
            .setEndpoint(PhpApi.HOST)
            .build();
        mPhpService = adapter.create(PhpApi.class);

        List<Info> list = new ArrayList<>();

        ListViewAdapter ListAdapter = new ListViewAdapter(this, R.layout.view_list_item, 0, list);
        mListView.setAdapter(ListAdapter);
        mListView.setOnItemClickListener(this);

        requestListData();
    }

    private void requestListData() {
        mPhpService.getList(new RequestCallback<>(new RequestListener<List<Info>>() {
            @Override
            public void onSuccess(List<Info> response) {
                Log.d(TAG, "requestListData onSuccess");

                ((ListViewAdapter)mListView.getAdapter()).setInfoList(response);

            }


            @Override
            public void onFailure(RetrofitError error) {

            }
        }));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "onItemClick");

    }
}
