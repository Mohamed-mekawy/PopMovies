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

public class Parser_Task extends AsyncTask<String,Void,Integer>{

    private Context mContext;

    final String OWM_RESULTS="results";

    final String OWM_TAG="id";
    final String OWM_TITLE="original_title";
    final String OWM_GRID_POSTER="poster_path";
    final String OWM_OVERVIEW="overview";
    final String OWM_DATE="release_date";
    final String OWM_RATE="vote_average";
    final String OWM_ISFAV="isFav";


    public Parser_Task(Context context){

        mContext=context;
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
    protected Integer doInBackground(String... mString) {
        int inserted_counter=0;
        String sort_mode=Utility.getsortmethod(mContext);
        try {
            JSONObject big_obj=new JSONObject(mString[0]);
            JSONArray results_array=big_obj.getJSONArray(OWM_RESULTS);

            Vector<ContentValues> Content_vector=new Vector<ContentValues>(results_array.length());

            for(int index=0;index<results_array.length();index++){

                JSONObject Movie_object=results_array.getJSONObject(index);
                ContentValues mContent= GET_MOVIE_CONTENT(Movie_object);
                Content_vector.add(mContent);
            }

            if(Content_vector.size()>0){
                ContentValues [] Content_array=new ContentValues[Content_vector.size()];
                Content_vector.toArray(Content_array);
                Uri Content_uri=null;

                if(sort_mode.equals(mContext.getString(R.string.sort_popularity_desc)))
                    Content_uri = dbContract.POP_MOVIES_TABLE.CONTENT_URI;

                else if(sort_mode.equals(mContext.getString(R.string.sort_vote_average_desc)))
                    Content_uri=dbContract.MOST_VOTED_TABLE.CONTENT_URI;

                //Filter only new records needs to be inserted
                for(int movie_index=0;movie_index<Content_vector.size();movie_index++) {

                    Cursor cr = mContext.getContentResolver().query(
                            Content_uri,
                            new String[]{OWM_TAG},
                            OWM_TAG + " = ?",
                            new String[]{Integer.toString(Content_array[movie_index].getAsInteger(OWM_TAG))},
                            null
                    );

                    if(!cr.moveToFirst()) {
                        Uri uri_insert=mContext.getContentResolver().insert(Content_uri, Content_array[movie_index]);
                        Log.i("insert_uri",uri_insert.toString());
                        inserted_counter++;
                    }

                   /* else if(cr.moveToFirst()){
                        Content_array[movie_index].remove(OWM_ISFAV);

                        int update=
                                mContext.getContentResolver().update(
                                Content_uri,
                                Content_array[movie_index],
                                OWM_TAG + " = ?",
                                new String[]{Integer.toString(Content_array[movie_index].getAsInteger(OWM_TAG))}
                        );
                        Log.i("update_uri", Integer.toString(update));
                    }
                    */

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return inserted_counter;
    }
}
