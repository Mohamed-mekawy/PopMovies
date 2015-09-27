package com.example.mekawy.popmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivityFragment extends Fragment {

    private GridView Image_Grid_View;
    private MovieAdapter mAdapter;

    private static final int Image_Loader=0;

    public MainActivityFragment() {

        setHasOptionsMenu(true);
    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Fetch_Task newFetchtask=new Fetch_Task(getActivity(),mAdapter);
//        newFetchtask.execute(movies_api_key.API_KEY.get_API_key());
//    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview= inflater.inflate(R.layout.movies_grid, container, false);

           Image_Grid_View=(GridView) rootview.findViewById(R.id.movies_grid);


           // mAdapter=new MovieAdapter(getActivity(),movies_records);

            Image_Grid_View.setAdapter(mAdapter);

        Image_Grid_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });

        return rootview;
    }
}
