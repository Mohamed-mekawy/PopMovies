package com.example.mekawy.popmovies;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mekawy.popmovies.Data.dbContract;

import java.util.zip.Inflater;


public class TrailerAdapter extends CursorAdapter{

    private Context mContext;

    public TrailerAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
        mContext=context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootview= LayoutInflater.from(mContext).inflate(R.layout.trailer_item,parent,false);
        return rootview;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

       TextView Trailer_name=(TextView) view.findViewById(R.id.trailer_name);
       Trailer_name.setText(cursor.getString(cursor.getColumnIndex(dbContract.MOVIE_VIDEOS.OWM_COLUMN_TRAILER_NAME)));
    }



}
