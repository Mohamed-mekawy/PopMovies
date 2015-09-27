package com.example.mekawy.popmovies;


import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;


import com.example.mekawy.popmovies.Data.dbContract;
import com.example.mekawy.popmovies.Data.dbContract.POP_MOVIES_TABLE;
import com.example.mekawy.popmovies.Data.dbContract.MOST_VOTED_TABLE;

public class MainActivityFragment extends Fragment {

    private GridView Image_Grid_View;
    private static MovieAdapter mAdapter;
    private static final int Image_Loader=0;

    //projection for tables

    private final static String []POP_TABLE_PROJECTION ={
            POP_MOVIES_TABLE.TABLE_NAME+"."+ POP_MOVIES_TABLE._ID,
            POP_MOVIES_TABLE.OWM_COLUMN_POSTER_PATH,
            dbContract.OWM_COMMON_COLUMN_TAG,
            dbContract.OWM_COMMON_COLUMN_TITLE,
            dbContract.OWM_COMMON_COLUMN_OVERVIEW,
            dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,
            dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE,
            dbContract.OWM_COMMON_COLUMN_IS_FAVORITE
    };

    private final static String []VOTE_TABLE_PROJECTION ={
            MOST_VOTED_TABLE.TABLE_NAME+"."+ POP_MOVIES_TABLE._ID,
            dbContract.OWM_COMMON_COLUMN_TAG,
            dbContract.OWM_COMMON_COLUMN_TITLE,
            dbContract.OWM_COMMON_COLUMN_OVERVIEW,
            dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,
            dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE,
            dbContract.OWM_COMMON_COLUMN_IS_FAVORITE
    };



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
//        getLoaderManager().initLoader(Image_Loader,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.movies_grid, container, false);
        Image_Grid_View=(GridView) rootview.findViewById(R.id.movies_grid);

        Cursor cr=getActivity().getContentResolver().query(
                POP_MOVIES_TABLE.CONTENT_URI,
                POP_TABLE_PROJECTION
                ,null,
                null,
                null);

        mAdapter=new MovieAdapter(getActivity(),cr,0);

        Image_Grid_View.setAdapter(mAdapter);

        return rootview;
    }


}
