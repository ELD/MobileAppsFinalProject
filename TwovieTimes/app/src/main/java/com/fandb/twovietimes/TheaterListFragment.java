package com.fandb.twovietimes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterListFragment extends Fragment {
    private RecyclerView mTheaterRecyclerView;
    private TheaterAdapter mAdapter;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onTheaterSelected(Theater theater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theater_list, container, false);
        mTheaterRecyclerView = (RecyclerView) view.findViewById(R.id.theater_recycler_view);
        mTheaterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    public void updateUI() {
        TheaterList theaterList = TheaterList.get(getActivity());
        List<Theater> theaters = theaterList.getTheaters();

        if (mAdapter == null) {
            mAdapter = new TheaterAdapter(theaters);
            mTheaterRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
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
            mCallbacks.onTheaterSelected(mTheater);
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
