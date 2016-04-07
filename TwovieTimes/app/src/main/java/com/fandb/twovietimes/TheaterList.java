package com.fandb.twovietimes;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterList {

    private static TheaterList sTheaterList;
    private Context mContext;

    private List<Theater> mTheaters;

    private TheaterList(Context context) {
        mContext = context.getApplicationContext();
        mTheaters = new ArrayList<>();
        mTheaters.add(new Theater("THEATER 1", "2 Miles"));
        mTheaters.add(new Theater("THEATER 2", "4 Miles"));
    }

    public static TheaterList get(Context context) {
        if (sTheaterList == null) {
            sTheaterList = new TheaterList(context);
        }
        return sTheaterList;
    }

    public Theater getTheater(UUID id) {
        for (Theater theater : mTheaters) {
            if (theater.getId().equals(id)) {
                return theater;
            }
        }
        return null;
    }

    public List<Theater> getTheaters() {
        return mTheaters;
    }
}
