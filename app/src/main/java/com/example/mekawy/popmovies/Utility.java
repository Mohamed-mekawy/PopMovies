package com.example.mekawy.popmovies;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;

import com.example.mekawy.popmovies.Data.MoviesContract;

import java.util.HashMap;

public class Utility {

    public final static String RESIZE_WIDTH="resize_width";
    public final static String RESIZE_HIGHT="resize_hight";

    /* Detect weather Device is tablet or not by getting the configuration of the device screen , if it's Large or
     XLARGE will return true, else return 0
    */
    public static boolean isTablet(Context context){
        boolean xLarge=
                ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)==
                        Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean Large=
                ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)==
                        Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xLarge||Large);
    }

    /*method return 0 if device in portrait view , and return 1 if in LandScape mode*/
    public static Integer getCurrentOrientation(Context context){
        WindowManager wm=(WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display dsp=wm.getDefaultDisplay();
        int orient= dsp.getOrientation();
        if(orient==0) return 0;
        if(orient==1 || orient==3) return 1;
        return null;
    }

    /*
    * this method return the best poster link width size of the device as the themoviedb.org , introduce available
    * width sizes of (92,154,185, 342, 500,780) pixel , i use this method as if i for example fetch the "185" width
    * and stretch it by Picasso to half of the screen it will pixelated and image will not be with good quality , so
    * i implement this method to find the best available width to your device to have good image quality
    * also it will be one link in both portrait and landscape mode to not fetch the same image with multiple dimensions
    */
    public static String getBestFitLink(Context context){

        WindowManager wm=(WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display dsp=wm.getDefaultDisplay();

        int Image_avail_dimension_width[]={92,154,185, 342, 500,780};

        Integer orient=getCurrentOrientation(context);
        //get the Dimension of Device Screen
        int Screen_Width =dsp.getWidth();
        int Screen_Hight =dsp.getHeight();

        int Column_width=0;
        //Default size id not available
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

    /* return the suitable dimension to resize According to current Orientation , it return  HASHMAP
    * Containing keys RESIZE_WIDTH,RESIZE_HIGHT of image in current situation
    */
    public static HashMap<String,Integer> Get_Prefered_Dimension(Context context){
        HashMap<String,Integer> DimenMap=new HashMap<String,Integer>();

        WindowManager wm=(WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display dsp=wm.getDefaultDisplay();

        Integer orient=getCurrentOrientation(context);
        boolean isTablet=isTablet(context);

        int Screen_Width =dsp.getWidth();
        int Screen_Hight =dsp.getHeight();

        int resize_width=0;
        int resize_hight=0;
        Double calc_hight;
        //hight scale is fixed rate between hight and width
        double hight_scale=1.5;

        if(orient==0){
            resize_width=Screen_Width/2;
            calc_hight=new Double(resize_width*(hight_scale));
            resize_hight=calc_hight.intValue();
        }

        else if(isTablet && orient==1){
            resize_width=Screen_Width/6;
            calc_hight=new Double(resize_width*(hight_scale));
            resize_hight=calc_hight.intValue();
        }

        else if(!isTablet && orient==1){
            resize_width=Screen_Width/3;
            calc_hight=new Double(resize_width*(hight_scale));
            resize_hight=calc_hight.intValue();
        }

        DimenMap.put(RESIZE_WIDTH,resize_width);
        DimenMap.put(RESIZE_HIGHT,resize_hight);
        return DimenMap;
    }

    //get Current Associated Table_name according to current Sort_mode
    public static String get_table_name(Context context){
        Uri uri=get_content_uri(context);
        if(uri.getPathSegments().get(0).equals(MoviesContract.POP_MOVIES_TABLE.TABLE_NAME))
            return  MoviesContract.POP_MOVIES_TABLE.TABLE_NAME;
        else if(uri.getPathSegments().get(0).equals(MoviesContract.VOTE_DESC_TABLE.TABLE_NAME))
            return MoviesContract.VOTE_DESC_TABLE.TABLE_NAME;
        else if(uri.getPathSegments().get(0).equals(MoviesContract.FAV_MOVIES_TABLE.TABLE_NAME))
            return MoviesContract.FAV_MOVIES_TABLE.TABLE_NAME;

        return null;
    }


    public static String getsortmethod(Context context){
        return  PreferenceManager.getDefaultSharedPreferences(context).
                getString(context.getString(R.string.setting_sort_key),context.getString(R.string.sort_popularity_desc));
    }

    //get the Current Uri according to sort mode
    public static Uri get_content_uri(Context context){
        String mode=getsortmethod(context);
        Uri ret_uri=null;

        if(mode.equals(context.getString(R.string.sort_popularity_desc)))
            ret_uri= MoviesContract.POP_MOVIES_TABLE.CONTENT_URI;

        else if(mode.equals(context.getString(R.string.sort_vote_average_desc)))
            ret_uri= MoviesContract.VOTE_DESC_TABLE.CONTENT_URI;

        else if(mode.equals(context.getString(R.string.sort_fav))){
            ret_uri= MoviesContract.FAV_MOVIES_TABLE.CONTENT_URI;
        }
        return ret_uri;
    }





}
