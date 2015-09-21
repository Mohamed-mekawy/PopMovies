package com.example.mekawy.popmovies;


public class Movie_object {

    private String Original_title;
    private String mainPoster;
    private String Poster_thumbnail;
    private String Overview;
    private double Rating;
    private String Release_date;



    public void set_Original_title(String name){
        this.Original_title=name;
    }

    public void set_Grid_Poster(String name){
        this.mainPoster=name;
    }

    public void set_thum_Poster(String name){
        this.Poster_thumbnail=name;
    }

    public void set_Overview(String name){
        this.Overview=name;
    }

    public void set_Release_date(String name){
        this.Release_date=name;
    }

    public void set_Rating(double name){
        this.Rating=name;
    }


    public String get_Original_title(){
        return this.Original_title;
    }

    public String get_Grid_Poster(){
        return this.mainPoster;
    }

    public String get_thum_Poster(){
        return this.Poster_thumbnail;
    }

    public String get_Overview(){
       return this.Overview;
    }

    public String get_Release_date(){
        return this.Release_date;
    }

    public double get_Rating(){
        return this.Rating;
    }




}
