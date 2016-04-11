package com.fandb.twovietimes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterActivity extends AppCompatActivity{

    private static final String EXTRA_THEATER_ID = "com.fandb.twovietimes.crime_id";

    private RecyclerView mMovieRecylcerView;
    private MovieAdapter mMovieAdapter;
    private Theater mTheater;
    private Button mGetMoviesButton;

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

        mGetMoviesButton = (Button) findViewById(R.id.get_movie_times);
        mGetMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Network update movie list
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
            mMovieAdapter = new MovieAdapter(moviePairs);
            mMovieRecylcerView.setAdapter(mMovieAdapter);
        } else {
            mMovieAdapter.notifyDataSetChanged();
        }
    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        private MoviePair mMoviePair;

        private TextView mMovieOneTextView;
        private TextView mMovieOneStart;
        private TextView mMovieOneDuration;
        private TextView mMovieTwoTextView;
        private TextView mMovieTwoStart;
        private TextView mMovieTwoDuration;

        public MovieHolder(View itemView) {
            super(itemView);

            mMovieOneTextView = (TextView) itemView.findViewById(R.id.movie_one_title);
            mMovieOneStart = (TextView) itemView.findViewById(R.id.movie_one_start);
            mMovieOneDuration = (TextView) itemView.findViewById(R.id.movie_one_runtime);
            mMovieTwoTextView = (TextView) itemView.findViewById(R.id.movie_two_title);
            mMovieTwoStart = (TextView) itemView.findViewById(R.id.movie_two_start);
            mMovieTwoDuration = (TextView) itemView.findViewById(R.id.movie_two_runtime);
        }

        public void bindMovieTime(MoviePair pair) {
            mMoviePair = pair;

            mMovieOneTextView.setText(mMoviePair.getMovieOne());
            mMovieOneStart.setText(mMoviePair.getMovieOneStart().toString());
            mMovieOneDuration.setText(mMoviePair.getMovieOneDuration().toString());
            mMovieTwoTextView.setText(mMoviePair.getMovieTwo());
            mMovieTwoStart.setText(mMoviePair.getMovieTwoStart().toString());
            mMovieTwoDuration.setText(mMoviePair.getMovieTwoDuration().toString());
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private List<MoviePair> mMoviePairs;

        public MovieAdapter(List<MoviePair> pairs) {
            mMoviePairs = pairs;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(TheaterActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_movie_pairing, parent, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            MoviePair pair = mMoviePairs.get(position);
            holder.bindMovieTime(pair);
        }

        @Override
        public int getItemCount() {
            return mMoviePairs.size();
        }
    }
}
