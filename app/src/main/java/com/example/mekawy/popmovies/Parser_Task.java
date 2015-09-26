package com.example.mekawy.popmovies;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mekawy.popmovies.Data.dbContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class Parser_Task extends AsyncTask<String,Void,Movie_object[]>{

    private Movie_object[] temp_objects;

    private Context mContext;
    private Grid_ImageAdapter mAdapter;
    private static String sort_mode;

    final String OWM_RESULTS="results";

    final String OWM_TAG="id";
    final String OWM_TITLE="original_title";
    final String OWM_GRID_POSTER="poster_path";
    final String OWM_OVERVIEW="overview";
    final String OWM_DATE="release_date";
    final String OWM_RATE="vote_average";
    final String OWM_ISFAV="isFav";




    public Parser_Task(Context C,Grid_ImageAdapter fadapter,String mode){
        mContext=C;
        mAdapter=fadapter;
        sort_mode=mode;
    }


    @Override
    protected void onPostExecute(Movie_object[] movie_objects) {
        super.onPostExecute(movie_objects);

        Uri Content_uri=null;
        String Images[];

        if(sort_mode.equals(mContext.getString(R.string.sort_popularity_desc)))
            Content_uri= dbContract.POP_MOVIES_TABLE.CONTENT_URI;
        else if(sort_mode.equals(mContext.getString(R.string.sort_vote_average_desc)))
            Content_uri=dbContract.MOST_VOTED_TABLE.CONTENT_URI;

        Cursor cur=mContext.getContentResolver().query(Content_uri,
                new String[]{OWM_TAG,OWM_TITLE,OWM_OVERVIEW,OWM_GRID_POSTER,OWM_DATE,OWM_RATE},
                null,
                null,
                null
                );
        Images=new String[cur.getCount()];

        if (cur.moveToFirst()){
            do {
                Images[cur.getPosition()]=cur.getString(cur.getColumnIndex(OWM_GRID_POSTER));
            }while (cur.moveToNext());
        }

        for(String image:Images){
            Log.i("images",image);

        }

            mAdapter.clear();
            for(Movie_object movie:movie_objects){
                mAdapter.add(movie);
            }
    }


    public ContentValues GET_MOVIE_CONTENT(JSONObject movie_object){
        ContentValues retContent =new ContentValues();

        try {
            int mTag=movie_object.getInt(OWM_TAG);
            String mTitle=movie_object.getString(OWM_TITLE);
            String mOverview=movie_object.getString(OWM_OVERVIEW);
            String mPoster=movie_object.getString(OWM_GRID_POSTER);
            String mDate=movie_object.getString(OWM_DATE);
            double mRate=movie_object.getDouble(OWM_RATE);
            int isFav=0;

            retContent.put(OWM_TAG, mTag);
            retContent.put(OWM_TITLE, mTitle);
            retContent.put(OWM_OVERVIEW, mOverview);
            retContent.put(OWM_GRID_POSTER, mPoster);
            retContent.put(OWM_DATE, mDate);
            retContent.put(OWM_RATE, mRate);
            retContent.put(OWM_ISFAV,isFav);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retContent;
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
                ContentValues mContent= GET_MOVIE_CONTENT(Movie_object);
                Content_vector.add(mContent);
            }

            if(Content_vector.size()>0){
                ContentValues [] Content_array=new ContentValues[Content_vector.size()];
                Content_vector.toArray(Content_array);
                Uri Content_uri=null;

                if(sort_mode.equals(mContext.getString(R.string.sort_popularity_desc)))
                    Content_uri= dbContract.POP_MOVIES_TABLE.CONTENT_URI;
                else if(sort_mode.equals(mContext.getString(R.string.sort_vote_average_desc)))
                    Content_uri=dbContract.MOST_VOTED_TABLE.CONTENT_URI;

               int inserted_counter= mContext.getContentResolver().bulkInsert(Content_uri,Content_array);
            }







        } catch (JSONException e) {
            e.printStackTrace();
        }

        return temp_objects;
    }
}
