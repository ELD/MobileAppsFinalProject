package com.fandb.twovietimes;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterList {

    private static TheaterList sTheaterList;
    private Context mContext;

    public static List<Theater> mTheaters;

    private TheaterList(Context context) {
        mContext = context.getApplicationContext();
        mTheaters = new ArrayList<>();
        mTheaters.add(new Theater("Loading...", ""));
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
