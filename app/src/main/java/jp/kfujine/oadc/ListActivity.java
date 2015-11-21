package jp.kfujine.oadc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.kfujine.oadc.object.Info;
import jp.kfujine.oadc.view.ListViewAdapter;

public class ListActivity extends AppCompatActivity {
    @Bind(R.id.listView)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        List<Info> list = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            list.add(new Info());
        }
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.view_list_item, 0, list);
        mListView.setAdapter(adapter);


    }

}
