package com.example.mekawy.popmovies;


public class Movie_object {

    private String Original_title;
    private String mainPoster;
    private String Poster_thumbnail;
    private String Overview;
    private double Rating;
    private String Release_date;


    public void Movie_object(String title,String main_poster,String thum_poster,String desc,String date,double rate){
        Original_title=title;
        mainPoster=main_poster;
        Poster_thumbnail=thum_poster;
        Overview=desc;
        Release_date=date;
        Rating=rate;
    }

    //to return the address of main poster
    public String get_main_poster (){
        return this.mainPoster;
    }

}
