package com.example.mekawy.popmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridView Image_Grid_View;
    private Grid_ImageAdapter mAdapter;


    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Fetch_Task newFetchtask=new Fetch_Task(getActivity(),mAdapter);
        newFetchtask.execute(movies_api_key.API_KEY.get_API_key());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.movies_grid, container, false);
           Image_Grid_View=(GridView) rootview.findViewById(R.id.movies_grid);

            //Dummy data
            Movie_object []t=new Movie_object[2];
            t[0]=new Movie_object();
            t[1]=new Movie_object();

            ArrayList<Movie_object> p=new ArrayList<Movie_object>();
            p.add(t[0]);
            p.add(t[1]);
           mAdapter=new Grid_ImageAdapter(getActivity(),p);

           Image_Grid_View.setAdapter(mAdapter);


        return rootview;
    }
}
