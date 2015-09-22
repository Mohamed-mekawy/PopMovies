package com.example.mekawy.popmovies.Data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DB_Contract {

    // Content uri
    public static final String CONTENT_AUTHORITY ="com.example.mekawy.popmovies";
    final static Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "Tmovies";
    public static final String PATH_FAV_MOVIES = "TFav";
    public static final String PATH_POP_MOVIES = "Tpop";
    public static final String PATH_VOTE_MOVIES = "Tvote";


    public static final class MOVIES_TABLE implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public final static String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_MOVIES;

        public static final String TABLE_NAME = "Tmovies";

        public static final String OWM_COLUMN_TAG = "id";
        public static final String OWM_COLUMN_TITLE = "title";
        public static final String OWM_COLUMN_OVERVIEW = "overview";
        public static final String OWM_COLUMN_RELEASE_DATE = "date";
        public static final String OWM_COLUMN_POSTER_PATH = "poster";
        public static final String OWM_COLUMN_VOTE_AVERAGE = "vote";
        public static final String OWM_COLUMN_IS_FAVORITE="isFav";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri buildMovieby_Movieid(String movie_id){
            return CONTENT_URI.buildUpon().appendPath(movie_id).build();
        }

    }

    public static class MOST_POPULAR_TABLE implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POP_MOVIES).build();

        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE +  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_POP_MOVIES;

        public static final String TABLE_NAME = "Tpop";

        public static final String OWM_COLUMN_MOVIE_ID = "movie_id";
        public static final String OWM_COLUMN_TITLE = "title";
        public static final String OWM_COLUMN_IS_FAVORITE="isFav";
    }


    public static class VOTE_TABLE implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VOTE_MOVIES).build();

        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE +  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_VOTE_MOVIES;


        public static final String TABLE_NAME = "Tvote";

        public static final String OWM_COLUMN_MOVIE_ID= "movie_id";
        public static final String OWM_COLUMN_TITLE = "title";
        public static final String OWM_COLUMN_IS_FAVORITE="isFav";
    }

    public static class FAVORITE_TABLE implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

        public final static String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE +  "/" +    CONTENT_AUTHORITY   + "/" +    PATH_FAV_MOVIES;


        public static final String TABLE_NAME = "TFav";

        public static final String OWM_COLUMN_MOVIE_ID = "id";
        public static final String OWM_COLUMN_TITLE = "title";
    }

}
