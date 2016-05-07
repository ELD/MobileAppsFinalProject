package com.fandb.twovietimes;

/**
 * Created by Steve on 4/24/2016.
 */
public class Movie{
    private String mTitle;
    private String[] genres;
    private String releaseDate;
    private String mLongDescrip;
    private String mShortDescrip;
    private String[] mDirectors;
    private String mURL;
    private String mDur;
    private MovieTime.Rating mRat;


    public Movie(String mTitle, String[] genres, String releaseDate, String mLongDescrip, String mShortDescrip, String[] mDirectors, String mURL, String mDur, MovieTime.Rating mRat) {
        this.mTitle = mTitle;
        this.genres = genres;
        this.releaseDate = releaseDate;
        this.mLongDescrip = mLongDescrip;
        this.mShortDescrip = mShortDescrip;
        this.mDirectors = mDirectors;
        this.mURL = mURL;
        this.mDur = APIHandler.parseRunTimeStr(mDur);
        this.mRat = mRat;
    }


    public String getTitle() {
        return mTitle;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getmLongDescrip() {
        return mLongDescrip;
    }

    public String getmShortDescrip() {
        return mShortDescrip;
    }

    public String[] getmDirectors() {
        return mDirectors;
    }

    public String getmURL() {
        return mURL;
    }

    public String getmDur() {
        return mDur;
    }

    public MovieTime.Rating getmRat() {
        return mRat;
    }
}
