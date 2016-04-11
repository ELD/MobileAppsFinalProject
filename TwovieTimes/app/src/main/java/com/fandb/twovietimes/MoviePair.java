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
