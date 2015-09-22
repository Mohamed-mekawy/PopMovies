package com.example.mekawy.popmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mekawy.popmovies.Data.DB_Contract;
import com.example.mekawy.popmovies.Data.Db_OpenHelper;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dummy test
        SQLiteDatabase db;
        Db_OpenHelper temp=new Db_OpenHelper(getApplicationContext());
        db=temp.getWritableDatabase();
        ContentValues x=new ContentValues();

        String title="movie1";
        int tag=658531;
        String ov="not overview";
        String poster="path of poster";
        int fav=1;// movie in fav list
        String date="2015";
        double avg=7.8;

        x.put(DB_Contract.MOVIES_TABLE.OWM_COLUMN_TAG,tag);
        x.put(DB_Contract.MOVIES_TABLE.OWM_COLUMN_TITLE,title);
        x.put(DB_Contract.MOVIES_TABLE.OWM_COLUMN_OVERVIEW,ov);
        x.put(DB_Contract.MOVIES_TABLE.OWM_COLUMN_POSTER_PATH,poster);
        x.put(DB_Contract.MOVIES_TABLE.OWM_COLUMN_IS_FAVORITE, fav);
        x.put(DB_Contract.MOVIES_TABLE.OWM_COLUMN_RELEASE_DATE,date);
        x.put(DB_Contract.MOVIES_TABLE.OWM_COLUMN_VOTE_AVERAGE, avg);

        Long retLong=db.insert(DB_Contract.MOVIES_TABLE.TABLE_NAME,null,x);

        if(retLong!=-1 ) Log.i("TestDB",Long.toString(retLong));



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent resolver=new Intent(getApplicationContext(),Setting_Activity.class);
            startActivity(resolver);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
