package com.example.mekawy.popmovies;

public enum movies_api_key {
    // You can get your key from themoviedb.org
    API_KEY ("7b5e30851a9285340e78c201c4e4ab99");

    private final String value;
    private movies_api_key(String sData){
    value=sData;
    }
    public String get_API_key(){
        return this.value;
    }

}
