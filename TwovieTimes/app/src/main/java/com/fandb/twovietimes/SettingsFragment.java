package com.fandb.twovietimes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Created by eric on 5/6/16.
 */
public class SettingsFragment extends Fragment {
    EditText mRadius;
    EditText mZip;

    Button mApply;

    @Override
    public void onCreate(@Nullable Bundle savedInstance){
         super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_settings, container, false);

        mRadius = (EditText) v.findViewById(R.id.radius);
        mZip = (EditText) v.findViewById(R.id.zip);

        mApply = (Button) v.findViewById(R.id.apply);
        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRadius.getText().length() > 0){
                    APIHandler.mRadius = Integer.parseInt(mRadius.getText().toString());
                    Log.d("api", String.valueOf(APIHandler.mRadius));
                }
                if(mZip.getText().length() == 5){
                    APIHandler.mZip = mZip.getText().toString();
                    Log.d("api", (APIHandler.mZip));
                }
            };
        });

        return v;
    }
}
