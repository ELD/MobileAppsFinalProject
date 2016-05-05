package com.fandb.twovietimes;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterListActivity extends SingleFragmentActivity implements TheaterListFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        APIHandler.mContext = getApplicationContext();
        APIHandler.mActivity = this;
        return new TheaterListFragment();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        if(grantResults[0] != getPackageManager().PERMISSION_GRANTED){
            Log.d(APIHandler.TAG, "Location Denied, using 80401");
            APIHandler.mZip = "80401";
        }
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onTheaterSelected(Theater theater) {
        APIHandler.mTheatreId = theater.getAPIID();
        Intent intent = TheaterFragmentActivity.newIntent(this, theater.getId());
        startActivity(intent);
    }

}
