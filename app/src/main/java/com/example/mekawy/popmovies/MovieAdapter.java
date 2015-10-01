package com.example.mekawy.popmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mekawy.popmovies.Data.dbContract;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class MovieAdapter extends CursorAdapter {

    private Context mContext;
    private int resize_width;
    private int resize_hight;
    private String http_width;

    private String IMAGE_BASE="http://image.tmdb.org/t/p/w"+MainFragment.BEST_FIT_IMAGE;

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;

        HashMap<String, Integer> Dimen = Utility.Get_Prefered_Dimension(mContext);
        resize_width = Dimen.get(Utility.RESIZE_WIDTH);
        resize_hight = Dimen.get(Utility.RESIZE_HIGHT);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View root= LayoutInflater.from(mContext).inflate(R.layout.movie_poster,parent,false);
        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView mImageview=(ImageView)view;

        String image_path =
                    cursor.getString(cursor.getColumnIndex(dbContract.OWM_COMMON_POSTER_PATH));

        Picasso.with(mContext).
                load(IMAGE_BASE+image_path).
                resize(resize_width, resize_hight).
                into(mImageview);
    }

}
