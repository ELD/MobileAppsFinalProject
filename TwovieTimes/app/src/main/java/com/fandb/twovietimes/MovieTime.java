package com.fandb.twovietimes;

import java.util.Date;

/**
 * Created by Alex on 4/9/16.
 */
public class MovieTime {

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
    private Integer mDuration;

    public enum Rating {
        G, PG, PG13, R, NC17
    }


}
