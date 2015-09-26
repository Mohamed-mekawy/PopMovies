package com.example.mekawy.popmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.mekawy.popmovies.Data.dbContract.FAV_MOVIES_TABLE;
import com.example.mekawy.popmovies.Data.dbContract.MOST_VOTED_TABLE;
import com.example.mekawy.popmovies.Data.dbContract.POP_MOVIES_TABLE;

public class dbOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME="MoviesApp";
    private final static int DB_VERSION=1;


    public dbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_POP_MOVIES_TABLE=
                "CREATE TABLE "+ dbContract.POP_MOVIES_TABLE.TABLE_NAME +" ( "+
                        dbContract.POP_MOVIES_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        dbContract.POP_MOVIES_TABLE.OWM_COLUMN_TAG + " INTEGER UNIQUE NOT NULL, " +
                        dbContract.POP_MOVIES_TABLE.OWM_COLUMN_TITLE + " TEXT NOT NULL, " +
                        dbContract.POP_MOVIES_TABLE.OWM_COLUMN_OVERVIEW +" TEXT NOT NULL, " +
                        dbContract.POP_MOVIES_TABLE.OWM_COLUMN_RELEASE_DATE+ " TEXT NOT NULL, "+
                        dbContract.POP_MOVIES_TABLE.OWM_COLUMN_POSTER_PATH + " TEXT NOT NULL, "+
                        dbContract.POP_MOVIES_TABLE.OWM_COLUMN_VOTE_AVERAGE+ " REAL NOT NULL, "+
                        dbContract.POP_MOVIES_TABLE.OWM_COLUMN_IS_FAVORITE+ " INTEGER NOT NULL);";

        final String SQL_VOTE_MOVIES_TABLE=
                "CREATE TABLE "+ dbContract.MOST_VOTED_TABLE.TABLE_NAME +" ( "+
                        dbContract.MOST_VOTED_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        dbContract.MOST_VOTED_TABLE.OWM_COLUMN_TAG + " INTEGER UNIQUE NOT NULL, " +
                        dbContract.MOST_VOTED_TABLE.OWM_COLUMN_TITLE + " TEXT NOT NULL, " +
                        dbContract.MOST_VOTED_TABLE.OWM_COLUMN_OVERVIEW +" TEXT NOT NULL, " +
                        dbContract.MOST_VOTED_TABLE.OWM_COLUMN_RELEASE_DATE+ " TEXT NOT NULL, "+
                        dbContract.MOST_VOTED_TABLE.OWM_COLUMN_POSTER_PATH + " TEXT NOT NULL, "+
                        dbContract.MOST_VOTED_TABLE.OWM_COLUMN_VOTE_AVERAGE+ " REAL NOT NULL, "+
                        dbContract.MOST_VOTED_TABLE.OWM_COLUMN_IS_FAVORITE+ " INTEGER NOT NULL);";

        final String SQL_FAV_MOVIES_TABLE=
                "CREATE TABLE "+ FAV_MOVIES_TABLE.TABLE_NAME +" ( "+
                        FAV_MOVIES_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        FAV_MOVIES_TABLE.OWM_COLUMN_TAG + " INTEGER UNIQUE NOT NULL, " +
                        FAV_MOVIES_TABLE.OWM_COLUMN_TITLE + " TEXT NOT NULL, " +
                        FAV_MOVIES_TABLE.OWM_COLUMN_OVERVIEW +" TEXT NOT NULL, " +
                        FAV_MOVIES_TABLE.OWM_COLUMN_RELEASE_DATE+ " TEXT NOT NULL, "+
                        FAV_MOVIES_TABLE.OWM_COLUMN_POSTER_PATH + " TEXT NOT NULL, "+
                        FAV_MOVIES_TABLE.OWM_COLUMN_VOTE_AVERAGE+ " REAL NOT NULL);";

        sqLiteDatabase.execSQL(SQL_POP_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_FAV_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_VOTE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MOST_VOTED_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FAV_MOVIES_TABLE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ POP_MOVIES_TABLE.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
