package com.example.mekawy.popmovies;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Fetch_Task extends AsyncTask<String,Void,String>{

    private Context mContext;
    private Grid_ImageAdapter mAdapter;
    private String pushed_mode;

    public  Fetch_Task(Context context,Grid_ImageAdapter Fadapter){
        mContext=context;
        mAdapter=Fadapter;
    }


    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);

        Parser_Task newParser=new Parser_Task(mContext,mAdapter,pushed_mode);
        newParser.execute(string);
    }

    @Override
    protected String doInBackground(String... strings) {

        BufferedReader reader=null;
        String Line=null;
        StringBuffer JSON_String=null;
        final String query_base="http://api.themoviedb.org/3/discover/movie?";
        final String sorting="sort_by";
        final String Api_key="api_key";
        final String mode=Utility.getsortmethod(mContext);
        pushed_mode=mode;

        Uri Builder=Uri.parse(query_base).buildUpon().appendQueryParameter(sorting, mode).
                appendQueryParameter(Api_key, strings[0]).build();
        Log.i("APPDEBUG", Builder.toString());
        try {
            URL url=new URL(Builder.toString());
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream input_reader=connection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(input_reader));
            JSON_String=new StringBuffer();
            while ((Line=reader.readLine())!=null)JSON_String.append(Line);
//          Log.i("APPDEBUG", JSON_String.toString());
            return JSON_String.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    }
