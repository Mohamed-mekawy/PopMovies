package com.example.mekawy.popmovies;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.mekawy.popmovies.Data.dbContract;

import java.util.HashMap;

public class Utility {

    public static boolean isTablet(Context context){
        boolean xLarge=
                ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)==
                        Configuration.SCREENLAYOUT_SIZE_XLARGE);

        boolean Large=
                ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)==
                        Configuration.SCREENLAYOUT_SIZE_LARGE);

        return (xLarge||Large);
    }


    public static Integer getCurrentOrientation(Context context){
        WindowManager wm=(WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display dsp=wm.getDefaultDisplay();
        int orient= dsp.getOrientation();
        if(orient==0) return 0;
        if(orient==1 || orient==3) return 1;
        return null;
    }




    public static String getBestFitLink(Context context){
        WindowManager wm=(WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display dsp=wm.getDefaultDisplay();

        int Image_avail_dimension_width[]={92,154,185, 342, 500,780};

        Integer orient=getCurrentOrientation(context);
        boolean isTablet=isTablet(context);

        int Screen_Width =dsp.getWidth();
        int Screen_Hight =dsp.getHeight();

        int Column_width=0;

        int ret_size=185;

        if(orient==0)
            Column_width= Screen_Width /2;

        else if( orient==1)
            Column_width=Screen_Hight/2;

        for( int i=0;i< Image_avail_dimension_width.length;i++){
            if(Column_width> Image_avail_dimension_width[i])
            {
                ret_size= Image_avail_dimension_width[i];
            }
        }

        return Integer.toString(ret_size);
    }





/*

    public static void testing(Context context){
        WindowManager wm=(WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display dsp=wm.getDefaultDisplay();


        Integer orient=getCurrentOrientation(context);
        boolean isTablet=isTablet(context);

        int Screen_Width =dsp.getWidth();
        int Screen_Hight =dsp.getHeight();

        int resize_width=0;
        int resize_hight=0;


        if(orient==0){
            resize_width=Screen_Width/2;
        }

        else if(isTablet && orient==1){
            resize_width=Screen_Width/6;

        }

    }*/










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


    public static String get_table_name(Context context){
        Uri uri=get_content_uri(context);
        if(uri.getPathSegments().get(0).equals(dbContract.POP_MOVIES_TABLE.TABLE_NAME))
            return  dbContract.POP_MOVIES_TABLE.TABLE_NAME;
        else if(uri.getPathSegments().get(0).equals(dbContract.MOST_VOTED_TABLE.TABLE_NAME))
            return dbContract.MOST_VOTED_TABLE.TABLE_NAME;
        return null;
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
