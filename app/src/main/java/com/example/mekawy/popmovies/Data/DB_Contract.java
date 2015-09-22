package com.example.mekawy.popmovies.Data;


import android.provider.BaseColumns;

public class DB_Contract {


    //common columns in tables


    public static final class MOVIES_TABLE implements BaseColumns{

        public static final String TABLE_NAME = "Tmovies";

        public static final String OWM_COLUMN_TAG = "id";
        public static final String OWM_COLUMN_TITLE = "title";
        public static final String OWM_COLUMN_OVERVIEW = "overview";
        public static final String OWM_COLUMN_RELEASE_DATE = "date";
        public static final String OWM_COLUMN_POSTER_PATH = "poster";
        public static final String OWM_COLUMN_VOTE_AVERAGE = "vote";
        public static final String OWM_COLUMN_IS_FAVORITE="isFav";

    }

    public static class MOST_POPULAR_TABLE implements BaseColumns {
        public static final String TABLE_NAME = "Tpop";

        public static final String OWM_COLUMN_MOVIE_ID = "movie_id";
        public static final String OWM_COLUMN_TITLE = "title";
        public static final String OWM_COLUMN_IS_FAVORITE="isFav";
    }


    public static class VOTE_TABLE implements BaseColumns {
        public static final String TABLE_NAME = "Tvote";

        public static final String OWM_COLUMN_MOVIE_ID= "movie_id";
        public static final String OWM_COLUMN_TITLE = "title";
        public static final String OWM_COLUMN_IS_FAVORITE="isFav";
    }

    public static class FAVORITE_TABLE implements BaseColumns{
        public static final String TABLE_NAME = "TFav";

        public static final String OWM_COLUMN_MOVIE_ID = "id";
        public static final String OWM_COLUMN_TITLE = "title";
    }



}
