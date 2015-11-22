package jp.kfujine.oadc.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.kfujine.oadc.MapActivity;
import jp.kfujine.oadc.R;
import jp.kfujine.oadc.http.PhpApi;
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

    public void setInfoList(List<Info> list) {
        mList.addAll(list);
        notifyDataSetChanged();

    }


    public class InfoViewHolder {
        @Bind(R.id.layout_item)
        RelativeLayout mLayout;
        @Bind(R.id.textView_date)
        TextView mTextViewDate;
        @Bind(R.id.button_map)
        ImageButton mButtonMap;
        @Bind(R.id.imageView_item)
        ImageView mImageViewItem;

        public InfoViewHolder(View view) {
            ButterKnife.bind(this, view);

        }

        public void bind(final Info info) {
            if (info == null) {
                return;
            }

            mTextViewDate.setText(getDateStr(info.getDate()));
            if (info.getImageName() != null && !info.getImageName().isEmpty()) {
                StringBuilder sb = new StringBuilder(PhpApi.HOST);
                sb.append("/image/").append(info.getImageName());
                Picasso.with(getContext()).load(sb.toString()).into(mImageViewItem);
            }

            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.setClass(mContext, MapActivity.class);
                    Bundle b = MapActivity.createIntentBundle(info.getId());
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });
        }



    }

    public static String getDateStr(String dateStr) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
            long time = date.getTime();
            long diffTime = Calendar.getInstance().getTimeInMillis() - time;

            if (diffTime >= 1 * 1000 * 60 * 60) {
                return "1時間以上前";
            } else {
                int min = (int)diffTime / (1000 * 60);
                return min + "分前";
            }
        }catch (ParseException e) {
            return dateStr == null ? "" : dateStr;
        }
    }
}
