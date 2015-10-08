package com.example.mekawy.popmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mekawy.popmovies.Data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MovieDetailedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    /*
    * this fragment shows the details of selected movie from MainFragment , this details include the info from
    * this movie table and the Trailers , Reviews if available from trailer_reviews_table
    */

    //tag used by bundle to fetch Uri of selected movie
    public final static String MOVIE_BUNDLE_TAG="mTag";
    private Uri mUri=null;

    // column index used by cursor instead of finding column by name
    static final int TITLE_COULMN=2;
    static final int OVERVIEW_COULMN=3;
    static final int DATE_COULMN=4;
    static final int POSTER_COULMN=5;
    static final int AVG_COULMN=6;



    private int resize_width;
    private int resize_hight;
    private String http_width;
    private String IMAGE_BASE="http://image.tmdb.org/t/p/w"+MainFragment.BEST_FIT_IMAGE;

    /*Loader used to Load data to the Fragment*/

    /*
    Basic Loader used to load the basic movie info from the movie table (pop/vote/fav)
    */
    private static final int MOVIE_BASIC_LOADER =1;

    /* MOVIE_ISFAV_LOADER used to check is the movie in favorite table or not,it's used only if current sort method is not
    by favorite saved movies
    */
    private static final int MOVIE_ISFAV_LOADER =3;

    /*TRAILER_REVIEW Loader will be used to fetch current movie Reviews and Trailers */
    private static final int TRAILER_REVIEW=5;

    // Hold Cursor for the Current Movie
    private Cursor Current_movie_Cursor;

    private MovieDetails_Adapter movieAdapter;
    //the main listview of fragment
    private ListView movielv;


    private View Header_layout;
    private ViewHolder Header_viewHolder;


    public static int Current_type=-1;
    Cursor statsh;

    public MovieDetailedFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String,Integer> Dimen=Utility.Get_Prefered_Dimension(getActivity());
        resize_width=Dimen.get("resize_width");
        resize_hight=Dimen.get("resize_hight");
    }

    /* View holder for Header of listview which display data stored in movie table*/
    public static class ViewHolder{

        public final TextView movie_title;
        public final ImageView movie_poster;
        public final TextView Release_date;
        public final TextView movie_rating;
        public final TextView Describtion;
        public final ImageView isFavimage;
        public final TextView isFavtext;

        public ViewHolder(View rootview){
            movie_title=(TextView) rootview.findViewById(R.id.detail_title);
            movie_poster=(ImageView) rootview.findViewById(R.id.detail_thumb_image);
            Release_date=(TextView) rootview.findViewById(R.id.detail_date);
            movie_rating=(TextView) rootview.findViewById(R.id.detail_rate);
            Describtion=(TextView) rootview.findViewById(R.id.detail_desc);
            isFavimage=(ImageView) rootview.findViewById(R.id.Movie_fav_icon);
            isFavtext=(TextView) rootview.findViewById(R.id.Movie_fav_Text);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Receive Bundle parcable data from bundle , and assign the mURI as Uri for current movie table appended with tag
        Bundle movie_arguments=getArguments();
        if(movie_arguments!=null){
            mUri=movie_arguments.getParcelable(MOVIE_BUNDLE_TAG);
//            Log.i("rec_uri",mUri.toString());
        }

        View rootview= inflater.inflate(R.layout.movie_fragment, container, false);
        // assign the movie_details layout as the the header of listview
        Header_layout =(View) getActivity().getLayoutInflater().inflate(R.layout.movie_details, null);
        Header_viewHolder =new ViewHolder(Header_layout);
        // set tag of current ViewHolder to easily get view ref again anywhere
        Header_layout.setTag(Header_viewHolder);

        movielv=(ListView) rootview.findViewById(R.id.movie_details_lv);
        movielv.addHeaderView(Header_layout,null,false);
        //define instance of Cursor adapter which will show the trailers and reviews
        movieAdapter=new MovieDetails_Adapter(getActivity(),null,0);
        movielv.setAdapter(movieAdapter);
        update_Reviews_Trailers();


        /* the listview on click Listener implemented to only notify that user tap on youtube Link ,
        * so it fisrt detect type of item if it Trailer or not and then Build the Uri by getting key from coresspond Cursor ,
        * and make implicit intent to play Trailer
        */

        movielv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int Cursor_type=movieAdapter.getItemViewType(position-1);
                    if(Cursor_type==0){ //youtube Link
                        Cursor cursor=(Cursor) movieAdapter.getItem(position-1);
                        Build_youtube_Link(cursor.getString(cursor.getColumnIndex(MoviesContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_CONTENT)));
                    }
            }
        });


        // Sshow diff. icon in case if the Movie in fav sort
        if(mUri!=null) {
            if(mUri.getPathSegments().get(0).equals(MoviesContract.FAV_MOVIES_TABLE.TABLE_NAME)){
            Header_viewHolder.isFavimage.setImageResource(R.drawable.favdelete);
            Header_viewHolder.isFavtext.setText(" Remove form favorite List ");
            }
        }

        // Change the state of favorite movies
        Header_viewHolder.isFavimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String table = mUri.getPathSegments().get(0);

                if (!table.equals(MoviesContract.FAV_MOVIES_TABLE.TABLE_NAME)) {
                    Uri Favorite_Uri = MoviesContract.FAV_MOVIES_TABLE.CONTENT_URI.buildUpon().appendPath(mUri.getLastPathSegment()).build();
                    Cursor query_cursor =
                            getActivity().getContentResolver().query(
                                    Favorite_Uri,
                                    MoviesContract.COMMON_SORT_PROJECTION,
                                    null,
                                    null,
                                    null);
                    if (query_cursor.moveToFirst())
                        RemoveFromFavTable(Favorite_Uri);
                    else if (!query_cursor.moveToFirst())
                        AddtoFavTable(Current_movie_Cursor);
                }

                else if (table.equals(MoviesContract.FAV_MOVIES_TABLE.TABLE_NAME)) {
                    int _delete = 0;
                    _delete = getActivity().getContentResolver().delete(mUri, null, null);
//                    Log.i("Delete_fav_Record", Integer.toString(_delete));
                    Toast.makeText(getActivity(), "Movie has been Removed from Favorite List", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "You Can Back to Favorite List", Toast.LENGTH_SHORT).show();
                    /*if the current view is table , i send notification to replace current fragment , and for the main fragment
                    * it will automatically removed as the Loader keep querying the data while changing
                    * */
                    if(Utility.isTablet(getActivity()) && Utility.getCurrentOrientation(getActivity())==1){
                        ((Remove_TwoPane) getActivity()).Remove_movieFragment();
                    }
                }
            }
        }
        );
        return rootview;
    }

    /* notify the Listener "MovieDetailedFragment" that user need to remove current fav movie from List*/
    public interface Remove_TwoPane{
        public void Remove_movieFragment();
    }

    // fetch and save into db the trailers and reviews for this movies
    public void update_Reviews_Trailers(){
    if (mUri!=null) {
        Trailer_Parser mTrailer = new Trailer_Parser(getActivity());
        mTrailer.execute(mUri.getLastPathSegment());

        Review_Parser mParser = new Review_Parser(getActivity());
        mParser.execute(mUri.getLastPathSegment());
    }
    }

    //Delete the record of movie from the table
    public void RemoveFromFavTable(Uri _remove){
        int Fav_Delete = getActivity().getContentResolver().delete(_remove,null, null);
        Header_viewHolder.isFavimage.setImageResource(R.drawable.favoff);
        Header_viewHolder.isFavtext.setText(" Remove form favorite List ");
    }

    // add the Movie to fav_movies table
    public void AddtoFavTable(Cursor cur){
        Uri Fav_Uri= MoviesContract.FAV_MOVIES_TABLE.CONTENT_URI;
        ContentValues fav_record_values=new ContentValues();
        if(cur.moveToFirst()) {
            int id = cur.getInt(cur.getColumnIndex(MoviesContract.MOVIE_COMMON_COLUMN_TAG));
            String mtitle = cur.getString(cur.getColumnIndex(MoviesContract.MOVIE_COMMON_COLUMN_TITLE));
            String mOverview = cur.getString(cur.getColumnIndex(MoviesContract.MOVIE_COMMON_COLUMN_OVERVIEW));
            String mRelease_date = cur.getString(cur.getColumnIndex(MoviesContract.MOVIE_COMMON_COLUMN_RELEASE_DATE));
            String mPoster_path = cur.getString(cur.getColumnIndex(MoviesContract.MOVIE_COMMON_COLUMN_POSTER_PATH));
            double mVote_avg = cur.getDouble(cur.getColumnIndex(MoviesContract.MOVIE_COMMON_COLUMN_VOTE_AVERAGE));

            fav_record_values.put(MoviesContract.MOVIE_COMMON_COLUMN_TAG, id);
            fav_record_values.put(MoviesContract.MOVIE_COMMON_COLUMN_TITLE, mtitle);
            fav_record_values.put(MoviesContract.MOVIE_COMMON_COLUMN_OVERVIEW, mOverview);
            fav_record_values.put(MoviesContract.MOVIE_COMMON_COLUMN_RELEASE_DATE, mRelease_date);
            fav_record_values.put(MoviesContract.MOVIE_COMMON_COLUMN_POSTER_PATH, mPoster_path);
            fav_record_values.put(MoviesContract.MOVIE_COMMON_COLUMN_VOTE_AVERAGE, mVote_avg);

            Uri Fav_insert = getActivity().getContentResolver().insert(Fav_Uri, fav_record_values);
        }
    }

        /* build the youtube link and start implicit intent with view options for user in case of
        * the user has many options
        * */
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

    /* initiate the Loader to load the data */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mUri != null) {
            String Current_table = mUri.getPathSegments().get(0);
            getLoaderManager().initLoader(MOVIE_BASIC_LOADER, null, this);      // init Movie Loader
            if(!mUri.getPathSegments().get(0).equals(MoviesContract.FAV_MOVIES_TABLE.TABLE_NAME))
                getLoaderManager().initLoader(MOVIE_ISFAV_LOADER, null, this);
                getLoaderManager().initLoader(TRAILER_REVIEW,null,this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(mUri!=null) {
            switch (id){
                //querying the table_name with common projection
                case MOVIE_BASIC_LOADER:{
                        return new CursorLoader(getActivity(),
                                mUri,
                                MoviesContract.COMMON_SORT_PROJECTION,
                                null,
                                null,
                                null);
                }

                case MOVIE_ISFAV_LOADER:{
                    Uri Favorite_query= MoviesContract.FAV_MOVIES_TABLE.CONTENT_URI.buildUpon().appendPath(mUri.getLastPathSegment()).build();
                        return new CursorLoader(
                                getActivity(),
                                Favorite_query,
                                MoviesContract.COMMON_SORT_PROJECTION,
                                null,
                                null,
                                null);
                }

                //Querying for Trailers and reviews but with ordered way to view trailer first and then reviews
                case TRAILER_REVIEW:{
                    String movie_tag=mUri.getPathSegments().get(1);
                    return new CursorLoader(getActivity(),
                            MoviesContract.TRAILER_REVIEWS_TABLE.CONTENT_URI,
                            MoviesContract.TRAILER_REVIEW_PROJECTION,
                            MoviesContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_MOVIE_TAG+ " = ?",
                            new String[]{movie_tag},
                            MoviesContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_TYPE +" ASC "
                    );
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
                    //Retreive the ViewHolder of the Header and assign values of current movie saved into Current_movie_Cursor
                    Current_movie_Cursor=data;
                    ViewHolder movie_holder=(ViewHolder) Header_layout.getTag();
                    movie_holder.movie_title.setText(data.getString(TITLE_COULMN));

                    movie_holder.movie_title.setBackgroundColor(Color.GREEN);
                    String mPoster_Path=data.getString(POSTER_COULMN);

                    if(!mPoster_Path.equals("null")){
                    Picasso.with(getActivity()).
                            load(IMAGE_BASE + data.getString(POSTER_COULMN)).
                            resize(resize_width,resize_hight).
                            into(movie_holder.movie_poster);
                    }
                    else if(mPoster_Path.equals("null")){
                        Picasso.with(getActivity()).
                                load(R.drawable.noposter).
                                resize(resize_width,resize_hight).
                                into(movie_holder.movie_poster);
                    }
                    movie_holder.Release_date.setText(data.getString(DATE_COULMN));
                    movie_holder.movie_rating.setText(data.getString(AVG_COULMN) + "/10");
                    movie_holder.Describtion.setText(data.getString(OVERVIEW_COULMN));
                }
                break;
            }

            case MOVIE_ISFAV_LOADER:{
                if(data.moveToFirst()) {
                        Header_viewHolder.isFavimage.setImageResource(R.drawable.favon);
                        Header_viewHolder.isFavtext.setText("Remove from Favorite List");
                }

                else if(!data.moveToFirst()) {
                    Header_viewHolder.isFavimage.setImageResource(R.drawable.favoff);
                    Header_viewHolder.isFavtext.setText("Add to Favorite List");
                }
                break;
            }

            case TRAILER_REVIEW:
            {
               movieAdapter.swapCursor(data);
                break;
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
