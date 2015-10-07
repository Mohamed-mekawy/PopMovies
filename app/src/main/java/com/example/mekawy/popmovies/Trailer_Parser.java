package com.example.mekawy.popmovies;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mekawy.popmovies.Data.dbContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Trailer_Parser extends AsyncTask<String,Void,Void>{

    //Movies Videos data
    final String MOVIES_VIDEOS_TRAILER_ID="id";
    final String MOVIES_VIDEOS_TRAILER_KEY="key";
    final String MOVIES_VIDEOS_TRAILER_NAME="name";
    final String OWM_RESULTS="results";

    private Context context;

    public Trailer_Parser(Context activity_context){
        context=activity_context;
    }

    @Override
    protected Void doInBackground(String... movie_tag) {
        BufferedReader reader=null;
        String Line=null;
        StringBuffer JSON_String=null;

        final String MOVIES_VIDEOS_BASE="https://api.themoviedb.org/3/movie";
        final String MOVIES_VIDEOS_QUERY="videos";
        final String api_key_string="api_key";

        String api_key_value=movies_api_key.API_KEY.get_API_key();

        Uri trailer_query=Uri.parse(MOVIES_VIDEOS_BASE).buildUpon().appendPath(movie_tag[0])
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


            String SelectionQuery=
                   dbContract.TRAILER_REVIEWS_TABLE.TABLE_NAME+"."+ dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_MOVIE_TAG +" = ? AND "+
                            dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_TYPE + " = ? ";

            String[] Selectionargs=
                            new String[]{movie_tag[0], dbContract.DETAILS_TYPE_TRAILER};

                    Cursor cur=context.getContentResolver().query(
                    dbContract.TRAILER_REVIEWS_TABLE.CONTENT_URI,
                    dbContract.TRAILER_REVIEW_PROJECTION,
                    SelectionQuery,
                    Selectionargs,
                    null
            );
/*
            if(cur.moveToFirst()){
                do {
                    Log.i("Trailer Avail :",cur.getString(cur.getColumnIndex(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_ITEM_ID)));
                }while (cur.moveToNext());
            }*/

            if(!cur.moveToFirst()){
                for(int index=0;index<Trailer_array.length();index++){

                    JSONObject mTrailer=Trailer_array.getJSONObject(index);
                    String trailer_id=mTrailer.getString(MOVIES_VIDEOS_TRAILER_ID);
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_MOVIE_TAG,movie_tag[0]);
                    contentValues.put(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_ITEM_ID,mTrailer.getString(MOVIES_VIDEOS_TRAILER_ID));
                    contentValues.put(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_CONTENT, mTrailer.getString(MOVIES_VIDEOS_TRAILER_KEY));
                    contentValues.put(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_TYPE,dbContract.DETAILS_TYPE_TRAILER);
                Uri entry=context.getContentResolver().insert(dbContract.TRAILER_REVIEWS_TABLE.CONTENT_URI, contentValues);

//                Log.i("Inserted trailers", entry.toString());
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
