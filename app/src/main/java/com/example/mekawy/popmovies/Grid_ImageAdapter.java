package com.example.mekawy.popmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;


//Test dummy Data
public class Grid_ImageAdapter extends BaseAdapter {

    private Context mContext;

    private static int Resize_width;
    private static int Resize_hight;
    private static int Http_width;

    public Grid_ImageAdapter(Context pContext){
        mContext =pContext;
        HashMap<String,Integer> mSizes=Utility.Get_Prefered_Dimension(pContext);
        Resize_width=mSizes.get("resize_width");
        Resize_hight=mSizes.get("resize_hight");
        Http_width=mSizes.get("Http_width");
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView mImage;

        if(view==null)
            mImage=new ImageView(mContext);
        else
            mImage=(ImageView) view;

        Picasso.with(mContext).
                load("http://image.tmdb.org/t/p/w"+Integer.toString(Http_width)+"/9gm3lL8JMTTmc3W4BmNMCuRLdL8.jpg").
                resize(Resize_width, Resize_hight).
                into(mImage);

        return mImage;
    }

}
