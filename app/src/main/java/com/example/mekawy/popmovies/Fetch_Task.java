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

    private String DEBUG=this.getClass().getSimpleName();

    private Context mContext;

    /*
    * TO get the movies List i have 2 separated ASYNC TASK , one of them perform fetching Data " FETCH_TASK",
    * and the other one will parse it in json format and insert into db "PARSER_TASK" , so i can easily modify any
    * one of them upon change without depending in refactoring the whole ASYNC task , for this async task in onPost execute
    * it will send the String result to parser task, to complete the process
    * */


    public Fetch_Task(Context context){
        mContext=context;
    }


    @Override
    protected String doInBackground(String... ApiKey) {
        // doInBackground receive VALID API key to start fetching Data of movies
        BufferedReader reader=null;
        String Line=null;
        StringBuffer JSON_String=null;
        final String query_base="http://api.themoviedb.org/3/discover/movie?";
        final String sorting="sort_by";
        final String Api_key="api_key";
        final String mode=Utility.getsortmethod(mContext);

        Uri Builder=Uri.parse(query_base).buildUpon().appendQueryParameter(sorting, mode).
                appendQueryParameter(Api_key, ApiKey[0]).build();
//        Log.i(DEBUG, Builder.toString());

        try {
            URL url=new URL(Builder.toString());
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream input_reader=connection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(input_reader));
            JSON_String=new StringBuffer();
            while ((Line=reader.readLine())!=null)JSON_String.append(Line);
//            Log.i(DEBUG, JSON_String.toString());
            return JSON_String.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String string) {
        //After finish the Fetch task execute the parser task
        super.onPostExecute(string);
        Parser_Task newParser=new Parser_Task(mContext);
        newParser.execute(string);
    }
}