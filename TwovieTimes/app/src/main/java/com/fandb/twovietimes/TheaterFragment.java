package com.fandb.twovietimes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
 * Created by eric on 5/5/16.
 */
public class TheaterFragment extends Fragment {
    public static String ARG_THEATER_ID = "theater_id";
    private static final String TAG = "THEATER_ACTIVITY";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_GENRE = "DialogGenre";
    private static final int REQUEST_TIME = 0;
    private static final int REQUEST_GENRE_LEFT = 1;
    private static final int REQUEST_GENRE_RIGHT = 2;


    private static final int REQUEST_DATE = 0;

    private RecyclerView mMovieRecylcerView;
    private MoviePairAdapter mMovieAdapter;
    private Theater mTheater;
    private Button mGetMoviesButton;
    private Button mLaterButton;
    private CheckBox mNowCheckbox;

    private Button mLeftGenreButton;
    private Button mRightGenreButton;

    private Date mMovieDate;

    //these probably won't be in the final design, but this is where these values are stored for now:
    private String leftSelectedMovieOrGenre;
    private boolean leftSelectionIsMovie;
    private String rightSelectedMovieOrGenre;
    private boolean rightSelectionIsMovie;

    public static TheaterFragment newInstance(UUID theaterId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_THEATER_ID, theaterId);
        TheaterFragment fragment = new TheaterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID theaterId = (UUID) getArguments().getSerializable(ARG_THEATER_ID);
        mTheater = TheaterList.get(getActivity()).getTheater(theaterId);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_theater, container, false);

        mMovieRecylcerView = (RecyclerView) v.findViewById(R.id.theater_movie_times_recycler_view);
        mMovieRecylcerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLaterButton = (Button) v.findViewById(R.id.movies_later_button);
        mLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TimePickerFragment tpf = new TimePickerFragment();
//
//                tpf.show(getSupportFragmentManager(), DIALOG_DATE);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View timepicker = inflater.inflate(R.layout.date_picker, null);

                final DatePicker picker = (DatePicker) timepicker.findViewById(R.id.dialog_date_picker);
                AlertDialog ad = new AlertDialog.Builder(getActivity())
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

        mGetMoviesButton = (Button) v.findViewById(R.id.get_movie_times);
        mGetMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Network update movie list
            }
        });

        mNowCheckbox = (CheckBox) v.findViewById(R.id.movies_now_checkbox);
        mNowCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mMovieDate = new Date();
                }
            }
        });

        mLeftGenreButton = (Button) v.findViewById(R.id.left_genre_button);
        mRightGenreButton = (Button) v.findViewById(R.id.right_genre_button);

        mLeftGenreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenrePickerFragment gp = new GenrePickerFragment();
                gp.setTargetFragment(TheaterFragment.this, REQUEST_GENRE_LEFT);
                gp.show(getFragmentManager(), DIALOG_GENRE);
            }
        });

        mRightGenreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenrePickerFragment gp = new GenrePickerFragment();
                gp.setTargetFragment(TheaterFragment.this, REQUEST_GENRE_RIGHT);
                gp.show(getFragmentManager(), DIALOG_GENRE);
            }
        });

        // TODO: Populate movie lists
        // TODO: Make DatePicker

        updateUI();

        return v;
    }

    public void updateUI() {
        MoviePairList moviePairList = MoviePairList.get(getActivity());
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
            mMoviePairView = new MoviePairView(itemView.getContext(),
                    pair,
                    getActivity().getWindowManager().getDefaultDisplay().getWidth()
            );

            holder.addView(mMoviePairView);

            mMoviePairView.invalidate();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MoviePairAdapter extends RecyclerView.Adapter<MoviePairHolder> {
        private List<MoviePair> mMoviePairs;

        public MoviePairAdapter(List<MoviePair> pairs) {
            mMoviePairs = pairs;
        }

        @Override
        public MoviePairHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: HELLOOOO request code: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: HELLOOOO BAD");
            return;
        }


        if (requestCode == REQUEST_DATE){

        }
        else if (requestCode == REQUEST_GENRE_LEFT){
            String genre = data.getStringExtra(GenrePickerFragment.EXTRA_GENRE);
            boolean isMovie = data.getBooleanExtra(GenrePickerFragment.EXTRA_IS_MOVIE, false);

            leftSelectedMovieOrGenre = genre;
            leftSelectionIsMovie = isMovie;

            String[] genreWords = genre.split(" ");
            if (isMovie){
                //mLeftGenreButton.setText(genreWords[0] + " " + genreWords[1] + "...");
                mLeftGenreButton.setText(genre.substring(0, 11) + "...");
            }
            else{
                mLeftGenreButton.setText(genre);
            }

        }
        else if (requestCode == REQUEST_GENRE_RIGHT){
            String genre = data.getStringExtra(GenrePickerFragment.EXTRA_GENRE);
            boolean isMovie = data.getBooleanExtra(GenrePickerFragment.EXTRA_IS_MOVIE, false);

            rightSelectedMovieOrGenre = genre;
            rightSelectionIsMovie = isMovie;

            String[] genreWords = genre.split(" ");
            if (isMovie){
                //mRightGenreButton.setText(genreWords[0] + " " + genreWords[1] + "...");
                mRightGenreButton.setText(genre.substring(0, 11) + "...");
            }
            else{
                mRightGenreButton.setText(genre);
            }
        }
    }
}
