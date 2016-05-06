package com.fandb.twovietimes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterListFragment extends Fragment {
    private static String TAG = "TheaterListFragment";

    private RecyclerView mTheaterRecyclerView;
    private TheaterAdapter mAdapter;
    private Callbacks mCallbacks;
    private boolean mDisabled = true;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onTheaterSelected(Theater theater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theater_list, container, false);
        mTheaterRecyclerView = (RecyclerView) view.findViewById(R.id.theater_recycler_view);
        mTheaterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new API().execute(APIType.GetTheaters);

        updateUI(null);
        return view;
    }

    public void updateUI(ArrayList<Theater> test) {
        List<Theater> theaters = null;
        if(test == null){
            TheaterList theaterList = TheaterList.get(getActivity());
            theaters = theaterList.getTheaters();
        }
        else{
            theaters = test;
            mAdapter = null;
        }

        if (mAdapter == null) {
            mAdapter = new TheaterAdapter(theaters);
            mTheaterRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.app_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_button:
                // TODO: Launch settings activity?
                return true;
            case R.id.loc_checkin:
                Toast.makeText(getActivity(), "Getting new location data...", Toast.LENGTH_LONG).show();
                APIHandler.mInit = false;
                ArrayList<Theater> load = new ArrayList<Theater>();
                load.add(new Theater("Loading...", ""));
                updateUI(load);

                new API().execute(APIType.GetTheaters);
                
                updateUI(null);
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Docs: http://developer.tmsapi.com/docs/v2/
    public class API extends AsyncTask<APIType, Integer, ArrayList<Theater> > {
        @Override
        protected ArrayList<Theater> doInBackground(APIType... type) {
            APIHandler.init();

            APIType request = type[0];
            switch(request){
                case GetTheaters:
                    return APIHandler.getTheaters();
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Theater> result){
            super.onPostExecute(result);
            if(result.size() == 0)
                result.add(new Theater("Out of API Calls :(, wait until tomorrow", ""));

            else mDisabled = false;
            updateUI(result);
        }
    }

    private class TheaterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Theater mTheater;

        private TextView mNameTextView;
        private TextView mDistanceTextView;


        public TheaterHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_theater_name_text_view);
            mDistanceTextView = (TextView) itemView.findViewById(R.id.list_item_theater_distance_text_view);

        }

        public void bindTheater(Theater theater) {
            mTheater = theater;
            mNameTextView.setText(mTheater.getName());
            mDistanceTextView.setText(mTheater.getDistance());
        }

        @Override
        public void onClick(View v) {
            if(mDisabled) return;
            Intent intent = TheaterFragmentActivity.newIntent(getActivity(), mTheater.getId());
            startActivity(intent);
            //mCallbacks.onTheaterSelected(mTheater);
        }
    }

    private class TheaterAdapter extends RecyclerView.Adapter<TheaterHolder> {
        private List<Theater> mTheaters;

        public TheaterAdapter(List<Theater> theaters) {
            mTheaters = theaters;
        }

        @Override
        public TheaterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_theater, parent, false);
            return new TheaterHolder(view);
        }

        @Override
        public void onBindViewHolder(TheaterHolder holder, int position) {
            Theater theater = mTheaters.get(position);
            holder.bindTheater(theater);
        }

        @Override
        public int getItemCount() {
            return mTheaters.size();
        }
    }
}
