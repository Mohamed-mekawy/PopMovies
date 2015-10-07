package com.example.mekawy.popmovies;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mekawy.popmovies.Data.dbContract;

public class movie_detailsAdapter extends CursorAdapter{



    public movie_detailsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        //why
        View view= LayoutInflater.from(context).inflate(R.layout.trailer_item,viewGroup,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv=(TextView)view.findViewById(R.id.trailer_name);
        tv.setText(cursor.getString(cursor.getColumnIndex(dbContract.MOVIE_VIDEOS.OWM_COLUMN_TRAILER_NAME)));
    }
}
