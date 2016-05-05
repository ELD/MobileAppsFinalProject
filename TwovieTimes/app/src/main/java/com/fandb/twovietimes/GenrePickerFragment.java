package com.fandb.twovietimes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 5/2/16.
 */
public class GenrePickerFragment extends DialogFragment {

    public static final String EXTRA_GENRE = "com.fandb.twoietimes.genre";
    public static final String EXTRA_IS_MOVIE = "com.fandb.twoietimes.isMovie";

    private Boolean selectedMovies;

    private LinearLayout mButtonLayout;

    private RecyclerView mGenreRecycleView;
    private GenreAdapter mGenreAdapter;

    private Button mMovieButton;
    private Button mGenreButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.genre_picker, null);

        selectedMovies = null;

        mGenreRecycleView = (RecyclerView) v.findViewById(R.id.genre_recycler_view);
        mGenreRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mButtonLayout = (LinearLayout) v.findViewById(R.id.genre_movie_button_view);

        mMovieButton = (Button) v.findViewById(R.id.select_movie_button);
        mGenreButton = (Button) v.findViewById(R.id.select_genre_button);

        mMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMovies = true;

                //for testing: begin
                ArrayList<String> stuffForTesting = APIHandler.getMovieNames();

                updateUIWithGenres(stuffForTesting);
            }
        });

        mGenreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMovies = false;

                ArrayList<String> stuffForTesting = APIHandler.getGenres();

                updateUIWithGenres(stuffForTesting);
            }
        });



        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .create();
    }

    public void updateUIWithGenres(ArrayList<String> genres) {
        mButtonLayout.setVisibility(View.GONE);
        mGenreAdapter = new GenreAdapter(genres);
        mGenreRecycleView.setAdapter(mGenreAdapter);
        mGenreAdapter.notifyDataSetChanged();
    }

    private class GenreHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private String mGenre;

        private TextView mGenreTextView;
        private LinearLayout mLayout;

        public GenreHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mGenreTextView = (TextView) itemView.findViewById(R.id.genre_title);
            mLayout = (LinearLayout) itemView.findViewById(R.id.genre_list_item_layout);

        }

        public void bindGenre(String genre, Integer currentSelected, int position) {
            mGenre = genre;
            mGenreTextView.setText(genre);
            if (currentSelected != null){
                if (currentSelected.equals(position)){
                    mLayout.setBackgroundColor(0xFF00FF00);
                }
            }
        }

        @Override
        public void onClick(View v) {
            //stuff goes here
            //mLayout.setBackgroundColor(getResources().getColor(R.color.colorGray));
            //mLayout.setBackgroundColor(0xFF00FF00);
            //sendResult(Activity.RESULT_OK, mGenre, selectedMovies);
            Log.d("STUFF", "onClick: CALLED position: " + getLayoutPosition());
            mGenreAdapter.setSelected(getLayoutPosition());
            mGenreAdapter.notifyDataSetChanged();
            //mGenreAdapter.notifyAll();

        }
    }
    private class GenreAdapter extends RecyclerView.Adapter<GenreHolder> {
        private List<String> mGenres;
        private Integer mSelected;

        public void setSelected(int selected){
            mSelected = selected;
        }

        public GenreAdapter(List<String> genres) {
            mGenres = genres;
        }

        @Override
        public GenreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.genre_list_item, parent, false);
            return new GenreHolder(view);
        }

        @Override
        public void onBindViewHolder(GenreHolder holder, int position) {
            String genre = mGenres.get(position);
            holder.bindGenre(genre, mSelected, position);
        }

        @Override
        public int getItemCount() {
            return mGenres.size();
        }
    }

    private void sendResult(int resultCode, String genre, boolean isMovie) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_GENRE, genre);
        intent.putExtra(EXTRA_IS_MOVIE, isMovie);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
