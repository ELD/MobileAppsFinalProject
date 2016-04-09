package com.fandb.twovietimes;

import java.util.UUID;

/**
 * Created by Alex on 4/6/16.
 */
public class Theater {
    private UUID mId;
    private String mName;
    //this probably won't end up being a string, but I'll make it one for now
    private String mDistance;
    private String mAddress;

    public Theater(){
        //Generate unique identifier
        mId = UUID.randomUUID();
    }

    public Theater(String name, String distance) {
        mId = UUID.randomUUID();
        mName = name;
        mDistance = distance;
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

}
