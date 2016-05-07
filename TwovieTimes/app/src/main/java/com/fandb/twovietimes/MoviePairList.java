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
