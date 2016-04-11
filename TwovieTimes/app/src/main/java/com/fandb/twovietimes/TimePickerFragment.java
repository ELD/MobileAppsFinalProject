package com.fandb.twovietimes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

/**
 * Created by eric on 4/10/16.
 */
public class TimePickerFragment extends DialogFragment {
    private static final String ARG = "date";

    private DatePicker mDatePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.date_picker, null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
