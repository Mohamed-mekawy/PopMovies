package com.example.mekawy.popmovies;

public enum movies_api_key {
    API_KEY ("XXXXXXXXXXXXXXXXXXXXX");

    private final String value;
    private movies_api_key(String sData){
    value=sData;
    }

    public String get_API_key(){
        return this.value;
    }

}
