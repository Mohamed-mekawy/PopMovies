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

public class Fetch_Task extends AsyncTask<String,Void,String>{

    Context mContext;
    public  Fetch_Task(Context context){
        mContext=context;
    }


    @Override
    protected String doInBackground(String... strings) {

        BufferedReader reader=null;
        String Line=null;
        StringBuffer JSON_String=null;
        final String query_base="http://api.themoviedb.org/3/discover/movie?";
        final String sorting="sort_by";
        final String mode=Utility.getsortmethod(mContext);
        final String Api_key="api_key";

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
