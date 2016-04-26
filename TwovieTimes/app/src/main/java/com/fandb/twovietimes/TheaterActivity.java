package com.fandb.twovietimes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterActivity extends AppCompatActivity{

    private static final String TAG = "THEATER_ACTIVITY";
    private static final String EXTRA_THEATER_ID = "com.fandb.twovietimes.theater_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private RecyclerView mMovieRecylcerView;
    private MoviePairAdapter mMovieAdapter;
    private Theater mTheater;
    private Button mGetMoviesButton;
    private Button mLaterButton;
    private CheckBox mNowCheckbox;

    private Date mMovieDate;

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

        mMovieRecylcerView = (RecyclerView) findViewById(R.id.theater_movie_times_recycler_view);
        mMovieRecylcerView.setLayoutManager(new LinearLayoutManager(this));

        mLaterButton = (Button) findViewById(R.id.movies_later_button);
        mLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TimePickerFragment tpf = new TimePickerFragment();
//
//                tpf.show(getSupportFragmentManager(), DIALOG_DATE);
                LayoutInflater inflater = LayoutInflater.from(TheaterActivity.this);
                final View timepicker = inflater.inflate(R.layout.date_picker, null);

                final DatePicker picker = (DatePicker) timepicker.findViewById(R.id.dialog_date_picker);
                AlertDialog ad = new AlertDialog.Builder(TheaterActivity.this)
                        .setTitle(R.id.dialog_date_picker)
                        .setView(timepicker)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Calendar cal = Calendar.getInstance();
                                cal.clear();
                                cal.set(Calendar.YEAR, picker.getYear());
                                cal.set(Calendar.MONTH, picker.getMonth());
                                cal.set(Calendar.DAY_OF_MONTH, picker.getDayOfMonth());
                                mMovieDate = cal.getTime();
                                Log.d(TAG, mMovieDate.toString());
                            }
                        })
                        .setNegativeButton(null, null).create();

                ad.show();
            }
        });

        mGetMoviesButton = (Button) findViewById(R.id.get_movie_times);
        mGetMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Network update movie list
            }
        });

        mNowCheckbox = (CheckBox) findViewById(R.id.movies_now_checkbox);
        mNowCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mMovieDate = new Date();
                }
            }
        });

        // TODO: Populate movie lists
        // TODO: Make DatePicker

        updateUI();
    }

    public void updateUI() {
        MoviePairList moviePairList = MoviePairList.get(this);
        List<MoviePair> moviePairs = moviePairList.getMoviePairs();

        if (mMovieAdapter == null) {
            //mMovieAdapter = new MovieAdapter(moviePairs);
            mMovieAdapter = new MoviePairAdapter(moviePairs);
            mMovieRecylcerView.setAdapter(mMovieAdapter);
        } else {
            mMovieAdapter.notifyDataSetChanged();
        }
    }


    private class MoviePairHolder extends RecyclerView.ViewHolder {
        //private MoviePair mMoviePair;
        private View mMoviePairView;

        public MoviePairHolder(View itemView) {
            super(itemView);
            mMoviePairView = itemView;
            //mMoviePairView = new MoviePairView(itemView.getContext(), mMoviePair, itemView.getWidth(), itemView.getHeight());

            //itemView.view
        }
        public void bindMoviePair(MoviePair pair) {
            FrameLayout holder = (FrameLayout) itemView.findViewById(R.id.holder);
            mMoviePairView = new MoviePairView(itemView.getContext(), pair, getWindowManager().getDefaultDisplay().getWidth());

            holder.addView(mMoviePairView);

            mMoviePairView.invalidate();
        }

    }

    private class MoviePairAdapter extends RecyclerView.Adapter<MoviePairHolder> {
        private List<MoviePair> mMoviePairs;

        public MoviePairAdapter(List<MoviePair> pairs) {
            mMoviePairs = pairs;
        }

        @Override
        public MoviePairHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(TheaterActivity.this);
            View view = layoutInflater.inflate(R.layout.movie_pair_holder, parent, false);


            return new MoviePairHolder(view);
        }

        @Override
        public void onBindViewHolder(MoviePairHolder holder, int position) {
            MoviePair pair = mMoviePairs.get(position);
            holder.bindMoviePair(pair);
        }

        @Override
        public int getItemCount() {
            return mMoviePairs.size();
        }


    }

}
