package com.example.mekawy.popmovies;


import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mekawy.popmovies.Data.dbContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Vector;


import com.example.mekawy.popmovies.Data.dbContract.FAV_MOVIES_TABLE;
import com.example.mekawy.popmovies.Data.dbContract.MOST_VOTED_TABLE;
import com.example.mekawy.popmovies.Data.dbContract.POP_MOVIES_TABLE;
public class Parser_Task extends AsyncTask<String,Void,Movie_object[]>{

    private Movie_object[] temp_objects;

    private Context mContext;
    private Grid_ImageAdapter mAdapter;
    private String sort_mode;

    final String OWM_RESULTS="results";

    final String OWM_TAG="id";
    final String OWM_TITLE="original_title";
    final String OWM_GRID_POSTER="poster_path";
    final String OWM_OVERVIEW="overview";
    final String OWM_DATE="release_date";
    final String OWM_RATE="vote_average";





    public Parser_Task(Context C,Grid_ImageAdapter fadapter,String mode){
        mContext=C;
        mAdapter=fadapter;
        sort_mode=mode;
    }

//
//    @Override
//    protected void onPostExecute(Movie_object[] movie_objects) {
//        super.onPostExecute(movie_objects);
//            mAdapter.clear();
//            for(Movie_object movie:movie_objects){
//                mAdapter.add(movie);
//            }
//    }
//
//

    public ContentValues GET_POP_CONTENT(JSONObject movie_object){
        ContentValues mContent=new ContentValues();

        try {
            int mTag=movie_object.getInt(OWM_TAG);
            String mTitle=movie_object.getString(OWM_TITLE);
            String mOverview=movie_object.getString(OWM_OVERVIEW);
            String mPoster=movie_object.getString(OWM_GRID_POSTER);
            String mDate=movie_object.getString(OWM_DATE);
            double mRate=movie_object.getDouble(OWM_RATE);
            int isFav=0;

            mContent.put(OWM_TAG,mTag);
            mContent.put(OWM_TITLE,mTitle);
            mContent.put(OWM_OVERVIEW,mOverview);
            mContent.put(OWM_GRID_POSTER,mPoster);
            mContent.put(OWM_DATE,mDate);
            mContent.put(OWM_RATE,mRate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

            Log.i("ContentData",mContent.toString());

        return null;
    }


    @Override
    protected Movie_object[] doInBackground(String... mString) {



        try {
            JSONObject big_obj=new JSONObject(mString[0]);
            JSONArray results_array=big_obj.getJSONArray(OWM_RESULTS);
            temp_objects=new Movie_object[results_array.length()];

            Vector<ContentValues> Content_vector=new Vector<ContentValues>(results_array.length());

            for(int index=0;index<results_array.length();index++){
                temp_objects[index]=new Movie_object();

                JSONObject Movie_object=results_array.getJSONObject(index);
                ContentValues mContent=GET_POP_CONTENT(Movie_object);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return temp_objects;
    }
}
