package com.example.mekawy.popmovies;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.example.mekawy.popmovies.Data.MoviesContract;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parser_Task extends AsyncTask<String,Void,Integer>{

    private Context mContext;

    final String OWM_RESULTS="results";
    final String OWM_TAG="id";
    final String OWM_TITLE="original_title";
    final String OWM_GRID_POSTER="poster_path";
    final String OWM_OVERVIEW="overview";
    final String OWM_DATE="release_date";
    final String OWM_RATE="vote_average";

    /*
    * @param results    Contain array of fetched movies
    * @param id         the unique id of movie
    * @param original_title     The title of movie
    * @param poster_path        the link for poster path
    * @param overview           the describtion of movie
    * @param release_date       the Release date of movie
    * @param vote_average       Average vote of movie
    * */


    public Parser_Task(Context context){
        mContext=context;

    }

    /*this method receive the JSON Object and parse it to content values accepted by data base, the returned
    * Content values will be saved into array,
    * */

    public ContentValues GET_MOVIE_CONTENT(JSONObject movie_object){
        ContentValues retContent =new ContentValues();

        try {
            int mTag=movie_object.getInt(OWM_TAG);
            String mTitle=movie_object.getString(OWM_TITLE);
            String mOverview=movie_object.getString(OWM_OVERVIEW);
            String mPoster=movie_object.getString(OWM_GRID_POSTER);
            String mDate=movie_object.getString(OWM_DATE);
            double mRate=movie_object.getDouble(OWM_RATE);

            retContent.put(OWM_TAG, mTag);
            retContent.put(OWM_TITLE, mTitle);
            retContent.put(OWM_OVERVIEW, mOverview);
            retContent.put(OWM_GRID_POSTER, mPoster);
            retContent.put(OWM_DATE, mDate);
            retContent.put(OWM_RATE, mRate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retContent;
    }


    @Override
    protected Integer doInBackground(String... mString) {
        int inserted_counter=0;
        String sort_mode=Utility.getsortmethod(mContext);

        /* parsing the JSON format will perorm one or more operation
        *  Adding movies only the NEW MOVIES , which mean that some of fetched movies not available in db , that movie tage will
        *  be save into Insert_movies ArrayList.
        *  Update movies , the already exsisted movies will not be inserted again ,instead of that it will be updated
        *  Referenced by Updated_movies ArrayList.
        *  Delete Movies , the movies which exist in db but not existed in fetched movies will be deleted
        *  and it's ref. by movie tags in Deleted_movies ArrayList
        */

        List<Integer> Available_movies=new ArrayList<Integer>();
        List<Integer> Insert_movies;
        List<Integer> Deleted_movies;
        List<Integer> Updated_Movies;
        List<Integer> Movies=new ArrayList<Integer>();

        if(mString[0]!=null) {

            try {
                JSONObject big_obj = new JSONObject(mString[0]);
                JSONArray results_array = big_obj.getJSONArray(OWM_RESULTS);
                /* using the SparseArray to easily insert the movies contentvalues referenced by it's tag to handle it later with
                 ArrayList of tags*/
                SparseArray<ContentValues> movies_parser =new SparseArray<ContentValues>(results_array.length());

                for (int index = 0; index < results_array.length(); index++) {
                    JSONObject Movie_object = results_array.getJSONObject(index);
                    ContentValues mContent = GET_MOVIE_CONTENT(Movie_object);
                    // insert into parse array movie_tag as key, and content Values as value
                    movies_parser.append(mContent.getAsInteger(OWM_TAG),mContent);
                }

                if (movies_parser.size() > 0) {
                    Uri Content_uri = null;
                    //Define the Content_uri which will be used later with provider according to sorting mode
                    if (sort_mode.equals(mContext.getString(R.string.sort_popularity_desc)))
                        Content_uri = MoviesContract.POP_MOVIES_TABLE.CONTENT_URI;

                    else if (sort_mode.equals(mContext.getString(R.string.sort_vote_average_desc)))
                        Content_uri = MoviesContract.VOTE_DESC_TABLE.CONTENT_URI;

                    //get Current Avail Movies in database
                    Cursor get_movies=mContext.getContentResolver().query(
                            Content_uri,
                            new String[]{OWM_TAG},
                            null,
                            null,
                            null);

//                    Add the Current movies if existed into Available_movies Arraylist
                        if(get_movies.moveToFirst()){
                            int mIndex=0;
                            do {
                                Available_movies.add(get_movies.getInt(get_movies.getColumnIndex(OWM_TAG)));
                                mIndex++;
                            }while (get_movies.moveToNext());
                        }

                    //get Fetched Movies List into Movies ArrayList
                    for (int movie_index = 0; movie_index < movies_parser.size(); movie_index++) {
                        Movies.add(movies_parser.valueAt(movie_index).getAsInteger(OWM_TAG));
                    }

                    //List of movies which will be inserted
                    Insert_movies =new ArrayList<Integer>(Movies);
                    Insert_movies.removeAll(Available_movies);

                    /*for(int  x: Insert_movies){
                        Log.i("NewMovies",Integer.toString(x));
                    }
                    */

                    //List of movies which will be Deleted
                    Deleted_movies=new ArrayList<>(Available_movies);
                    Deleted_movies.removeAll(Movies);

                    /*for(int x:Deleted_movies){
                        Log.i("DeletedMovies",Integer.toString(x));
                    }*/

                    //Movies which will be Updated into Updated_Movies ArrayList
                    Updated_Movies=new ArrayList<Integer>(Available_movies);
                    Updated_Movies.removeAll(Deleted_movies);

                    /*for(int x:Updated_Movies){
                        Log.i("UpdatedMovies",Integer.toString(x));
                    }*/

                    //Using Bulk insert of the new Movies
                    if (Insert_movies.size()>0) {
                        ContentValues[] temp_insert = new ContentValues[Insert_movies.size()];
                        for (int temp_index = 0; temp_index < temp_insert.length; temp_index++) {
                            temp_insert[temp_index] = movies_parser.get(Insert_movies.get(temp_index));
                        }
                        int records_number=mContext.getContentResolver().bulkInsert(Content_uri,temp_insert);
//                        Log.i("Records Inserted",Integer.toString(records_number));
                    }

                    //update the Common Movies
                    if(Updated_Movies.size()>0){
                        ContentValues[] temp_update = new ContentValues[Updated_Movies.size()];
                        int records_number=0;
                        for (int temp_index = 0; temp_index < temp_update.length; temp_index++) {
                            Uri update_uri=Content_uri.buildUpon().appendPath(Integer.toString(Updated_Movies.get(temp_index))).build();
                            temp_update[temp_index] = movies_parser.get(Updated_Movies.get(temp_index));
//                            Log.i("UPDATED_URI",update_uri.toString());
                            int record_updated=mContext.getContentResolver().update(update_uri,temp_update[temp_index],null,null);
                            records_number++;
                        }
//                        Log.i("Records updates",Integer.toString(records_number));
                    }

                    //delete Old movies
                    if(Deleted_movies.size()>0){
                        int records_number=0;
                        for(int delete_index =0; delete_index <Deleted_movies.size(); delete_index++){
                        Uri Deleted_Movie_Uri=Content_uri.buildUpon().appendPath(Integer.toString(Deleted_movies.get(delete_index))).build();
                        int _del=mContext.getContentResolver().delete(Deleted_Movie_Uri,null,null);
                        if(_del==1)records_number++;
                        }
//                        Log.i("Records deleted",Integer.toString(records_number));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return inserted_counter;
    }
}
