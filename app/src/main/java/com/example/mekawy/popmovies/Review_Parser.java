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

public class Review_Parser extends AsyncTask<String,Void,Void> {

    final String OWM_ID="id";
    final String OWM_RESULTS="results";
    final String OWM_AUTHOR="author";
    final String OWM_CONTENT="content";

    private Context context;

    public Review_Parser(Context Parent_Context){
        context=Parent_Context;
    }

    @Override
    protected Void doInBackground(String... movie_tag) {
        BufferedReader reader=null;
        String Line=null;
        StringBuffer JSON_String=null;

        final String URI_BASE="https://api.themoviedb.org/3/movie";
        final String URI_QUERY="reviews";
        final String api_key_string="api_key";

        String api_key_value=movies_api_key.API_KEY.get_API_key();

        Uri Review_query=Uri.parse(URI_BASE).buildUpon().appendPath(movie_tag[0])
                .appendPath(URI_QUERY).appendQueryParameter(api_key_string,api_key_value).build();

        try {
            URL url=new URL(Review_query.toString());
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream input_reader=connection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(input_reader));
            JSON_String=new StringBuffer();
            while ((Line=reader.readLine())!=null)JSON_String.append(Line);

            JSONObject Review_Object=new JSONObject(JSON_String.toString());
            JSONArray Reviews_array=Review_Object.getJSONArray(OWM_RESULTS);


            String SelectionQuery=
                    dbContract.TRAILER_REVIEWS_TABLE.TABLE_NAME+"."+ dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_MOVIE_TAG +" = ? AND "+
                            dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_TYPE + " = ? ";

            String[] Selectionargs=
                    new String[]{movie_tag[0], dbContract.DETAILS_TYPE_REVIEWS};

            Cursor cur=context.getContentResolver().query(
                    dbContract.TRAILER_REVIEWS_TABLE.CONTENT_URI,
                    dbContract.TRAILER_REVIEW_PROJECTION,
                    SelectionQuery,
                    Selectionargs,
                    null
            );

            if(cur.moveToFirst()){
                do {
                    Log.i("REVIEW Avail :",cur.getString(cur.getColumnIndex(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_ITEM_ID)));
                }while (cur.moveToNext());
            }

            if(!cur.moveToFirst()){

                for(int index=0;index<Reviews_array.length();index++){
                    JSONObject mReview=Reviews_array.getJSONObject(index);

                    String Review_id=mReview.getString(OWM_ID);

                    ContentValues contentValues=new ContentValues();

                    contentValues.put(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_MOVIE_TAG,movie_tag[0]);
                    contentValues.put(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_ITEM_ID,mReview.getString(OWM_ID));
//                    contentValues.put(dbContract.MOVIES_REVIEWS_TABLE.OWM_COLUMN_REVIEW_AUTHOR, mReview.getString(OWM_AUTHOR));
                    contentValues.put(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_CONTENT, mReview.getString(OWM_CONTENT));
                    contentValues.put(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_TYPE,dbContract.DETAILS_TYPE_REVIEWS);

                    Uri entry=context.getContentResolver().insert(dbContract.TRAILER_REVIEWS_TABLE.CONTENT_URI, contentValues);

                    Log.i("Inserted Review", entry.toString());
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }




        return null;
    }






}
