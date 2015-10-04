package com.example.mekawy.popmovies.Data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class dbContract {

    public static final String CONTENT_AUTHORITY ="com.example.mekawy.popmovies";
    final static Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);

    //construct three tables for "favorite movies","popular movies","most vote movies"

    public static final String PATH_FAV_MOVIES = "fav_movies";
    public static final String PATH_POP_MOVIES = "pop_movies";
    public static final String PATH_VOTE_MOVIES = "vote_movies";
    public static final String PATH_MOVIES_VIDEOS="movies_videos";


    public static final String OWM_COMMON_COLUMN_TAG = "id";
    public static final String OWM_COMMON_POSTER_PATH = "poster_path";
    public static final String OWM_COMMON_COLUMN_TITLE = "original_title";
    public static final String OWM_COMMON_COLUMN_OVERVIEW = "overview";
    public static final String OWM_COMMON_COLUMN_RELEASE_DATE = "release_date";
    public static final String OWM_COMMON_COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String OWM_COMMON_COLUMN_IS_FAVORITE="isFav";



    public final static String[] COMMON_PROJECTION={
            "_id",
            dbContract.OWM_COMMON_COLUMN_TAG,
            dbContract.OWM_COMMON_COLUMN_TITLE,
            dbContract.OWM_COMMON_COLUMN_OVERVIEW,
            dbContract.OWM_COMMON_COLUMN_RELEASE_DATE,
            dbContract.OWM_COMMON_POSTER_PATH,
            dbContract.OWM_COMMON_COLUMN_VOTE_AVERAGE,
            dbContract.OWM_COMMON_COLUMN_IS_FAVORITE
    };





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
        public static final String OWM_COLUMN_IS_FAVORITE="isFav";

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
        public static final String OWM_COLUMN_IS_FAVORITE="isFav";

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
    }


    public static class MOVIE_VIDEOS implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES_VIDEOS).build();


        public final static String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_MOVIES_VIDEOS;

        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +    CONTENT_AUTHORITY   +  "/" +    PATH_MOVIES_VIDEOS;

        public static final String TABLE_NAME = PATH_MOVIES_VIDEOS;

        public static final String OWM_COLUMN_MOVIE_TAG = "id";
        public static final String OWM_COLUMN_TRAILER_ID = "trailer_id";
        public static final String OWM_COLUMN_KEY = "key";
        public static final String OWM_COLUMN_TRAILER_NAME = "name";

        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }



    }





}
