package jp.kfujine.oadc.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.kfujine.oadc.R;
import jp.kfujine.oadc.object.Info;

/**
 * Created by fuji on 2015/11/22.
 */
public class ListViewAdapter extends ArrayAdapter<Info>{
    private Context mContext;
    private List<Info> mList;
    public ListViewAdapter(Context context, int resource, int textViewResourceId, List<Info> objects) {
        super(context, resource, textViewResourceId, objects);
        mContext = context;
        mList = objects;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InfoViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_list_item, parent, false);
            holder = new InfoViewHolder(convertView);
            convertView.setTag(holder);
        }  else {
            holder =(InfoViewHolder) convertView.getTag();
        }

        holder.bind(getItem(position));

        return convertView;

    }


    public class InfoViewHolder {
        @Bind(R.id.textView_date)
        TextView mTextViewDate;
        @Bind(R.id.button_map)
        ImageButton mButtonMap;
        @Bind(R.id.imageView_item)
        ImageView mImageViewItem;

        public InfoViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(Info info) {
            Log.d("", "");
        }


    }
}
