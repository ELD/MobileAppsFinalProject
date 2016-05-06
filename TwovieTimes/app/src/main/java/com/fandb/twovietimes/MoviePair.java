package com.fandb.twovietimes;

import java.util.Date;
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

    private Date mMovieTwoEnd;

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
}
