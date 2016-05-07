package com.fandb.twovietimes;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;


public class SettingsActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SettingsActivity.class);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }

}
