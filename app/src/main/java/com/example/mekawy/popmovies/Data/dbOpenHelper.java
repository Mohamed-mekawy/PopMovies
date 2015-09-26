package com.example.mekawy.popmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.mekawy.popmovies.Data.dbContract.MOST_POPULAR_TABLE;
    import com.example.mekawy.popmovies.Data.dbContract.VOTE_TABLE;
    import com.example.mekawy.popmovies.Data.dbContract.FAVORITE_TABLE;


public class dbOpenHelper extends SQLiteOpenHelper {

    public static final int VERSION_NUMBER=1;
    public static final String DB_NAME="popmovies.db";

    public dbOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NUMBER);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_MOVIES_TABLE=
                        "CREATE TABLE "+ dbContract.MOVIES_TABLE.TABLE_NAME+
                        " ("+ dbContract.MOVIES_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        dbContract.MOVIES_TABLE.OWM_COLUMN_TAG +" INTEGER UNIQUE NOT NULL, "+
                        dbContract.MOVIES_TABLE.OWM_COLUMN_TITLE +" TEXT NOT NULL, "+
                        dbContract.MOVIES_TABLE.OWM_COLUMN_OVERVIEW + " TEXT NOT NULL, "+
                        dbContract.MOVIES_TABLE.OWM_COLUMN_RELEASE_DATE + " TEXT NOT NULL, "+
                        dbContract.MOVIES_TABLE.OWM_COLUMN_POSTER_PATH +" TEXT NOT NULL, "+
                        dbContract.MOVIES_TABLE.OWM_COLUMN_VOTE_AVERAGE +" REAL NOT NULL, "+
                        dbContract.MOVIES_TABLE.OWM_COLUMN_IS_FAVORITE +" INTEGER NOT NULL );";

        final String SQL_MOST_POP_MOVIES_TABLE=
                        "CREATE TABLE "+MOST_POPULAR_TABLE.TABLE_NAME+
                        " ("+MOST_POPULAR_TABLE._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        MOST_POPULAR_TABLE.OWM_COLUMN_MOVIE_ID + " INTEGER NOT NULL, "+
                        MOST_POPULAR_TABLE.OWM_COLUMN_TITLE +" TEXT NOT NULL, "+
                        MOST_POPULAR_TABLE.OWM_COLUMN_IS_FAVORITE+ " INTEGER NOT NULL,"+
                        " FOREIGN KEY (" +MOST_POPULAR_TABLE.OWM_COLUMN_MOVIE_ID +") REFERENCES "+
                        dbContract.MOVIES_TABLE.TABLE_NAME+" ("+ dbContract.MOVIES_TABLE._ID +" ), "+
                        " UNIQUE ("+MOST_POPULAR_TABLE._ID+","+MOST_POPULAR_TABLE.OWM_COLUMN_MOVIE_ID+") ON CONFLICT REPLACE);";

        final String SQL_VOTE_DESC_MOVIES_TABLE=
                        "CREATE TABLE "+VOTE_TABLE.TABLE_NAME+
                        " ("+VOTE_TABLE._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                VOTE_TABLE.OWM_COLUMN_MOVIE_ID + " INTEGER NOT NULL, "+
                                VOTE_TABLE.OWM_COLUMN_TITLE +" TEXT NOT NULL, "+
                                VOTE_TABLE.OWM_COLUMN_IS_FAVORITE+ " INTEGER NOT NULL,"+
                        " FOREIGN KEY (" +VOTE_TABLE.OWM_COLUMN_MOVIE_ID +") REFERENCES "+
                        dbContract.MOVIES_TABLE.TABLE_NAME+" ("+ dbContract.MOVIES_TABLE._ID +" ), "+
                        " UNIQUE ("+VOTE_TABLE._ID+","+VOTE_TABLE.OWM_COLUMN_MOVIE_ID+") ON CONFLICT REPLACE);";

        final String SQL_FAV_MOVIES_TABLE=
                        "CREATE TABLE "+FAVORITE_TABLE.TABLE_NAME+
                        " ("+FAVORITE_TABLE._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        FAVORITE_TABLE.OWM_COLUMN_MOVIE_ID + " INTEGER NOT NULL, "+
                        FAVORITE_TABLE.OWM_COLUMN_TITLE +" TEXT NOT NULL, "+
                        " FOREIGN KEY (" +FAVORITE_TABLE.OWM_COLUMN_MOVIE_ID +") REFERENCES "+
                        dbContract.MOVIES_TABLE.TABLE_NAME+" ("+ dbContract.MOVIES_TABLE._ID +" ), "+
                        " UNIQUE ("+FAVORITE_TABLE._ID+","+FAVORITE_TABLE.OWM_COLUMN_MOVIE_ID+") ON CONFLICT REPLACE);";



        sqLiteDatabase.execSQL(SQL_MOST_POP_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_VOTE_DESC_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_FAV_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ dbContract.MOVIES_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ dbContract.FAVORITE_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ dbContract.VOTE_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ dbContract.MOST_POPULAR_TABLE.TABLE_NAME);
    }
}
