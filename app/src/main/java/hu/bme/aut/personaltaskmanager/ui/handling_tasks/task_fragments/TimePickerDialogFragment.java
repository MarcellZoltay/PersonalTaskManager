package hu.bme.aut.personaltaskmanager.ui.handling_tasks.task_fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.TimePicker;

import java.util.Calendar;

import hu.bme.aut.personaltaskmanager.R;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private OnTimeSelectedListener onTimeSelectedListener;
    private int hour, minute;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment fragment = getTargetFragment();
        if (!(fragment instanceof OnTimeSelectedListener)) {
            throw new RuntimeException("The fragment does not implement the OnTimeSelectedListener interface");
        }

        onTimeSelectedListener = (OnTimeSelectedListener) fragment;

        Bundle b = getArguments();
        hour = b.getIntArray(getString(R.string.bundle_time_parts))[0];
        minute = b.getIntArray(getString(R.string.bundle_time_parts))[1];
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(hour == -1) {
            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        return new TimePickerDialog(getTargetFragment().getContext(), this, hour, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        onTimeSelectedListener.onTimeSelected(i, i1);
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int hour, int minute);
    }
}
