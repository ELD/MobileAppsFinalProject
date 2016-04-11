package com.fandb.twovietimes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        mGetMoviesButton = (Button) findViewById(R.id.get_movie_times);
        mGetMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Network update movie list
            }
        });

        // TODO: Populate movie lists
        // TODO: Make DatePicker
    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        private MoviePair mMoviePair;
        public MovieHolder(View itemView) {
            super(itemView);
        }

        public void bindMovieTime(MoviePair pair) {
            mMoviePair = pair;
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private List<MoviePair> mMoviePairs;

        public MovieAdapter(List<MoviePair> pairs) {
            mMoviePairs = pairs;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
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
