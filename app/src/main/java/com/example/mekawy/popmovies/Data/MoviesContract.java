package com.example.mekawy.popmovies.Data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    /* CONTENT_AUTHORITY represent the Package name ,and identifier for Content provider Autority  */
    public static final String CONTENT_AUTHORITY ="com.example.mekawy.popmovies";

    /* BASE_CONTENT_URI contain the basic part of provider URI > content://com.example.mekawy.popmovies/
    *  which will be used to append PATH's which represent the actual name of DB Tables*/
    final static Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);

    /* PATH_XXX include the PATH wich will be appended to BASE_CONTENT_URI to select existing table */
    public static final String PATH_FAV_MOVIES = "fav_movies";
    public static final String PATH_POP_MOVIES = "pop_movies";
    public static final String PATH_VOTE_MOVIES = "vote_movies";
    public static final String PATH_TRAILERS_REVIEWS="movies_trailer_reviews";

    /* MOVIE_COMMON_COLUMN_XXX  this String represent the COMMON COLUMNS in the TRHEE TABLES
    * (pop_movies, fav_movies,vote_movies) all of the three tables have the same column name
    *  to easily use a unified call without depending on each table name
    *
    *  MOVIE_COMMON_COLUMN_TAG it's the movies tag which is UNIQUE such 135397
    *  MOVIE_COMMON_COLUMN_POSTER_PATH column contain the poster image of movies
    *  MOVIE_COMMON_COLUMN_TITLE column contain Movies titles
    *  MOVIE_COMMON_COLUMN_OVERVIEW column Contain movies Describtion
    *  MOVIE_COMMON_COLUMN_RELEASE_DATE Column contain the Release dates of movies
    *  MOVIE_COMMON_COLUMN_VOTE_AVERAGE Column Contain the Average Vote of movies
    */

    public static final String MOVIE_COMMON_COLUMN_TAG = "id";
    public static final String MOVIE_COMMON_COLUMN_POSTER_PATH = "poster_path";
    public static final String MOVIE_COMMON_COLUMN_TITLE = "original_title";
    public static final String MOVIE_COMMON_COLUMN_OVERVIEW = "overview";
    public static final String MOVIE_COMMON_COLUMN_RELEASE_DATE = "release_date";
    public static final String MOVIE_COMMON_COLUMN_VOTE_AVERAGE = "vote_average";

    /* COMMON_SORT_PROJECTION used as a projection for the the movies tables , it a
    * String array uses the predefined MOVIE_COMMON_COLUMN_XXX
    * */
    public final static String[] COMMON_SORT_PROJECTION ={
            "_id",
            MoviesContract.MOVIE_COMMON_COLUMN_TAG,
            MoviesContract.MOVIE_COMMON_COLUMN_TITLE,
            MoviesContract.MOVIE_COMMON_COLUMN_OVERVIEW,
            MoviesContract.MOVIE_COMMON_COLUMN_RELEASE_DATE,
            MoviesContract.MOVIE_COMMON_COLUMN_POSTER_PATH,
            MoviesContract.MOVIE_COMMON_COLUMN_VOTE_AVERAGE,
    };

    /* TRAILER_REVIEW_PROJECTION Contain the Full Projection String array of TRAILERS_REVIEW TABLE  */
    public final static String[] TRAILER_REVIEW_PROJECTION={
            TRAILER_REVIEWS_TABLE._ID,
            TRAILER_REVIEWS_TABLE.OWM_COLUMN_MOVIE_TAG,
            TRAILER_REVIEWS_TABLE.OWM_COLUMN_ITEM_ID,
            TRAILER_REVIEWS_TABLE.OWM_COLUMN_CONTENT,
            TRAILER_REVIEWS_TABLE.OWM_COLUMN_TYPE,
            TRAILER_REVIEWS_TABLE.OWM_COLUM_ITEM_NAME
};

   /* TYPE INDICATOR used by TRAILER_REVIEWS in insert in db , to identify ellement is TRAILER OR REVIEW */

    public static final int DETAILS_TYPE_TRAILER=0; // identify ellement is TRAILER
    public static final int DETAILS_TYPE_REVIEWS=1; // identify ellemnt is REVIEW

    /* INNER Class defines the contents of pop_movies */
    public static final class POP_MOVIES_TABLE implements BaseColumns {
        // the Base Content Uri of pop_movies as content://com.example.mekawy.popmovies/pop_movies
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POP_MOVIES).build();

        // identify that provider will handle single returned item
        public final static String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_POP_MOVIES;

        // identify that provider will handle multiple returned item
        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +    CONTENT_AUTHORITY   +  "/" +    PATH_POP_MOVIES;


        public static final String TABLE_NAME = "pop_movies";

        public static final String OWM_COLUMN_TAG = "id";
        public static final String OWM_COLUMN_TITLE = "original_title";
        public static final String OWM_COLUMN_OVERVIEW = "overview";
        public static final String OWM_COLUMN_RELEASE_DATE = "release_date";
        public static final String OWM_COLUMN_POSTER_PATH = "poster_path";
        public static final String OWM_COLUMN_VOTE_AVERAGE = "vote_average";

        /*
        *buildMovieUri return the Base uri for pop_movies Appended with long var which is the record number
        * of inserted movie, to notify insertion completed
        */
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /* buildUriwithtag it return the BASE URI appended with movie tag for querying the database*/
        public static Uri buildUriwithtag(int tag){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(tag)).build();
        }
    }

        /* INNER Class defines the contents of vote_movies */
    public static class VOTE_DESC_TABLE implements BaseColumns {
            // the Base Content Uri of pop_movies as content://com.example.mekawy.popmovies/vote_movies
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VOTE_MOVIES).build();
            // identify that provider will handle single returned item
        public final static String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_VOTE_MOVIES;
            // identify that provider will handle multiple returned item
        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +    CONTENT_AUTHORITY   +  "/" +    PATH_VOTE_MOVIES;

        public static final String TABLE_NAME = "vote_movies";

        public static final String OWM_COLUMN_TAG = "id";
        public static final String OWM_COLUMN_TITLE = "original_title";
        public static final String OWM_COLUMN_OVERVIEW = "overview";
        public static final String OWM_COLUMN_RELEASE_DATE = "release_date";
        public static final String OWM_COLUMN_POSTER_PATH = "poster_path";
        public static final String OWM_COLUMN_VOTE_AVERAGE = "vote_average";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildUriwithtag(int tag){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(tag)).build();
        }

    }

    /* FAV_MOVIES_TABLE Define user favorite movies list , user can tap the star icon to make movie as favorite movies
    *  it movie will be stored from pop_movies/or /vote_movies into fav_movies so the user can view this movies even if
    *  the movie it self no longer existed in pop/vote tables or to view it in offline mode,
    *  the fav_movie TABLE contain the same pop/vote_movie Columns
    * */
    public static class FAV_MOVIES_TABLE implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

        public final static String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_FAV_MOVIES;

        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +    CONTENT_AUTHORITY   +  "/" +    PATH_FAV_MOVIES;

        public static final String TABLE_NAME = "fav_movies";

        public static final String OWM_COLUMN_TAG = "id";
        public static final String OWM_COLUMN_TITLE = "original_title";
        public static final String OWM_COLUMN_OVERVIEW = "overview";
        public static final String OWM_COLUMN_RELEASE_DATE = "release_date";
        public static final String OWM_COLUMN_POSTER_PATH = "poster_path";
        public static final String OWM_COLUMN_VOTE_AVERAGE = "vote_average";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri builUriwithtag(int tag){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(tag)).build();
        }
    }

    /* TRAILER_REVIEWS_TABLE define a table for trailers and reviews together ,in previous version
    *  i have used a separated tables one for trailer and one for reviews , but for modification
    *  to easily use CursorAdapter Later to view both trailers and reviews with the same same cursor
    *  but with different type so i have combined them together with identifier column int "item_type"
    *  , so for each record the inserted text in columns depend weather it's a trailer or review
    */

    public static class TRAILER_REVIEWS_TABLE implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS_REVIEWS).build();

        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +    CONTENT_AUTHORITY   +  "/" +    PATH_TRAILERS_REVIEWS;

        public static final String TABLE_NAME = "movies_trailer_reviews";
        // define the movie tag for trailer or review
        public static final String OWM_COLUMN_MOVIE_TAG = "id";
        // item_id contain UNIQUE id for trailer or review
        public static final String OWM_COLUMN_ITEM_ID = "item_id";

        /* IN case of trailer content contain the KEY VALUE for youtube link,
        *  else in case of review content contain the review text
        * */
        public static final String OWM_COLUMN_CONTENT = "content";
        /* item_type is integer value if item is trailer item_type is equal to 0,
        * if item is review item_type is equal to 1
        * */
        public static final String OWM_COLUMN_TYPE = "item_type";
        /* item_name is String value if item type is trailer so item_name is equal to trailer name, which almost the name
        * of youtube channel , in case of item_type is review the item_name is equal to the name of reviewers
        * */
        public static final String OWM_COLUM_ITEM_NAME="item_name";

        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
