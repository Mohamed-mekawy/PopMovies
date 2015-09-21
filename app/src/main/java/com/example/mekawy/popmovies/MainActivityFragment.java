package com.example.mekawy.popmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.HashMap;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridView Image_Grid_View;

    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.movies_grid, container, false);
        Image_Grid_View=(GridView) rootview.findViewById(R.id.movies_grid);
        Grid_ImageAdapter movies_adapter=new Grid_ImageAdapter(getActivity());
        Image_Grid_View.setAdapter(movies_adapter);

        Fetch_Task newFetchtask=new Fetch_Task(getActivity());
        newFetchtask.execute(movies_api_key.API_KEY.get_API_key());

        return rootview;
    }
}
