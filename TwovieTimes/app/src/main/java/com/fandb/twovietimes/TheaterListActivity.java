package com.fandb.twovietimes;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterListActivity extends SingleFragmentActivity implements TheaterListFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new TheaterListFragment();
    }


    @Override
    public void onTheaterSelected(Theater theater) {
        Intent intent = TheaterActivity.newIntent(this, theater.getId());
        startActivity(intent);
    }

}
