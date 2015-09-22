package com.example.mekawy.popmovies;

import android.content.Intent;
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


/**
 * A placeholder fragment containing a simple view.
 */
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
        Intent mIntent =getActivity().getIntent();
        HashMap<String,String> received_data= (HashMap<String, String>) mIntent.getSerializableExtra("movie_data");

        movie_title=(TextView) rootview.findViewById(R.id.detail_title);
        movie_poster=(ImageView) rootview.findViewById(R.id.detail_thumb_image);
        Release_date=(TextView) rootview.findViewById(R.id.detail_date);
        movie_rating=(TextView) rootview.findViewById(R.id.detail_rate);
        Describtion=(TextView) rootview.findViewById(R.id.detail_desc);

        movie_title.setText(received_data.get("Title"));

        Picasso.with(getActivity()).
                load(IMAGE_BASE + received_data.get("Image")).
                resize(resize_width, resize_hight).
                into(movie_poster);

        Release_date.setText(received_data.get("Date"));
        movie_rating.setText(received_data.get("Rate"));
        Describtion.setText(received_data.get("Desc"));
        return rootview;
    }

}
