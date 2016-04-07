package com.fandb.twovietimes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

/**
 * Created by Alex on 4/6/16.
 */
public class TheaterFragment extends Fragment{

    private static final String ARG_THEATER_ID = "theater_id";

    private Theater mTheater;
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
        mCallbacks = (Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static TheaterFragment newInstance(UUID theaterId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_THEATER_ID, theaterId);

        TheaterFragment fragment = new TheaterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID theaterId = (UUID) getArguments().getSerializable(ARG_THEATER_ID);
        mTheater = TheaterList.get(getActivity()).getTheater(theaterId);
        setHasOptionsMenu(true);
    }

    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_theater, container, false);

        return v;
    }
}
