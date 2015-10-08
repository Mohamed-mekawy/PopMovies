package com.example.mekawy.popmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.mekawy.popmovies.Data.MoviesContract.*;


/* Create db with tables in Contract Locally */

public class MoviesdbHelper extends SQLiteOpenHelper {

    private final static String DB_NAME="MoviesApp";
    private final static int DB_VERSION=1;


    public MoviesdbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /* SQL statement for creating the pop_movies Tables defining each Column name and types
        * that structure is common in the three tables (pop/vote/fav)
        * */
        final String SQL_POP_MOVIES_TABLE=
                "CREATE TABLE "+ MoviesContract.POP_MOVIES_TABLE.TABLE_NAME +" ( "+
                        MoviesContract.POP_MOVIES_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        MoviesContract.POP_MOVIES_TABLE.OWM_COLUMN_TAG + " INTEGER UNIQUE NOT NULL, " +
                        MoviesContract.POP_MOVIES_TABLE.OWM_COLUMN_TITLE + " TEXT NOT NULL, " +
                        MoviesContract.POP_MOVIES_TABLE.OWM_COLUMN_OVERVIEW +" TEXT NOT NULL, " +
                        MoviesContract.POP_MOVIES_TABLE.OWM_COLUMN_RELEASE_DATE+ " TEXT NOT NULL, "+
                        MoviesContract.POP_MOVIES_TABLE.OWM_COLUMN_POSTER_PATH + " TEXT NOT NULL, "+
                        MoviesContract.POP_MOVIES_TABLE.OWM_COLUMN_VOTE_AVERAGE+ " REAL NOT NULL);";

        final String SQL_VOTE_MOVIES_TABLE=
                "CREATE TABLE "+ VOTE_DESC_TABLE.TABLE_NAME +" ( "+
                        VOTE_DESC_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        VOTE_DESC_TABLE.OWM_COLUMN_TAG + " INTEGER UNIQUE NOT NULL, " +
                        VOTE_DESC_TABLE.OWM_COLUMN_TITLE + " TEXT NOT NULL, " +
                        VOTE_DESC_TABLE.OWM_COLUMN_OVERVIEW +" TEXT NOT NULL, " +
                        VOTE_DESC_TABLE.OWM_COLUMN_RELEASE_DATE+ " TEXT NOT NULL, "+
                        VOTE_DESC_TABLE.OWM_COLUMN_POSTER_PATH + " TEXT NOT NULL, "+
                        VOTE_DESC_TABLE.OWM_COLUMN_VOTE_AVERAGE+ " REAL NOT NULL); ";

        final String SQL_FAV_MOVIES_TABLE=
                "CREATE TABLE "+ FAV_MOVIES_TABLE.TABLE_NAME +" ( "+
                        FAV_MOVIES_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        FAV_MOVIES_TABLE.OWM_COLUMN_TAG + " INTEGER UNIQUE NOT NULL, " +
                        FAV_MOVIES_TABLE.OWM_COLUMN_TITLE + " TEXT NOT NULL, " +
                        FAV_MOVIES_TABLE.OWM_COLUMN_OVERVIEW +" TEXT NOT NULL, " +
                        FAV_MOVIES_TABLE.OWM_COLUMN_RELEASE_DATE+ " TEXT NOT NULL, "+
                        FAV_MOVIES_TABLE.OWM_COLUMN_POSTER_PATH + " TEXT NOT NULL, "+
                        FAV_MOVIES_TABLE.OWM_COLUMN_VOTE_AVERAGE+ " REAL NOT NULL);";

        /* SQL statement for creating the TRAILER_REVIEWS TABLE defining the Column name and types but filling the data
        * will differ in case of trailer or movies
        */

        final String SQL_TRAILER_REVIEWS_TABLE=
                "CREATE TABLE "+ MoviesContract.TRAILER_REVIEWS_TABLE.TABLE_NAME+" ( "+
                        MoviesContract.TRAILER_REVIEWS_TABLE._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        MoviesContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_MOVIE_TAG + " TEXT NOT NULL, "+
                        MoviesContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_ITEM_ID + " TEXT UNIQUE NOT NULL, "+
                        TRAILER_REVIEWS_TABLE.OWM_COLUM_ITEM_NAME+" TEXT ,"+
                        MoviesContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_CONTENT+ " TEXT, "+
                        MoviesContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_TYPE +" INTEGER NOT NULL );";

        // execute the SQL statement to create the 4 tables
        sqLiteDatabase.execSQL(SQL_POP_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_FAV_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_VOTE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_TRAILER_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        /* In case of upgrade i will only DROP the the tables which have online resources such POP/Vote tables ,
        *  keeping the fav_movies table
        * */
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ VOTE_DESC_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ POP_MOVIES_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TRAILER_REVIEWS_TABLE.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
