package com.example.mekawy.popmovies;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mekawy.popmovies.Data.dbContract;

public class movie_detailsAdapter extends CursorAdapter{


    private final static int TYPES_COUNT=2;

    private final static int TRAILER_TYPE=0;
    private final static int REVIEW_TYPE=1;


    public static class Review_ViewHolder{
        public final TextView review_author;
        public final TextView review_content;

        public Review_ViewHolder(View view){
            review_author=(TextView) view.findViewById(R.id.review_author);
            review_content=(TextView) view.findViewById(R.id.review_content);
        }
    }



    public static class TRAILER_ViewHolder{
        public final TextView Trailer_name;

        public TRAILER_ViewHolder(View view){
            Trailer_name=(TextView) view.findViewById(R.id.trailer_name);
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPES_COUNT;
    }



    @Override
    public int getItemViewType(int position) {
       Cursor c=(Cursor) getItem(position);

        int type=c.getInt(c.getColumnIndex(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_TYPE));

        if(type==dbContract.DETAILS_TYPE_TRAILER) return TRAILER_TYPE;
        else if(type==dbContract.DETAILS_TYPE_REVIEWS) return REVIEW_TYPE;

       else return -1;
    }

    public movie_detailsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        //why

        int ViewType=getItemViewType(cursor.getPosition());
        int layid=-1;

        switch (ViewType){
            case TRAILER_TYPE:{
                layid=R.layout.trailer_item;
            break;
            }
            case REVIEW_TYPE:{
                layid=R.layout.review_item;
                break;
            }
        }

        View view= LayoutInflater.from(context).inflate(layid,viewGroup,false);

        if(ViewType==TRAILER_TYPE){
            TRAILER_ViewHolder tHolder=new TRAILER_ViewHolder(view);
            view.setTag(tHolder);
        }

        if(ViewType==REVIEW_TYPE){
            Review_ViewHolder rHolder=new Review_ViewHolder(view);
            view.setTag(rHolder);
        }
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int ViewType=getItemViewType(cursor.getPosition());

        if(ViewType==TRAILER_TYPE) {
            TRAILER_ViewHolder mHolder=(TRAILER_ViewHolder) view.getTag();
            mHolder.Trailer_name.setText(cursor.getString(cursor.getColumnIndex(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_CONTENT)));
        }

        else if((ViewType==REVIEW_TYPE)){
            Review_ViewHolder mHolder=(Review_ViewHolder) view.getTag();
            mHolder.review_author.setText(cursor.getString(cursor.getColumnIndex(dbContract.TRAILER_REVIEWS_TABLE.OWM_COLUMN_CONTENT)));

        }

    }


}
