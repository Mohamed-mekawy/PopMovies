package com.example.mekawy.popmovies.Data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


import com.example.mekawy.popmovies.Data.MoviesContract.*;

public class MoviesProvider extends ContentProvider{
    // create instance of MoviesdbHelper to use the locally DB
    private static MoviesdbHelper mhelper;

    /*Define the matcher which will used to match received URI , to define which table will handle for
    * INSERT,UPDATE,DELETE,QUERY
    */
    private static final UriMatcher sUriMatcher=fill_matcher();


    private static final int POP_MOVIES=1;
    private static final int POP_MOVIES_WITH_TAG =2;

    private static final int VOTE_MOVIES=3;
    private static final int VOTE_MOVIES_WITH_TAG =4;

    private static final int FAV_MOVIES=5;
    private static final int FAV_MOVIES_WITH_TAG =6;

    private static final int TRAILER_REVIEWS=7;

    /* Define the String for QUERY using the Query Builder  COLUMN_TAG represent the UNIQUE movie tag
    * in pop/vote/fav movies, and it can be repeated in movies_trailer_reviews table as the movie can have
    * multi trailers and multi reviews
    */

    // pop_movies.id =?
    private static final String POP_MOVIE_SELECT_BY_TAG=
            POP_MOVIES_TABLE.TABLE_NAME+"."+POP_MOVIES_TABLE.OWM_COLUMN_TAG+ " = ? ";
    // vote_movies.id =?
    private static final String VOTE_MOVIE_SELECT_BY_TAG=
            VOTE_DESC_TABLE.TABLE_NAME+"."+ VOTE_DESC_TABLE.OWM_COLUMN_TAG+ " = ? ";
    // fav_movies.id =?
    private static final String FAV_MOVIE_SELECT_BY_TAG=
            FAV_MOVIES_TABLE.TABLE_NAME+"."+FAV_MOVIES_TABLE.OWM_COLUMN_TAG+ " = ? ";
    // movies_trailer_reviews.id =?
    private static final String TRAILERS_REVIES_SELECT_BY_TAG=
            TRAILER_REVIEWS_TABLE.TABLE_NAME+"."+TRAILER_REVIEWS_TABLE.OWM_COLUMN_MOVIE_TAG+ " = ? ";



    private static UriMatcher fill_matcher(){
        UriMatcher mMathcer=new UriMatcher(UriMatcher.NO_MATCH);
        String Authority= MoviesContract.CONTENT_AUTHORITY;

        /* matcher define each uri as integer value after matching it's format , and these
        * Integer values predefined as final int in before
        * (#) represent movie tag
        */

        mMathcer.addURI(Authority, MoviesContract.PATH_POP_MOVIES,POP_MOVIES);
        mMathcer.addURI(Authority, MoviesContract.PATH_POP_MOVIES+"/#", POP_MOVIES_WITH_TAG);

        mMathcer.addURI(Authority, MoviesContract.PATH_VOTE_MOVIES,VOTE_MOVIES);
        mMathcer.addURI(Authority, MoviesContract.PATH_VOTE_MOVIES+"/#", VOTE_MOVIES_WITH_TAG);

        mMathcer.addURI(Authority, MoviesContract.PATH_FAV_MOVIES,FAV_MOVIES);
        mMathcer.addURI(Authority, MoviesContract.PATH_FAV_MOVIES+"/#", FAV_MOVIES_WITH_TAG);

        mMathcer.addURI(Authority, MoviesContract.PATH_TRAILERS_REVIEWS,TRAILER_REVIEWS);

        return mMathcer;
    }


    /*
    * this function used to get the movie cursor by only the movie tag, after receiving the
    * Query from Current provider and matching it , if it's like format
    * content://com.example.mekawy.popmovies/table_name/XXXX , where table name can be
    * (pop/vote/fav), the provider then call this method to peroform custom query using the
    *  Custom QUERY using sQueryBuilder
    */

