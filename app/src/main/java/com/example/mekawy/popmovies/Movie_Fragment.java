package com.example.mekawy.popmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import com.example.mekawy.popmovies.Data.dbContract;

public class Movie_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

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


    private final static String[] MOVIE_VIDEOS_PROJECTION={
            dbContract.MOVIE_VIDEOS._ID,
            dbContract.MOVIE_VIDEOS.OWM_COLUMN_MOVIE_TAG,
            dbContract.MOVIE_VIDEOS.OWM_COLUMN_TRAILER_NAME,
            dbContract.MOVIE_VIDEOS.OWM_COLUMN_KEY,
    };


    //tage used by bundle to fetch Uri of selected movie
    public final static String MOVIE_BUNDLE_TAG="mTag";
    private Uri mUri=null;

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
    private String IMAGE_BASE="http://image.tmdb.org/t/p/w"+MainFragment.BEST_FIT_IMAGE;

    private static final int MOVIE_BASIC_LOADER =1;
    private static final int MOVIE_TRAILER_LOADER =2;
    private static final int MOVIE_ISFAV_LOADER =3;
    private static final int MOVIE_FAV_LOADER=4;

    private TextView movie_title;
    private ImageView movie_poster;
    private TextView Release_date;
    private TextView movie_rating;
    private TextView Describtion;

    private ListView Trailer_Listview;
    private TrailerAdapter mtrailerAdapter;

    private TextView mFavText;
    private ImageView mFavImage;
    private String table_name;


    public Movie_Fragment() {

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String,Integer> Dimen=Utility.Get_Prefered_Dimension(getActivity());
        resize_width=Dimen.get("resize_width");
        resize_hight=Dimen.get("resize_hight");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Receive Bundle parcable data from bundle
        Bundle movie_arguments=getArguments();

        if(movie_arguments!=null){
            mUri=movie_arguments.getParcelable(MOVIE_BUNDLE_TAG);
            Log.i("rec_uri",mUri.toString());
        }


        View rootview= inflater.inflate(R.layout.fragment_movie_, container, false);

        movie_title=(TextView) rootview.findViewById(R.id.detail_title);
        movie_poster=(ImageView) rootview.findViewById(R.id.detail_thumb_image);
        Release_date=(TextView) rootview.findViewById(R.id.detail_date);
        movie_rating=(TextView) rootview.findViewById(R.id.detail_rate);
        Describtion=(TextView) rootview.findViewById(R.id.detail_desc);

        Trailer_Listview=(ListView) rootview.findViewById(R.id.Trailer_listview);
        mtrailerAdapter=new TrailerAdapter(getActivity(),null,0);
        Trailer_Listview.setAdapter(mtrailerAdapter);

        mFavImage=(ImageView) rootview.findViewById(R.id.Movie_fav_icon);
        mFavText=(TextView) rootview.findViewById(R.id.Movie_fav_Text);

        Trailer_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor sel = (Cursor) adapterView.getItemAtPosition(position);
                Build_youtube_Link(sel.getString(sel.getColumnIndex(dbContract.MOVIE_VIDEOS.OWM_COLUMN_KEY)));
            }
        });


        if(mUri!=null) {
        if(mUri.getPathSegments().get(0).equals(dbContract.FAV_MOVIES_TABLE.TABLE_NAME)){
            mFavImage.setImageResource(R.drawable.favdelete);
            mFavText.setText(" Remove form favorite List ");
        }
        }

        mFavImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String table=mUri.getPathSegments().get(0);

                if(!table.equals(dbContract.FAV_MOVIES_TABLE.TABLE_NAME))
                Change_FavState();

                else {
                    int _delete=0;
                    Uri table_uri=Check_is_Avail();

                    //Record still existed in POP/VOTE Table;
                    if(table_uri!=null){
                        Uri update_isFav=table_uri.buildUpon().appendPath(mUri.getLastPathSegment()).appendEncodedPath(dbContract.OWM_COMMON_COLUMN_IS_FAVORITE).appendPath("0").build();
                        int _update=getActivity().getContentResolver().update(update_isFav,null,null,null);
                        Log.i("Update_isFAV",Integer.toString(_update));
                    }

                    _delete=getActivity().getContentResolver().delete(mUri,null,null);
                    Log.i("Delete_fav_Record",Integer.toString(_delete));

                    getFragmentManager().beginTransaction().
                            replace(R.id.movie_container,
                                    new Movie_Fragment(),
                                    MainActivity.MOVIE_FRAG_TAG).commit();
                    Toast.makeText(getActivity(), "Movie has been Removed from Favorite List", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "You Can Back to Favorite List", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootview;
    }


    public Uri Check_is_Avail(){
        Uri Founded_in=null;

        Cursor POP_cur =
                getActivity().getContentResolver().query(
                        dbContract.POP_MOVIES_TABLE.CONTENT_URI.buildUpon().appendPath(mUri.getLastPathSegment()).build(),
                        new String[]{dbContract.OWM_COMMON_COLUMN_TAG},
                        null,
                        null,
                        null
                );
        if (POP_cur.moveToFirst()) Founded_in = dbContract.POP_MOVIES_TABLE.CONTENT_URI;

        else if (!POP_cur.moveToFirst()) {
            Cursor VOTE_cur =
                    getActivity().getContentResolver().query(
                            dbContract.MOST_VOTED_TABLE.CONTENT_URI.buildUpon().appendPath(mUri.getLastPathSegment()).build(),
                            new String[]{dbContract.OWM_COMMON_COLUMN_TAG},
                            null,
                            null,
                            null
                    );
            if (VOTE_cur.moveToFirst())Founded_in = dbContract.MOST_VOTED_TABLE.CONTENT_URI;
        }
        return Founded_in;
    }







    public void Change_FavState(){

        Cursor query=
                getActivity().getContentResolver().query(
                                mUri,
                                new String[]{
                                        dbContract.OWM_COMMON_COLUMN_TAG,
                                        dbContract.OWM_COMMON_COLUMN_TITLE,
                                        dbContract.OWM_COMMON_COLUMN_OVERVIEW,
                                        dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,
                                        dbContract.OWM_COMMON_POSTER_PATH,
                                        dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE,
                                        dbContract.OWM_COMMON_COLUMN_IS_FAVORITE},
                                null,
                                null,
                                null);

        if(query.moveToFirst()){
            int Fav_state=query.getInt(query.getColumnIndex(dbContract.OWM_COMMON_COLUMN_IS_FAVORITE));
            if(Fav_state==0){
                Uri update_uri=mUri.buildUpon().appendPath(dbContract.OWM_COMMON_COLUMN_IS_FAVORITE).appendPath("1").build();
                AddtoFavTable(query);
                Log.i("update_uri_val", update_uri.toString());
                int update_result=getActivity().getContentResolver().update(update_uri, null, null, null);
                Log.i("update_result_val", Integer.toString(update_result));

                if(update_result==1) {
                    mFavImage.setImageResource(R.drawable.favon);
                    mFavText.setText("Remove from Favorite List");
                }
            }


            else if(Fav_state==1){
                Uri update_uri=mUri.buildUpon().appendPath(dbContract.OWM_COMMON_COLUMN_IS_FAVORITE).appendPath("0").build();
                RemoveFromFavTable();
                Log.i("update_uri_val", update_uri.toString());
                int update_result=getActivity().getContentResolver().update(update_uri, null, null, null);
                Log.i("update_result_val", Integer.toString(update_result));

                if(update_result==1) {
                    mFavImage.setImageResource(R.drawable.favoff);
                    mFavText.setText("Add to Favorite List");
                }
            }
        }
    }

    public void RemoveFromFavTable(){
        String movie_tag=mUri.getLastPathSegment();
        Uri Fav_Uri= dbContract.FAV_MOVIES_TABLE.CONTENT_URI.buildUpon().appendPath(movie_tag).build();

        int Delete_val = getActivity().getContentResolver().
                delete(Fav_Uri,null,
                null);

        Log.i("getData",Integer.toString(Delete_val));
    }


    public void AddtoFavTable(Cursor cur){

        Uri Fav_Uri= dbContract.FAV_MOVIES_TABLE.CONTENT_URI;
        ContentValues fav_record_values=new ContentValues();
        if(cur.moveToFirst()){

        int id=cur.getInt(cur.getColumnIndex(dbContract.OWM_COMMON_COLUMN_TAG));
        String mtitle=cur.getString(cur.getColumnIndex(dbContract.OWM_COMMON_COLUMN_TITLE));
        String mOverview=cur.getString(cur.getColumnIndex(dbContract.OWM_COMMON_COLUMN_OVERVIEW));
        String mRelease_date=cur.getString(cur.getColumnIndex(dbContract.OWM_COMMON_COLUMN_RELEASE_DATE));
        String mPoster_path=cur.getString(cur.getColumnIndex(dbContract.OWM_COMMON_POSTER_PATH));
        double mVote_avg=cur.getDouble(cur.getColumnIndex(dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE));

        fav_record_values.put(dbContract.OWM_COMMON_COLUMN_TAG,id);
        fav_record_values.put(dbContract.OWM_COMMON_COLUMN_TITLE,mtitle);
        fav_record_values.put(dbContract.OWM_COMMON_COLUMN_OVERVIEW,mOverview);
        fav_record_values.put(dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,mRelease_date);
        fav_record_values.put(dbContract.OWM_COMMON_POSTER_PATH,mPoster_path);
        fav_record_values.put(dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE,mVote_avg);

        Uri as=getActivity().getContentResolver().insert(Fav_Uri,fav_record_values);
            Log.i("getData",as.toString());
        }

    }


    public void Build_youtube_Link(String link_key){
        String Link_Base="https://www.youtube.com/watch";
        String Link_query="v";
        Uri link_Builder=Uri.parse(Link_Base).buildUpon().appendQueryParameter(Link_query,link_key).build();
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setData(link_Builder);
        if(intent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(intent);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mUri != null) {
            String Current_table = mUri.getPathSegments().get(0);
            getLoaderManager().initLoader(MOVIE_BASIC_LOADER, null, this);               // init Movie Loader
            getLoaderManager().initLoader(MOVIE_TRAILER_LOADER, null, this);    // init Trailer Loader
            if (!Current_table.equals(dbContract.FAV_MOVIES_TABLE.TABLE_NAME)) {
                getLoaderManager().initLoader(MOVIE_ISFAV_LOADER, null, this);
            } else if (Current_table.equals(dbContract.FAV_MOVIES_TABLE.TABLE_NAME)) {
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(mUri!=null) {
            table_name = mUri.getPathSegments().get(0);
            String Projection[]=null;

            if(table_name.equals(dbContract.FAV_MOVIES_TABLE.TABLE_NAME)) {
                Projection=new String[dbContract.FAVORITE_PROJECTION.length];
                Projection = dbContract.FAVORITE_PROJECTION;
            }

           else if(!table_name.equals(dbContract.FAV_MOVIES_TABLE.TABLE_NAME)) {
                Projection=new String[dbContract.COMMON_PROJECTION.length];
                Projection = dbContract.COMMON_PROJECTION;
            }

            switch (id){

                case MOVIE_BASIC_LOADER:{
                        return new CursorLoader(getActivity(),
                                mUri,
                                Projection,
                                null,
                                null,
                                null);
                }

                case MOVIE_TRAILER_LOADER:{
                    String movie_tag=mUri.getPathSegments().get(1);
                    return new CursorLoader(getActivity(),
                            dbContract.MOVIE_VIDEOS.CONTENT_URI,
                            MOVIE_VIDEOS_PROJECTION,
                            dbContract.MOVIE_VIDEOS.OWM_COLUMN_MOVIE_TAG + " = ?",
                            new String[]{movie_tag},
                            null
                    );
                }

                case MOVIE_ISFAV_LOADER:{

                        return new CursorLoader(
                                getActivity(),
                                mUri,
                                new String[]{dbContract.OWM_COMMON_COLUMN_IS_FAVORITE},
                                null,
                                null,
                                null);
                }



            }
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id=loader.getId();
        switch (id){

            case MOVIE_BASIC_LOADER:{
                if(data.moveToFirst()){
                    Log.i("mine record","record founded");
                    movie_title.setText(data.getString(TITLE_COULMN));
                    movie_title.setBackgroundColor(Color.GREEN);
                    Picasso.with(getActivity()).
                            load(IMAGE_BASE + data.getString(POSTER_COULMN)).
                            resize(resize_width,resize_hight).
                            into(movie_poster);
                    Release_date.setText(data.getString(DATE_COULMN));
                    movie_rating.setText(data.getString(AVG_COULMN) + "/10");
                    Describtion.setText(data.getString(OVERVIEW_COULMN));
                }
                break;
            }

            case MOVIE_ISFAV_LOADER:{
                if(data.moveToFirst()) {
                    int Fav_state=data.getInt(data.getColumnIndex(dbContract.OWM_COMMON_COLUMN_IS_FAVORITE));
                    if(Fav_state==0){
                        mFavImage.setImageResource(R.drawable.favoff);
                        mFavText.setText("Add to Favorite List");
                    }
                    else if(Fav_state==1){
                        mFavImage.setImageResource(R.drawable.favon);
                        mFavText.setText("Remove from Favorite List");
                    }
                }
                break;
            }

            case MOVIE_TRAILER_LOADER:{
                mtrailerAdapter.swapCursor(data);
                break;
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
