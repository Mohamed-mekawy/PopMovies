package com.example.mekawy.popmovies;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivityFragment extends Fragment {

    private GridView Image_Grid_View;
    private Grid_ImageAdapter mAdapter;


    public MainActivityFragment() {
        setHasOptionsMenu(true);
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
            //No Data at beginig
            final ArrayList<Movie_object> movies_list=new ArrayList<Movie_object>();
            mAdapter=new Grid_ImageAdapter(getActivity(),movies_list);
            Image_Grid_View.setAdapter(mAdapter);

        Image_Grid_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent mIntent=new Intent(getActivity(),Movie_Activity.class);

                HashMap<String,String> passed_val=new HashMap<String, String>();

                passed_val.put("Title",movies_list.get(i).get_Original_title());
                passed_val.put("Image",movies_list.get(i).get_Grid_Poster());
                passed_val.put("Date",movies_list.get(i).get_Release_date());
                passed_val.put("Rate", Double.toString(movies_list.get(i).get_Rating()) + "/10");
                passed_val.put("Desc",movies_list.get(i).get_Overview());

                mIntent.putExtra("movie_data",passed_val);

                startActivity(mIntent);
            }
        });

        return rootview;
    }
}