    private Cursor GetMovieByTag(Uri uri, String[] projection, String sort_order){

        SQLiteQueryBuilder sQueryBuilder=new SQLiteQueryBuilder();

        SQLiteDatabase db=mhelper.getReadableDatabase();
        /* elicit the Table name and movie tag from passed URI*/
        String Table_name=uri.getPathSegments().get(0);
        String Movie_TAG=uri.getPathSegments().get(1);

        String selection;

        /*Assing the Selection statement according to the URI table_name , and directing the Query Builder
        * to selected table
        */

        if(Table_name.equals(POP_MOVIES_TABLE.TABLE_NAME)){
            selection=POP_MOVIE_SELECT_BY_TAG;
            sQueryBuilder.setTables(POP_MOVIES_TABLE.TABLE_NAME);
        }

        else if(Table_name.equals(VOTE_DESC_TABLE.TABLE_NAME)){
            selection=VOTE_MOVIE_SELECT_BY_TAG;
            sQueryBuilder.setTables(VOTE_DESC_TABLE.TABLE_NAME);
        }

        else if(Table_name.equals(FAV_MOVIES_TABLE.TABLE_NAME)){
            selection=FAV_MOVIE_SELECT_BY_TAG;
            sQueryBuilder.setTables(FAV_MOVIES_TABLE.TABLE_NAME);
        }

        /* if any error happened matching table name or Uri contain invalid table name
        *  return the function with NULL Cursor
        */
        else return null;

        // Selection String must be the Movie_Tag to match the Selection statement
        //table_name.id=movie_tag
        String SelectionArgs[]=new String[]{Movie_TAG};

        Cursor mCur=sQueryBuilder.query(
                db,
                projection,
                selection,
                SelectionArgs,
                null,
                null,
                sort_order);

        return mCur;
    }


    @Override
    public boolean onCreate() {
        /*get new Instance of database helper Class to get permission to perform operation with
        * the locally db*/
        mhelper =new MoviesdbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        SQLiteDatabase db=mhelper.getReadableDatabase();
        int match_val=sUriMatcher.match(uri);
        //match to define the Kind of request
        Cursor ret_cursor = null;
        switch (match_val){

            // content://com.example.mekawy.popmovies/pop_movies
            case POP_MOVIES:{
                ret_cursor=db.query(POP_MOVIES_TABLE.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sort);
                break;
            }

            // content://com.example.mekawy.popmovies/vote_movies
            case VOTE_MOVIES:{
                ret_cursor=db.query(VOTE_DESC_TABLE.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sort);
                break;
            }

            // content://com.example.mekawy.popmovies/fav_movies
            case FAV_MOVIES:{
                ret_cursor=db.query(FAV_MOVIES_TABLE.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sort);
                break;
            }

            // content://com.example.mekawy.popmovies/movies_trailers_reviews
            case TRAILER_REVIEWS:{
                ret_cursor=db.query(MoviesContract.TRAILER_REVIEWS_TABLE.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sort
                        );
                break;
            }

            // content://com.example.mekawy.popmovies/pop_movies/XXXX
                // it will call the Function to preform cutom query
            case POP_MOVIES_WITH_TAG:{
                ret_cursor= GetMovieByTag(uri, projection, sort);
                break;
            }

            // content://com.example.mekawy.popmovies/vote_movies/XXXX
            case VOTE_MOVIES_WITH_TAG:{
                ret_cursor= GetMovieByTag(uri, projection, sort);
                break;
            }

            // content://com.example.mekawy.popmovies/fav_movies/XXXX
            case FAV_MOVIES_WITH_TAG:{
                ret_cursor= GetMovieByTag(uri, projection, sort);
                break;
            }
            default:throw  new UnsupportedOperationException("unsupported Query "+uri);
        }
        //notify the observer for any changes
        ret_cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return ret_cursor;
    }

