package com.example.mekawy.popmovies;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import com.example.mekawy.popmovies.Data.dbContract;

public class Movie_ActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final static String []POP_TABLE_PROJECTION ={
            dbContract.POP_MOVIES_TABLE.TABLE_NAME+"."+ dbContract.POP_MOVIES_TABLE._ID,
            dbContract.OWM_COMMON_COLUMN_TAG,
            dbContract.OWM_COMMON_COLUMN_TITLE,
            dbContract.OWM_COMMON_COLUMN_OVERVIEW,
            dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,
            dbContract.OWM_COMMON_POSTER_PATH,
            dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE,
            dbContract.OWM_COMMON_COLUMN_IS_FAVORITE
    };


    private final static String []VOTE_TABLE_PROJECTION ={
            dbContract.MOST_VOTED_TABLE.TABLE_NAME+"."+ dbContract.MOST_VOTED_TABLE._ID,
            dbContract.OWM_COMMON_COLUMN_TAG,
            dbContract.OWM_COMMON_COLUMN_TITLE,
            dbContract.OWM_COMMON_COLUMN_OVERVIEW,
            dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,
            dbContract.OWM_COMMON_POSTER_PATH,
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



    private int resize_width;
    private int resize_hight;
    private String http_width;
    private String IMAGE_BASE="http://image.tmdb.org/t/p/w";
    private static final int LOADER_ID=1;

    private TextView movie_title;
    private ImageView movie_poster;
    private TextView Release_date;
    private TextView movie_rating;
    private TextView Describtion;

    public Movie_ActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String,Integer> Dimen=Utility.Get_Prefered_Dimension(getActivity());
        resize_width=Dimen.get("resize_width");
        resize_hight=Dimen.get("resize_hight");
        http_width=Integer.toString(Dimen.get("Http_width"));
        IMAGE_BASE+=http_width;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_movie_, container, false);

        movie_title=(TextView) rootview.findViewById(R.id.detail_title);
        movie_poster=(ImageView) rootview.findViewById(R.id.detail_thumb_image);
        Release_date=(TextView) rootview.findViewById(R.id.detail_date);
        movie_rating=(TextView) rootview.findViewById(R.id.detail_rate);
        Describtion=(TextView) rootview.findViewById(R.id.detail_desc);


        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent rec_intent=getActivity().getIntent();
        String table_name;
        if(rec_intent!=null) {
            Uri rec_Uri = rec_intent.getData();
            Log.i("sad", rec_Uri.toString());
            table_name=rec_Uri.getPathSegments().get(0);


            if(table_name.equals(dbContract.POP_MOVIES_TABLE.TABLE_NAME))
            return new CursorLoader(getActivity(),
                    rec_Uri,
                    POP_TABLE_PROJECTION,
                    null,
                    null,
                    null);

            else if (table_name.equals(dbContract.MOST_VOTED_TABLE.TABLE_NAME))

                return new CursorLoader(getActivity(),
                        rec_Uri,
                        VOTE_TABLE_PROJECTION,
                        null,
                        null,
                        null);



            else return null;
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("onfinish", "finished");

        if(data.moveToFirst()){
            movie_title.setText(data.getString(TITLE_COULMN));

            Picasso.with(getActivity()).
                    load(IMAGE_BASE + data.getString(POSTER_COULMN)).
                    resize(resize_width,resize_hight).
                    into(movie_poster);

            Release_date.setText(data.getString(DATE_COULMN));
            movie_rating.setText(data.getString(AVG_COULMN));
            //text view error viewing full text
            Describtion.setText(data.getString(OVERVIEW_COULMN));
            Log.i("Cdata",data.getString(OVERVIEW_COULMN));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
