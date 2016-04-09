package com.fandb.twovietimes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterActivity extends AppCompatActivity{

    private static final String EXTRA_THEATER_ID = "com.fandb.twovietimes.crime_id";

    private Theater mTheater;

    public static Intent newIntent(Context packageContext, UUID theaterId) {
        Intent intent = new Intent(packageContext, TheaterActivity.class);
        intent.putExtra(EXTRA_THEATER_ID, theaterId);
        return intent;
    }
/*
    @Override
    protected Fragment createFragment() {
        UUID theaterId = (UUID) getIntent().getSerializableExtra(EXTRA_THEATER_ID);
        return TheaterFragment.newInstance(theaterId);
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_theater);

        UUID theaterId = (UUID) getIntent().getSerializableExtra(EXTRA_THEATER_ID);
        mTheater = TheaterList.get(this).getTheater(theaterId);
    }

}
