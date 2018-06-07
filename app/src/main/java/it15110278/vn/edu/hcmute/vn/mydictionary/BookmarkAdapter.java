package it15110278.vn.edu.hcmute.vn.mydictionary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ASUS on 6/7/2018.
 */

public class BookmarkAdapter extends BaseAdapter {

    private ListItemListener listener;
    private ListItemListener listenerDelete;

    Context mContext;
    ArrayList<String> mScource;

    public BookmarkAdapter(Context context, String[] source) {
        this.mContext = context;
        this.mScource = new ArrayList<>(Arrays.asList(source));
    }

    @Override
    public int getCount() {
        return mScource.size();
    }

    @Override
    public Object getItem(int i) {
        return mScource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.bookmark_item, viewGroup, false);
            viewHolder.textView = view.findViewById(R.id.textWord);
            viewHolder.btnDelete = view.findViewById(R.id.btnDelete);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(mScource.get(i));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(i);
            }
        });


        //Sự kiện khi click vào button Delete
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerDelete != null)
                    listenerDelete.onItemClick(i);
            }
        });
        return view;
    }


    public void removeItem(int position) {
        mScource.remove(position);
    }



    public class ViewHolder {
        TextView textView;
        ImageView btnDelete;
    }

    public void setOnItemClick(ListItemListener listItemListener) {
        this.listener = listItemListener;

    }

    //  Delete
    public void setOnItemDeleteClick(ListItemListener listItemListener) {
        this.listenerDelete = listItemListener;

    }


}
