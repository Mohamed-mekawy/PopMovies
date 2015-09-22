package com.example.mekawy.popmovies;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Parser_Task extends AsyncTask<String,Void,Movie_object[]>{

    private Movie_object[] temp_objects;

    private Context mContext;
    private Grid_ImageAdapter mAdapter;

    public Parser_Task(Context C,Grid_ImageAdapter fadapter){
        mContext=C;
        mAdapter=fadapter;
    }

    @Override
    protected void onPostExecute(Movie_object[] movie_objects) {
        super.onPostExecute(movie_objects);
            mAdapter.clear();
            for(Movie_object movie:movie_objects){
                mAdapter.add(movie);
            }
    }

    @Override
    protected Movie_object[] doInBackground(String... strings) {

        final String OWM_RESULTS="results";
        final String OWM_TITLE="original_title";
        final String OWM_GRID_POSTER="poster_path";
        final String OWM_THUM_POSTER="backdrop_path";
        final String OWM_OVERVIEW="overview";
        final String OWM_DATE="release_date";
        final String OWM_RATE="vote_average";

        try {
            JSONObject big_obj=new JSONObject(strings[0]);
            JSONArray results_array=big_obj.getJSONArray(OWM_RESULTS);
            temp_objects=new Movie_object[results_array.length()];

            for(int index=0;index<results_array.length();index++){
                temp_objects[index]=new Movie_object();

                JSONObject Movie_object=results_array.getJSONObject(index);
                temp_objects[index].set_Original_title(Movie_object.getString(OWM_TITLE));
                temp_objects[index].set_Grid_Poster(Movie_object.getString(OWM_GRID_POSTER));
                temp_objects[index].set_thum_Poster(Movie_object.getString(OWM_THUM_POSTER));
                temp_objects[index].set_Overview(Movie_object.getString(OWM_OVERVIEW));
                temp_objects[index].set_Release_date(Movie_object.getString(OWM_DATE));
                temp_objects[index].set_Rating(Movie_object.getDouble(OWM_RATE));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return temp_objects;
    }
}
