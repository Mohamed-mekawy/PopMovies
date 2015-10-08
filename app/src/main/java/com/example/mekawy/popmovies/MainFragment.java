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


import com.example.mekawy.popmovies.Data.MoviesContract;
import com.example.mekawy.popmovies.Data.MoviesContract.POP_MOVIES_TABLE;
import com.example.mekawy.popmovies.Data.MoviesContract.VOTE_DESC_TABLE;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private GridView Image_Grid_View;
    private static MovieAdapter mAdapter;
    private static final int Image_Loader=0;

    private int Selected_position=GridView.INVALID_POSITION;
    private static final String Selected_position_key="Selected_position";
    boolean RESET_POSITION_FLAG=false;

    public static String BEST_FIT_IMAGE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BEST_FIT_IMAGE=Utility.getBestFitLink(getActivity());
    }

    public MainFragment() {
        setHasOptionsMenu(true);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the Selected item position to back to this position again
        outState.putInt(Selected_position_key, Selected_position);
    }

    //restart Loader upon change in sort type
    public void restartLoader(){
        getLoaderManager().restartLoader(Image_Loader, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetch_new_data();
    }

    //fetch the data only if sort method is pop/vote
    public void fetch_new_data(){
        if(!Utility.getsortmethod(getActivity()).equals(getString(R.string.sort_fav))) {
            Fetch_Task newFetchtask = new Fetch_Task(getActivity());
            newFetchtask.execute(movies_api_key.API_KEY.get_API_key());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(Image_Loader, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void set_Grid_Col(){
        int Orient=Utility.getCurrentOrientation(getActivity());
        if(Orient==0)Image_Grid_View.setNumColumns(2); // if Portrait mode set to 2 horizontal postrs
        else if(Orient==1)Image_Grid_View.setNumColumns(3); // if landscape mode set to 3 horizontal posters
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.movies_grid, container, false);
        Image_Grid_View=(GridView) rootview.findViewById(R.id.movies_grid);
        set_Grid_Col();
//        get instance of Adapter
        mAdapter=new MovieAdapter(getActivity(),null,0);

        Image_Grid_View.setAdapter(mAdapter);

        Image_Grid_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cr = (Cursor) adapterView.getItemAtPosition(position);

                if (cr != null) {
                    String table_name = Utility.get_table_name(getActivity());
                    Uri passed_uri = null;
                    int selected_tag = 0;

                    if (table_name.equals(POP_MOVIES_TABLE.TABLE_NAME)) {
                        selected_tag = cr.getInt(cr.getColumnIndex(POP_MOVIES_TABLE.OWM_COLUMN_TAG));
                        passed_uri = POP_MOVIES_TABLE.buildUriwithtag(selected_tag);

                    } else if (table_name.equals(VOTE_DESC_TABLE.TABLE_NAME)) {
                        selected_tag = cr.getInt(cr.getColumnIndex(VOTE_DESC_TABLE.OWM_COLUMN_TAG));
                        passed_uri = VOTE_DESC_TABLE.buildUriwithtag(selected_tag);
                    }

                    else if(table_name.equals(MoviesContract.FAV_MOVIES_TABLE.TABLE_NAME)){
                        selected_tag = cr.getInt(cr.getColumnIndex(MoviesContract.FAV_MOVIES_TABLE.OWM_COLUMN_TAG));
                        passed_uri = MoviesContract.FAV_MOVIES_TABLE.builUriwithtag(selected_tag);
                    }
                    //save the Selected psition
                    Selected_position=position;
//                  use interface to start fragment MovieDetailsFragment , depends on current mode
                    ((movie_Callback) getActivity()).onMovieSelected(passed_uri);
                }
            }
        });

        if(savedInstanceState!=null && savedInstanceState.containsKey(Selected_position_key)) {
            Selected_position = savedInstanceState.getInt(Selected_position_key);
//            Log.i("LastSelection",Integer.toString(Selected_position));
        }

        return rootview;
    }

    public interface movie_Callback{
        public void  onMovieSelected(Uri movie_uri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri load_uri=Utility.get_content_uri(getActivity());
            return new CursorLoader(
                    getActivity(),
                    load_uri,
                    MoviesContract.COMMON_SORT_PROJECTION,
                    null, null,
                    null
            );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        /* swap the Adapter with new Cursor data*/
        if (data.moveToFirst())
            mAdapter.swapCursor(data);
        else if(!data.moveToFirst())
            mAdapter.swapCursor(null);

        if (Selected_position!=GridView.INVALID_POSITION){
            Image_Grid_View.smoothScrollToPosition(Selected_position);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
