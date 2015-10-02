package com.example.mekawy.popmovies.Data;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mekawy.popmovies.R;

public class TrailerAdapter extends CursorAdapter{


    private Context mContext;

    public TrailerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.trailer_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView Trailer_name=(TextView) view.findViewById(R.id.trailer_name);
        Trailer_name.setText(cursor.getString(cursor.getColumnIndex(dbContract.MOVIE_VIDEOS.OWM_COLUMN_TRAILER_NAME)));
    }



}
