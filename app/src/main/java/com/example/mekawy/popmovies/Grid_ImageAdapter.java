package com.example.mekawy.popmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Grid_ImageAdapter extends ArrayAdapter<Movie_object> {

    private Context gContext;
    private int resize_width;
    private int resize_hight;
    private String http_width;
    private String IMAGE_BASE="http://image.tmdb.org/t/p/w";

    public Grid_ImageAdapter(Context context, ArrayList<Movie_object> objects) {
        super(context, 0, objects);
        gContext=context;

        HashMap<String,Integer> Dimen=Utility.Get_Prefered_Dimension(gContext);

        resize_width=Dimen.get("resize_width");
        resize_hight=Dimen.get("resize_hight");
        http_width=Integer.toString(Dimen.get("Http_width"));
        IMAGE_BASE+=http_width;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie_object obj=getItem(position);
        ImageView mImageView;

        if(convertView==null)
            mImageView=new ImageView(gContext);
        else
            mImageView=(ImageView) convertView;
        try {
            Picasso.with(getContext()).
                    load(IMAGE_BASE+obj.get_Grid_Poster()).
                    resize(resize_width, resize_hight).
                    into(mImageView);

            }catch (Exception e){
            e.printStackTrace();
        }

        return mImageView;
    }

}