    @Override
    public String getType(Uri uri) {
            // DEFINE the kind of uri by matching
        int match_value=sUriMatcher.match(uri);
        switch (match_value){
            case POP_MOVIES:return POP_MOVIES_TABLE.CONTENT_DIR_TYPE;
            case POP_MOVIES_WITH_TAG:return POP_MOVIES_TABLE.CONTENT_ITEM_TYPE;

            case FAV_MOVIES:return FAV_MOVIES_TABLE.CONTENT_DIR_TYPE;
            case FAV_MOVIES_WITH_TAG:return FAV_MOVIES_TABLE.CONTENT_ITEM_TYPE;

            case VOTE_MOVIES:return VOTE_DESC_TABLE.CONTENT_DIR_TYPE;
            case VOTE_MOVIES_WITH_TAG:return VOTE_DESC_TABLE.CONTENT_ITEM_TYPE;

            case TRAILER_REVIEWS:return MoviesContract.TRAILER_REVIEWS_TABLE.CONTENT_DIR_TYPE;

            default: throw new UnsupportedOperationException("unsupported type :"+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db=mhelper.getWritableDatabase();
        int match_value=sUriMatcher.match(uri);
        Uri ret_uri;
        switch (match_value){

            case POP_MOVIES:{
                Long ret_val=db.insert(POP_MOVIES_TABLE.TABLE_NAME,null,contentValues);
                if(ret_val!=-1) ret_uri=POP_MOVIES_TABLE.buildMovieUri(ret_val);
                // if there is an error in insertion in database it will throw the exception
                else throw new SQLException("Provider_insert_DB_NOT_VALID");
                break;
            }

            case VOTE_MOVIES:{
                Long ret_val=db.insert(VOTE_DESC_TABLE.TABLE_NAME,null,contentValues);
                if(ret_val!=-1) ret_uri= VOTE_DESC_TABLE.buildMovieUri(ret_val);
                else throw new SQLException("Provider_insert_DB_NOT_VALID");
                break;
            }

            case FAV_MOVIES:{
                Long ret_val=db.insert(FAV_MOVIES_TABLE.TABLE_NAME,null,contentValues);
                if(ret_val!=-1) ret_uri=FAV_MOVIES_TABLE.buildMovieUri(ret_val);
                else throw new SQLException("Provider_insert_DB_NOT_VALID");
                break;
            }

            case TRAILER_REVIEWS:{
                Long ret_val = db.insert(MoviesContract.TRAILER_REVIEWS_TABLE.TABLE_NAME, null, contentValues);
                if (ret_val != -1) ret_uri = MoviesContract.TRAILER_REVIEWS_TABLE.buildTrailerUri(ret_val);
                else throw new SQLException("Provider_insert_DB_NOT_VALID");
                break;
            }
            // if there is no matched URI throw Exception
            default: throw new UnsupportedOperationException("error,Uri not supported");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ret_uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db=mhelper.getWritableDatabase();
        int match_val=sUriMatcher.match(uri);

        int ret_val;
        String Selection;
        String mTag;

        switch (match_val){
            // Delete the movie from db by movie_tag reference
            case POP_MOVIES_WITH_TAG:{
                Selection=POP_MOVIE_SELECT_BY_TAG;
                mTag=uri.getLastPathSegment();
                ret_val=db.delete(POP_MOVIES_TABLE.TABLE_NAME, Selection, new String[]{mTag});
                break;
            }

            case VOTE_MOVIES_WITH_TAG:{
                Selection=VOTE_MOVIE_SELECT_BY_TAG;
                mTag=uri.getLastPathSegment();
                ret_val=db.delete(VOTE_DESC_TABLE.TABLE_NAME, Selection, new String[]{mTag});
                break;
            }

            case FAV_MOVIES_WITH_TAG:{
                Selection=FAV_MOVIE_SELECT_BY_TAG;
                mTag=uri.getLastPathSegment();
                ret_val=db.delete(FAV_MOVIES_TABLE.TABLE_NAME, Selection, new String[]{mTag});
                break;
            }
            // Delete both Trailers and reviews referenced by movie tag
            case TRAILER_REVIEWS:{
                Selection=TRAILERS_REVIES_SELECT_BY_TAG;
                mTag=uri.getLastPathSegment();
                ret_val=db.delete(TRAILER_REVIEWS_TABLE.TABLE_NAME, Selection, new String[]{mTag});
                break;
            }

            default:throw new UnsupportedOperationException("unsupported delete command : "+uri);
        }
        //notify only in case of rows deleted
        if(ret_val!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return ret_val;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        /* Update will receive uri appended with tag and the content values of updated values*/
        SQLiteDatabase db=mhelper.getWritableDatabase();
        int match_val=sUriMatcher.match(uri);
        int ret_val;

        String Selection;
        String mTag;

        switch (match_val){

            case POP_MOVIES_WITH_TAG:{
                Selection=POP_MOVIE_SELECT_BY_TAG;
                mTag=uri.getLastPathSegment();
                ret_val=db.update(POP_MOVIES_TABLE.TABLE_NAME,contentValues,Selection,new String[]{mTag});
                break;
            }

            case VOTE_MOVIES_WITH_TAG:{
                Selection=VOTE_MOVIE_SELECT_BY_TAG;
                mTag=uri.getLastPathSegment();
                ret_val=db.update(VOTE_DESC_TABLE.TABLE_NAME,contentValues,Selection,new String[]{mTag});
                break;
            }

            case FAV_MOVIES:{
                ret_val=db.update(FAV_MOVIES_TABLE.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            }
            default:throw new UnsupportedOperationException("unsupported update command : "+uri);
        }
        if(ret_val!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return ret_val;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db=mhelper.getWritableDatabase();
        int match_val=sUriMatcher.match(uri);
        int ret_val;
        ret_val=0;
        /* Bulk insert used after Parsing the JSON String and if the New movies
        * as it accept array of Contentvalues represents the movies data
        */
        switch (match_val){

            case POP_MOVIES:{
                db.beginTransaction();
                for(ContentValues value:values){
                    long ret=db.insert(POP_MOVIES_TABLE.TABLE_NAME,null,value);
                    if(ret!=-1)ret_val++;
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                break;
            }

            case VOTE_MOVIES:{
                db.beginTransaction();
                for(ContentValues value:values){
                    long ret=db.insert(VOTE_DESC_TABLE.TABLE_NAME,null,value);
                    if(ret!=-1)ret_val++;
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                break;
            }

            default :throw new UnsupportedOperationException("unsupported bulk insertion, "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ret_val;
    }
}
