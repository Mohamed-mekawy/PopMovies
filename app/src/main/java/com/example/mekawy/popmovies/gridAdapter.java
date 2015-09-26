package com.example.mekawy.popmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.mekawy.popmovies.Data.dbContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class gridAdapter extends ArrayAdapter<Integer> {

    private Context mContext;
    private int resize_width;
    private int resize_hight;
    private String http_width;
    private String IMAGE_BASE="http://image.tmdb.org/t/p/w";

    public gridAdapter(Context context, ArrayList<Integer> Records_id) {
        super(context, 0, Records_id);
        mContext =context;

        HashMap<String,Integer> Dimen=Utility.Get_Prefered_Dimension(mContext);

        resize_width=Dimen.get("resize_width");
        resize_hight=Dimen.get("resize_hight");
        http_width=Integer.toString(Dimen.get("Http_width"));
        IMAGE_BASE+=http_width;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView mImageView;

        if(convertView==null)
        {
            mImageView=new ImageView(mContext);
        }
        else
        {
            mImageView=(ImageView) convertView;
        }

        int Record_id=getItem(position);
        Log.i("record number",Integer.toString(Record_id));

        Uri current_Content_uri=Utility.get_content_uri(mContext);

        Cursor cr=mContext.getContentResolver().query(
                current_Content_uri,
                new String[]{dbContract.OWM_COMMON_POSTER_PATH},
                dbContract.OWM_COMMON_COLUMN_TAG+" = ?",
                new String[]{Integer.toString(Record_id)},
                null
                );

        if(cr.moveToFirst()){
            try {
                Picasso.with(getContext()).
                        load(IMAGE_BASE+cr.getString(cr.getColumnIndex(dbContract.OWM_COMMON_POSTER_PATH))).
                        resize(resize_width, resize_hight).
                        into(mImageView);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return mImageView;
    }

}
