package com.fandb.twovietimes;

import android.util.Log;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.UUID;

/**
 * Created by eric on 4/10/16.
 */
public class MoviePair {
    private UUID mPairId;
    private Movie mMovieOne;
    private Movie mMovieTwo;
    private Date mMovieOneStart;
    private Date mMovieOneEnd;
    private Date mMovieTwoStart;
    private Date mMovieTwoEnd;
    private long mDiff;

    public UUID getPairId() {
        return mPairId;
    }

    public String getMovieOneTitle() {
        return mMovieOne.getTitle();
    }

    public Integer getMovieOneDuration() {
        return Integer.getInteger(mMovieOne.getmDur());
    }

    public Date getMovieOneEnd() {
        return mMovieOneEnd;
    }

    public void setMovieOneEnd(Date movieOneEnd) {
        mMovieOneEnd = movieOneEnd;
    }

    public Date getMovieOneStart() {
        return mMovieOneStart;
    }

    public void setMovieOneStart(Date movieOneStart) {
        mMovieOneStart = movieOneStart;
    }

    public String getMovieTwoTitle() {
        return mMovieTwo.getTitle();
    }

    public Integer getMovieTwoDuration() {
        return Integer.getInteger(mMovieTwo.getmDur());
    }

    public Date getMovieTwoEnd() {
        return mMovieTwoEnd;
    }

    public void setMovieTwoEnd(Date movieTwoEnd) {
        mMovieTwoEnd = movieTwoEnd;
    }

    public Date getMovieTwoStart() {
        return mMovieTwoStart;
    }

    public void setMovieTwoStart(Date movieTwoStart) {
        mMovieTwoStart = movieTwoStart;
    }



    public MoviePair(Movie movieOne, Movie movieTwo, Integer durationOne, Integer durationTwo,
                     Date startOne, Date endOne, Date startTwo, Date endTwo) {
        mPairId = UUID.randomUUID();
        mMovieOne = movieOne;
        mMovieTwo = movieTwo;
        mMovieOneStart = startOne;
        mMovieOneEnd = endOne;
        mMovieTwoStart = startTwo;
        mMovieTwoEnd = endTwo;
    }

    @Override
    public boolean equals(Object mp){
        MoviePair a = (MoviePair)mp;
        boolean ret = false;
        if(mMovieOne.getTitle() == a.getMovieOneTitle() && mMovieTwo.getTitle() == a.getMovieTwoTitle()) ret = true;

        return ret;

    }

    @Override
    public int hashCode(){
        int hash = 3;
        hash = 53 * hash + (mMovieOne.getTitle().hashCode() + mMovieTwo.getTitle().hashCode());
        return hash;
    }

    public MoviePair(Movie movieOne, Movie movieTwo, Integer durationOne, Integer durationTwo,
                     Date startOne, Date startTwo) {
        mPairId = UUID.randomUUID();
        mMovieOne = movieOne;
        mMovieTwo = movieTwo;
        mMovieOneStart = startOne;
        mMovieTwoStart = startTwo;

        Calendar m1end = Calendar.getInstance();
        m1end.setTime(mMovieOneStart);
        //.add(Calendar.MINUTE, getMovieOneDuration());
        mMovieOneEnd = m1end.getTime();

        Calendar m2end = Calendar.getInstance();
        m2end.setTime(mMovieTwoStart);
        //m2end.add(Calendar.MINUTE, getMovieTwoDuration());
        mMovieTwoEnd = m2end.getTime();

        mDiff = Math.abs(startOne.getTime() - startTwo.getTime());
    }

    public boolean movieOneStartBeforeMovieTwo(){
        return mMovieOneStart.before(mMovieTwoStart);
    }

    public boolean movieOneEndsBeforeMovieTwo(){
        return mMovieOneEnd.before(mMovieTwoEnd);
    }

    public String getMovieOneStartString(){
        String timeFormat = "KK:mm";
        Calendar time = Calendar.getInstance();
        return DateFormat.format(timeFormat, mMovieOneStart).toString();
    }

    public String getMovieOneEndString(){
        String timeFormat = "KK:mm";
        Calendar time = Calendar.getInstance();
        return DateFormat.format(timeFormat, mMovieOneEnd).toString();
    }

    public String getMovieTwoStartString(){
        String timeFormat = "KK:mm";
        Calendar time = Calendar.getInstance();
        return DateFormat.format(timeFormat, mMovieTwoStart).toString();
    }

    public String getMovieTwoEndString(){
        String timeFormat = "KK:mm";
        Calendar time = Calendar.getInstance();
        return DateFormat.format(timeFormat, mMovieTwoEnd).toString();
    }

    public String getMovieOneTimesString(){
        return getMovieOneStartString() + " - " + getMovieOneEndString();
    }

    public String getMovieTwoTimesString(){
        return getMovieTwoStartString() + " - " + getMovieTwoEndString();
    }

}
