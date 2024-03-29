package com.example.mekawy.popmovies;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MovieDetailedActivity extends ActionBarActivity {


    /*in case of device is Phone this Activity will be responsible for create the fragment */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if(savedInstanceState==null){
            Bundle movie_bundle=new Bundle();
            movie_bundle.putParcelable(MovieDetailedFragment.MOVIE_BUNDLE_TAG, getIntent().getData());

            MovieDetailedFragment mFragment=new MovieDetailedFragment();
            mFragment.setArguments(movie_bundle);

            getSupportFragmentManager().beginTransaction().
                    add(R.id.movie_container,
                            mFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_, menu);
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
            startActivity(new Intent(getApplicationContext(),Setting_Activity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
