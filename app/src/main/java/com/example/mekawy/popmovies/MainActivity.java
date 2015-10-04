package com.example.mekawy.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mekawy.popmovies.Data.dbContract;

public class MainActivity extends ActionBarActivity implements MainFragment.movie_Callback{

    private final static String MOVIE_FRAG_TAG ="MFTAG";
    public static boolean double_pane;
    private String Sorted_by;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get the initail sort mode
        Sorted_by = Utility.getsortmethod(this);

        if (findViewById(R.id.movie_container) != null) {
            if (savedInstanceState == null) {
                Movie_Fragment mf = new Movie_Fragment();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.movie_container,
                                mf,
                                MOVIE_FRAG_TAG).commit();
            }
            double_pane = true;
        } else double_pane = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String Current_Sort_method= PreferenceManager.getDefaultSharedPreferences(this).
                getString(this.getString(R.string.setting_sort_key), this.getString(R.string.sort_popularity_desc));

        Log.i("mydata1",Current_Sort_method);
        Log.i("mydata2",Sorted_by);

        if(Current_Sort_method!=null && !Sorted_by.equals(Current_Sort_method)){
            MainFragment mf=(MainFragment) getSupportFragmentManager().findFragmentById(R.id.movies_grid);
           if(mf!=null) mf.update_Ui();
        }


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

    @Override
    public void onMovieSelected(Uri movie_uri) {

        if(double_pane){
        // make new Bundle and put Uri as parcable form , then set Argument of the new Instance to that Bundle
        // and Replace the current one
        Bundle Double_Pane_Bundle=new Bundle();
        Double_Pane_Bundle.putParcelable(Movie_Fragment.MOVIE_BUNDLE_TAG, movie_uri);
        Movie_Fragment new_movieFragment=new Movie_Fragment();
        new_movieFragment.setArguments(Double_Pane_Bundle);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.movie_container,
                        new_movieFragment,
                        MOVIE_FRAG_TAG).commit();
        }

        else if(!double_pane){
            startActivity(new Intent(this,Movie_Activity.class).setData(movie_uri));
        }


    }
}
