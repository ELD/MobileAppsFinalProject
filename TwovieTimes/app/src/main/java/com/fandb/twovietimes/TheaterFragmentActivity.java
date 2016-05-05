package com.fandb.twovietimes;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by eric on 5/5/16.
 */
public class TheaterFragmentActivity extends SingleFragmentActivity {
    public static String EXTRA_THEATER_ID = "com.fandb.twovietimes.theater_id";

    public static Intent newIntent(Context packageContext, UUID theaterId) {
        Intent intent = new Intent(packageContext, TheaterFragmentActivity.class);
        intent.putExtra(EXTRA_THEATER_ID, theaterId);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID theaterId = (UUID) getIntent().getSerializableExtra(EXTRA_THEATER_ID);
        return TheaterFragment.newInstance(theaterId);
    }
}
