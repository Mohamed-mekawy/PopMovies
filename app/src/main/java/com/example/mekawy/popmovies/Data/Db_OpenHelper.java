package com.example.mekawy.popmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




public class Db_OpenHelper extends SQLiteOpenHelper {

    public static final int VERSION_NUMBER=1;
    public static final String DB_NAME="popmovies.db";

    public Db_OpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NUMBER);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_MOST_MOVIES_TABLE=

                "CREATE TABLE "+DB_Contract.MOVIES_TABLE.TABLE_NAME+
                        " ("+ DB_Contract.MOVIES_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        DB_Contract.MOVIES_TABLE.OWM_COLUMN_TAG +" INTEGER UNIQUE NOT NULL, "+
                        DB_Contract.MOVIES_TABLE.OWM_COLUMN_TITLE +" TEXT NOT NULL, "+
                        DB_Contract.MOVIES_TABLE.OWM_COLUMN_OVERVIEW + " TEXT NOT NULL, "+
                        DB_Contract.MOVIES_TABLE.OWM_COLUMN_RELEASE_DATE + " TEXT NOT NULL, "+
                        DB_Contract.MOVIES_TABLE.OWM_COLUMN_POSTER_PATH +" TEXT NOT NULL, "+
                        DB_Contract.MOVIES_TABLE.OWM_COLUMN_VOTE_AVERAGE +" REAL NOT NULL, "+
                        DB_Contract.MOVIES_TABLE.OWM_COLUMN_IS_FAVORITE +" INTEGER NOT NULL );";

           sqLiteDatabase.execSQL(SQL_MOST_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
      sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DB_Contract.MOVIES_TABLE.TABLE_NAME);
    }
}
