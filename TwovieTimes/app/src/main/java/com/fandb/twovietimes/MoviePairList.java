package com.fandb.twovietimes;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
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
        mMoviePairs.add(
                new MoviePair("Movie One", "Movie Two", 90, 105, new Date(), new Date(),
                        new Date(), new Date())
        );

        mMoviePairs.add(
                new MoviePair("Movie Three", "Movie Four", 90, 105, new Date(), new Date(),
                        new Date(), new Date())
        );
    }

    public static MoviePairList get(Context context) {
        if (sMoviePairList == null) {
            sMoviePairList = new MoviePairList(context);
        }

        return sMoviePairList;
    }

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
