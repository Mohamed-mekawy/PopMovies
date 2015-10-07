package com.example.mekawy.popmovies.Data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class dbContract {

    public static final String CONTENT_AUTHORITY ="com.example.mekawy.popmovies";
    final static Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAV_MOVIES = "fav_movies";
    public static final String PATH_POP_MOVIES = "pop_movies";
    public static final String PATH_VOTE_MOVIES = "vote_movies";
    public static final String PATH_TRAILERS_REVIEWS="movies_trailer_reviews";

    public static final String OWM_COMMON_COLUMN_TAG = "id";
    public static final String OWM_COMMON_POSTER_PATH = "poster_path";
    public static final String OWM_COMMON_COLUMN_TITLE = "original_title";
    public static final String OWM_COMMON_COLUMN_OVERVIEW = "overview";
    public static final String OWM_COMMON_COLUMN_RELEASE_DATE = "release_date";
    public static final String OWM_COMMON_COLUMN_VOTE_AVERAGE = "vote_average";

    public final static String[] COMMON_SORT_PROJECTION ={
            "_id",
            dbContract.OWM_COMMON_COLUMN_TAG,
            dbContract.OWM_COMMON_COLUMN_TITLE,
            dbContract.OWM_COMMON_COLUMN_OVERVIEW,
            dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,
            dbContract.OWM_COMMON_POSTER_PATH,
            dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE,
    };

    public final static String[] TRAILER_REVIEW_PROJECTION={
            TRAILER_REVIEWS_TABLE._ID,
            TRAILER_REVIEWS_TABLE.OWM_COLUMN_MOVIE_TAG,
            TRAILER_REVIEWS_TABLE.OWM_COLUMN_ITEM_ID,
            TRAILER_REVIEWS_TABLE.OWM_COLUMN_CONTENT,
            TRAILER_REVIEWS_TABLE.OWM_COLUMN_TYPE
};
    public static final int DETAILS_TYPE_TRAILER=0;
    public static final int DETAILS_TYPE_REVIEWS=1;


    public static final class POP_MOVIES_TABLE implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POP_MOVIES).build();

        public final static String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_POP_MOVIES;

        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +    CONTENT_AUTHORITY   +  "/" +    PATH_POP_MOVIES;


        public static final String TABLE_NAME = "pop_movies";

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

    public static class MOST_VOTED_TABLE implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VOTE_MOVIES).build();

        public final static String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_VOTE_MOVIES;

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

        public static Uri builUriwithtag(int tag){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(tag)).build();
        }

    }

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

    public static class TRAILER_REVIEWS_TABLE implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS_REVIEWS).build();

        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +    CONTENT_AUTHORITY   +  "/" +    PATH_TRAILERS_REVIEWS;

        public static final String TABLE_NAME = PATH_TRAILERS_REVIEWS;

        public static final String OWM_COLUMN_MOVIE_TAG = "id";
        public static final String OWM_COLUMN_ITEM_ID = "item_id";
        public static final String OWM_COLUMN_CONTENT = "content";
        public static final String OWM_COLUMN_TYPE = "item_type";

        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }




}
