package com.example.mekawy.popmovies;


import android.content.Context;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

import com.example.mekawy.popmovies.Data.dbContract;

import java.util.HashMap;

public class Utility {

    //return best dimensions in hashmap for grid posters
    public static HashMap<String,Integer> Get_Prefered_Dimension(Context uContext){

        HashMap<String,Integer> DimenMap=new HashMap<String,Integer>();

        int img_dimesnsions_width[]={92,154,185,342,500,780};
        int img_dimesnsions_hight[]={138,231,278,513,750,1170};
        int index=0;
        int width =uContext.getResources().getDisplayMetrics().widthPixels;
        int height=uContext.getResources().getDisplayMetrics().heightPixels;

        int mid_width=width/2;
        int ret_size=0;

        for( int i=0;i<img_dimesnsions_width.length;i++){
            if(mid_width>img_dimesnsions_width[i])
            {
                ret_size=img_dimesnsions_width[i];
                index=i;
            }
        }

        DimenMap.put("resize_width",mid_width);
        DimenMap.put("resize_hight",img_dimesnsions_hight[index]);
        DimenMap.put("Http_width",ret_size);

        return DimenMap;
    }


    public static String getsortmethod(Context context){
        return  PreferenceManager.getDefaultSharedPreferences(context).
                getString(context.getString(R.string.setting_sort_key),context.getString(R.string.sort_popularity_desc));
    }


    public static Uri get_content_uri(Context context){
        String mode=getsortmethod(context);
        Uri ret_uri=null;

        if(mode.equals(context.getString(R.string.sort_popularity_desc)))
            ret_uri=dbContract.POP_MOVIES_TABLE.CONTENT_URI;

        else if(mode.equals(context.getString(R.string.sort_vote_average_desc)))
            ret_uri=dbContract.MOST_VOTED_TABLE.CONTENT_URI;

        return ret_uri;
    }





}
