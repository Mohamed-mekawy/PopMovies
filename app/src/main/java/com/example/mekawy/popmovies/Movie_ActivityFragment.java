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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import com.example.mekawy.popmovies.Data.dbContract;

public class Movie_ActivityFragment extends Fragment {


    private int resize_width;
    private int resize_hight;
    private String http_width;
    private String IMAGE_BASE="http://image.tmdb.org/t/p/w";


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
        String movie_tag;
        Intent mIntent =getActivity().getIntent();

        movie_tag=(String) mIntent.getExtras().get(Intent.EXTRA_TEXT);
        Uri movie_uri=Utility.get_content_uri(getActivity());

        movie_title=(TextView) rootview.findViewById(R.id.detail_title);
        movie_poster=(ImageView) rootview.findViewById(R.id.detail_thumb_image);
        Release_date=(TextView) rootview.findViewById(R.id.detail_date);
        movie_rating=(TextView) rootview.findViewById(R.id.detail_rate);
        Describtion=(TextView) rootview.findViewById(R.id.detail_desc);

        Log.i("rec_Intent",movie_tag);

        Cursor cur=getActivity().getContentResolver().query(
                movie_uri,
                new String[]{
                        dbContract.OWM_COMMON_COLUMN_TAG,
                        dbContract.OWM_COMMON_COLUMN_TITLE,
                        dbContract.OWM_COMMON_COLUMN_OVERVIEW,
                        dbContract.OWM_COMMON_POSTER_PATH,
                        dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,
                        dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE,
                        dbContract.OWM_COMMON_COLUMN_IS_FAVORITE},
                dbContract.OWM_COMMON_COLUMN_TAG+"= ? ",
                new String[]{movie_tag},
                null
        );

        if(cur.moveToFirst()){
            movie_title.setText(cur.getString(cur.getColumnIndex(dbContract.OWM_COMMON_COLUMN_TITLE)));

            Picasso.with(getActivity()).
                    load(IMAGE_BASE + cur.getString(cur.getColumnIndex(dbContract.OWM_COMMON_POSTER_PATH))).
                    resize(resize_width, resize_hight).
                    into(movie_poster);

            Release_date.setText(cur.getString(cur.getColumnIndex(dbContract.OWM_COMMON_COLUMN_RELEASE_DATE)));
            movie_rating.setText(Double.toString(cur.getDouble(cur.getColumnIndex(dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE))));
            Describtion.setText(cur.getString(cur.getColumnIndex(dbContract.OWM_COMMON_COLUMN_OVERVIEW)));
        }

        return rootview;
    }

}
