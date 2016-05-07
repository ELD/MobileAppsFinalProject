package com.fandb.twovietimes;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Alex on 4/6/16.
 * Theater object to hold all info for a theater
 */
public class Theater {
    private UUID mId;
    private String mName;
    private String mDistance;
    private String mAddress;
    private String mAPIId;
    private ArrayList <MovieTime> mMovieTimes;

    public Theater(){
        //Generate unique identifier
        mId = UUID.randomUUID();
    }

    public Theater(String name, String distance) {
        this();
        mName = name;
        mDistance = distance;
    }

    public Theater(String name, String distance, String address, String apiid){
        this(name, distance);
        mAddress = address;
        mAPIId = apiid;
    }

    public UUID getId() {
        return mId;
    }

    public String getName(){
        return mName;
    }

    public String getDistance(){
        return mDistance;
    }

    public String getAPIID(){ return mAPIId; }

    public String getAddress(){ return mAddress; }

    public void setAddress(String address){mAddress = address;}

}
