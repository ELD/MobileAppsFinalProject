package com.fandb.twovietimes;

import java.util.Date;

/**
 * Created by Alex on 4/9/16.
 */
public class MovieTime {

    public String getTitle() {
        return mTitle;
    }

    public Rating getmRating() {
        return mRating;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public Date getEndTime() {
        return mEndTime;
    }

    public int getmDuration() {
        return mDuration;
    }

    MovieTime(String tit, Rating r, Date st, Date en, Integer dur){
        mTitle = tit;
        mRating = r;
        mStartTime = st;
        mEndTime = en;
        mDuration = dur;
    }
    private String mTitle;
    private Rating mRating;
    private Date mStartTime;
    private Date mEndTime;
    //duration in minutes (rounded)
    private int mDuration;

    public enum Rating {
        G, PG, PG13, R, NC17
    }


}
