package com.example.mekawy.popmovies;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mekawy.popmovies.Data.dbContract;
import com.example.mekawy.popmovies.Data.dbContract.MOVIE_VIDEOS;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    //Movies Videos data
    final String MOVIES_VIDEOS_TRAILER_ID="id";
    final String MOVIES_VIDEOS_TRAILER_KEY="key";
    final String MOVIES_VIDEOS_TRAILER_NAME="name";


    public Parser_Task(Context context){
        mContext=context;

    }




    public void get_Trailers(String movie_tag){

        BufferedReader reader=null;
        String Line=null;
        StringBuffer JSON_String=null;

        final String MOVIES_VIDEOS_BASE="https://api.themoviedb.org/3/movie";
        final String MOVIES_VIDEOS_QUERY="videos";
        final String api_key_string="api_key";

        String api_key_value=movies_api_key.API_KEY.get_API_key();

        Uri trailer_query=Uri.parse(MOVIES_VIDEOS_BASE).buildUpon().appendPath(movie_tag)
                .appendPath(MOVIES_VIDEOS_QUERY).appendQueryParameter(api_key_string,api_key_value).build();

        try {
            URL url=new URL(trailer_query.toString());
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream input_reader=connection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(input_reader));
            JSON_String=new StringBuffer();
            while ((Line=reader.readLine())!=null)JSON_String.append(Line);

            JSONObject Movie_Object=new JSONObject(JSON_String.toString());
            JSONArray Trailer_array=Movie_Object.getJSONArray(OWM_RESULTS);


            for(int index=0;index<Trailer_array.length();index++){

                JSONObject mTrailer=Trailer_array.getJSONObject(index);
                String trailer_id=mTrailer.getString(MOVIES_VIDEOS_TRAILER_ID);

                ContentValues contentValues=new ContentValues();

                    contentValues.put(MOVIE_VIDEOS.OWM_COLUMN_MOVIE_TAG,movie_tag);
                    contentValues.put(MOVIE_VIDEOS.OWM_COLUMN_TRAILER_ID,mTrailer.getString(MOVIES_VIDEOS_TRAILER_ID));
                    contentValues.put(MOVIE_VIDEOS.OWM_COLUMN_KEY, mTrailer.getString(MOVIES_VIDEOS_TRAILER_KEY));
                    contentValues.put(MOVIE_VIDEOS.OWM_COLUMN_TRAILER_NAME, mTrailer.getString(MOVIES_VIDEOS_TRAILER_NAME));

                  Uri entry=mContext.getContentResolver().insert(MOVIE_VIDEOS.CONTENT_URI, contentValues);
                  Log.i("trailer_entry",entry.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
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
    protected Integer doInBackground(String... mString) {
        int inserted_counter=0;
        String sort_mode=Utility.getsortmethod(mContext);

        if(mString[0]!=null) {

            try {
                JSONObject big_obj = new JSONObject(mString[0]);
                JSONArray results_array = big_obj.getJSONArray(OWM_RESULTS);

                Vector<ContentValues> Content_vector = new Vector<ContentValues>(results_array.length());

                for (int index = 0; index < results_array.length(); index++) {

                    JSONObject Movie_object = results_array.getJSONObject(index);
                    ContentValues mContent = GET_MOVIE_CONTENT(Movie_object);
                    Content_vector.add(mContent);
                }

                if (Content_vector.size() > 0) {
                    ContentValues[] Content_array = new ContentValues[Content_vector.size()];
                    Content_vector.toArray(Content_array);
                    Uri Content_uri = null;

                    if (sort_mode.equals(mContext.getString(R.string.sort_popularity_desc)))
                        Content_uri = dbContract.POP_MOVIES_TABLE.CONTENT_URI;

                    else if (sort_mode.equals(mContext.getString(R.string.sort_vote_average_desc)))
                        Content_uri = dbContract.MOST_VOTED_TABLE.CONTENT_URI;

                    //Filter only new records needs to be inserted
                    for (int movie_index = 0; movie_index < Content_vector.size(); movie_index++) {
                        String mTag = Integer.toString(Content_array[movie_index].getAsInteger(OWM_TAG));

                        Cursor cr = mContext.getContentResolver().query(
                                Content_uri,
                                new String[]{OWM_TAG},
                                OWM_TAG + "= ?",
                                new String[]{mTag},
                                null
                        );

                    get_Trailers(mTag);

                        if (!cr.moveToFirst()) {
                            Uri uri_insert = mContext.getContentResolver().insert(Content_uri, Content_array[movie_index]);
                            Log.i("insert_uri", uri_insert.toString());
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


                //get pre loaded trailers
                Cursor curr = mContext.getContentResolver().query(
                        MOVIE_VIDEOS.CONTENT_URI,
                        new String[]{MOVIE_VIDEOS.OWM_COLUMN_TRAILER_ID,MOVIE_VIDEOS._ID},
                        null,
                        null,
                        null
                );

                if(curr.moveToFirst()){
                    do {
                        Log.i("trailer name : "+curr.getString(curr.getColumnIndex(MOVIE_VIDEOS._ID))+"  :",curr.getString(curr.getColumnIndex(MOVIE_VIDEOS.OWM_COLUMN_TRAILER_ID)));
                    }while (curr.moveToNext());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return inserted_counter;
    }
}
