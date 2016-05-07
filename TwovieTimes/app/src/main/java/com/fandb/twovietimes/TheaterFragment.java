package com.fandb.twovietimes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

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

    private MoviePairList mMoviePairList;

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

        mMovieDate = new Date();

        mMoviePairList = MoviePairList.get(getActivity());

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
                Log.d(TAG, "onClick: Spawn date picker!");
                TimePickerFragment dialog = TimePickerFragment.newInstance(mMovieDate);
                dialog.setTargetFragment(TheaterFragment.this, REQUEST_TIME);

                dialog.show(getFragmentManager(), DIALOG_DATE);
            }
        });

        mGetMoviesButton = (Button) v.findViewById(R.id.get_movie_times);
        mGetMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!leftSelectionIsMovie || !rightSelectionIsMovie) && (rightSelectedMovieOrGenre == null || leftSelectedMovieOrGenre == null))
                    Toast.makeText(getActivity(), "Need to select two movies/genres first!", Toast.LENGTH_LONG).show();
                else if(leftSelectedMovieOrGenre.equals(rightSelectedMovieOrGenre) && (leftSelectionIsMovie == true && rightSelectionIsMovie == true))
                    Toast.makeText(getActivity(), "Can\'t select two of the same movie", Toast.LENGTH_LONG).show();
                else if(leftSelectionIsMovie && rightSelectionIsMovie) {
                    Log.d(TAG, String.valueOf(APIHandler.getMoviePairs(leftSelectedMovieOrGenre, rightSelectedMovieOrGenre)));
                    mMoviePairList.setMoviePairList(APIHandler.getMoviePairs(leftSelectedMovieOrGenre, rightSelectedMovieOrGenre));
                }
                else if(!leftSelectionIsMovie && !rightSelectionIsMovie) {
                    mMoviePairList.setMoviePairList(APIHandler.getMoviePairsGenres(leftSelectedMovieOrGenre, rightSelectedMovieOrGenre));
                }

                else {
                    String mov = "";
                    String gen = "";
                    if (leftSelectionIsMovie) {
                        mov = leftSelectedMovieOrGenre;
                        gen = rightSelectedMovieOrGenre;
                    } else {
                        mov = rightSelectedMovieOrGenre;
                        gen = leftSelectedMovieOrGenre;
                    }
                    mMoviePairList.setMoviePairList(APIHandler.getMoviePairsGenre(mov, gen));
                }
                if(mMoviePairList.getMoviePairListLength() < 1){
                    Toast.makeText(getActivity(), "No movies with the selected criteria", Toast.LENGTH_LONG).show();
                }
                updateUI();
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

        updateUI();

        return v;
    }

    public void updateUI() {
        if (mMovieAdapter == null) {
            mMovieAdapter = new MoviePairAdapter(mMoviePairList.getMoviePairs());
            mMovieRecylcerView.setAdapter(mMovieAdapter);
        } else {
            mMovieAdapter.setMovies(mMoviePairList.getMoviePairs());
            mMovieAdapter.notifyDataSetChanged();
        }
    }

    private class MoviePairHolder extends RecyclerView.ViewHolder {
        private View mMoviePairView;

        public MoviePairHolder(View itemView) {
            super(itemView);
            mMoviePairView = itemView;
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

        public void setMovies(List<MoviePair> movies) {
            mMoviePairs = movies;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: HELLOOOO request code: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: HELLOOOO BAD");
            return;
        } else if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_DATE);
            mMovieDate = date;
            Log.d(TAG, "onActivityResult: " + mMovieDate);
        } else if (requestCode == REQUEST_GENRE_LEFT){
            String genre = data.getStringExtra(GenrePickerFragment.EXTRA_GENRE);
            boolean isMovie = data.getBooleanExtra(GenrePickerFragment.EXTRA_IS_MOVIE, false);

            leftSelectedMovieOrGenre = genre;
            leftSelectionIsMovie = isMovie;

            String[] genreWords = genre.split(" ");
            if (isMovie){
                //mLeftGenreButton.setText(genreWords[0] + " " + genreWords[1] + "...");
                if (genre.length() >= 11){
                    mLeftGenreButton.setText(genre.substring(0, 11) + "...");
                }
                else{
                    mLeftGenreButton.setText(genre);
                }

            }
            else{
                mLeftGenreButton.setText(genre);
            }

        } else if (requestCode == REQUEST_GENRE_RIGHT){
            String genre = data.getStringExtra(GenrePickerFragment.EXTRA_GENRE);
            boolean isMovie = data.getBooleanExtra(GenrePickerFragment.EXTRA_IS_MOVIE, false);

            rightSelectedMovieOrGenre = genre;
            rightSelectionIsMovie = isMovie;

            String[] genreWords = genre.split(" ");
            if (isMovie){
                //mRightGenreButton.setText(genreWords[0] + " " + genreWords[1] + "...");
                if (genre.length() >= 11){
                    mRightGenreButton.setText(genre.substring(0, 11) + "...");
                }
                else{
                    mRightGenreButton.setText(genre);
                }
            } else {
                mRightGenreButton.setText(genre);
            }
        }
    }
}
