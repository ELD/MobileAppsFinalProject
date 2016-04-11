package com.fandb.twovietimes;

import java.util.Date;
import java.util.UUID;

/**
 * Created by eric on 4/10/16.
 */
public class MoviePair {
    private UUID mPairId;
    private String mMovieOne;
    private String mMovieTwo;
    private Integer mMovieOneDuration;
    private Integer mMovieTwoDuration;
    private Date mMovieOneStart;
    private Date mMovieOneEnd;
    private Date mMovieTwoStart;

    public UUID getPairId() {
        return mPairId;
    }

    public String getMovieOne() {
        return mMovieOne;
    }

    public void setMovieOne(String movieOne) {
        mMovieOne = movieOne;
    }

    public Integer getMovieOneDuration() {
        return mMovieOneDuration;
    }

    public void setMovieOneDuration(Integer movieOneDuration) {
        mMovieOneDuration = movieOneDuration;
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

    public String getMovieTwo() {
        return mMovieTwo;
    }

    public void setMovieTwo(String movieTwo) {
        mMovieTwo = movieTwo;
    }

    public Integer getMovieTwoDuration() {
        return mMovieTwoDuration;
    }

    public void setMovieTwoDuration(Integer movieTwoDuration) {
        mMovieTwoDuration = movieTwoDuration;
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

    private Date mMovieTwoEnd;

    public MoviePair(String movieOne, String movieTwo, Integer durationOne, Integer durationTwo,
                     Date startOne, Date endOne, Date startTwo, Date endTwo) {
        mPairId = UUID.randomUUID();
        mMovieOne = movieOne;
        mMovieTwo = movieTwo;
        mMovieOneDuration = durationOne;
        mMovieTwoDuration = durationTwo;
        mMovieOneStart = startOne;
        mMovieOneEnd = endOne;
        mMovieTwoStart = startTwo;
        mMovieTwoEnd = endTwo;
    }
}
