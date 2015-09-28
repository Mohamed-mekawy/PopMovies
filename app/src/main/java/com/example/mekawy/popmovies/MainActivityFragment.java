package com.example.mekawy.popmovies;


import android.content.Intent;
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

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

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
            dbContract.OWM_COMMON_POSTER_PATH,
            dbContract.OWM_COMMON_COLUMN_TAG,
            dbContract.OWM_COMMON_COLUMN_TITLE,
            dbContract.OWM_COMMON_COLUMN_OVERVIEW,
            dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,
            dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE,
            dbContract.OWM_COMMON_COLUMN_IS_FAVORITE
    };


    static final int _ID_COULMN=0;
    static final int TAG_COULMN=1;
    static final int TITLE_COULMN=2;
    static final int OVERVIEW_COULMN=3;
    static final int DATE_COULMN=4;
    static final int POSTER_COULMN=5;
    static final int AVG_COULMN=6;
    static final int ISFAV_COULMN=7;




    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Fetch_Task newFetchtask=new Fetch_Task(getActivity());
        newFetchtask.execute(movies_api_key.API_KEY.get_API_key());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(Image_Loader,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.movies_grid, container, false);
        Image_Grid_View=(GridView) rootview.findViewById(R.id.movies_grid);
        mAdapter=new MovieAdapter(getActivity(),null,0);
        Image_Grid_View.setAdapter(mAdapter);

        Image_Grid_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cr=(Cursor)adapterView.getItemAtPosition(position);
                if(cr!=null){
                    String table_name=Utility.get_table_name(getActivity());
                    Uri passed_uri=null;

                    if(table_name.equals(POP_MOVIES_TABLE.TABLE_NAME))
                        passed_uri=POP_MOVIES_TABLE.builUriwithtag(
                                    cr.getInt(cr.getColumnIndex(POP_MOVIES_TABLE.OWM_COLUMN_TAG)));

                    else if (table_name.equals(MOST_VOTED_TABLE.TABLE_NAME))
                        passed_uri=MOST_VOTED_TABLE.builUriwithtag(
                                cr.getInt(cr.getColumnIndex(MOST_VOTED_TABLE.OWM_COLUMN_TAG)));

                    Intent passed_intent=new Intent(getActivity(),Movie_Activity.class);
                    passed_intent.setData(passed_uri);
                    startActivity(passed_intent);
                }
            }
        });
        return rootview;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("onCreateLoader","start onCreateLoader method");

        Uri load_uri=Utility.get_content_uri(getActivity());
        String table_name=load_uri.getPathSegments().get(0).toString();

        if(table_name.equals(POP_MOVIES_TABLE.TABLE_NAME))
            return new CursorLoader(getActivity(),
                    load_uri,
                    POP_TABLE_PROJECTION,
                    null,
                    null,
                    null);

        else if(table_name.equals(MOST_VOTED_TABLE.TABLE_NAME)){
            return new CursorLoader(getActivity(),
                    load_uri,
                    VOTE_TABLE_PROJECTION,
                    null,
                    null,
                    null);
        }
        else return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
