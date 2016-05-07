package com.fandb.twovietimes;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by eric on 4/10/16.
 */
public class MoviePairList {
    private static MoviePairList sMoviePairList;
    private Context mContext;

    private List<MoviePair> mMoviePairs;

    private MoviePairList(Context context) {
        mContext = context.getApplicationContext();
        mMoviePairs = new ArrayList<>();
        /*
        Movie movie1 = new Movie()
        mMoviePairs.add(
                new MoviePair("Movie One", "Movie Two", 90, 105, new Date(2016, 04, 26, 5, 0), new Date(),
                        new Date(2016, 04, 26, 5, 15), new Date())
        );

        mMoviePairs.add(
                new MoviePair("Movie Three", "Movie Four", 90, 105, new Date(2016, 04, 26, 5, 25), new Date(),
                        new Date(2016, 04, 26, 5, 15), new Date())
        );
        */
    }

    public static MoviePairList get(Context context) {
        if (sMoviePairList == null) {
            sMoviePairList = new MoviePairList(context);
        }

        return sMoviePairList;
    }

    public void setMoviePairList(List<MoviePair> pairs) {
        mMoviePairs = pairs;
    }

    public int getMoviePairListLength(){ return mMoviePairs.size(); }

    public MoviePair getPair(UUID id) {
        for (MoviePair pair : mMoviePairs) {
            if (pair.getPairId().equals(id)) {
                return pair;
            }
        }

        return null;
    }

    public List<MoviePair> getMoviePairs() {
        return mMoviePairs;
    }
}
