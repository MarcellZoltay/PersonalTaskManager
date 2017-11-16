package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;

import hu.bme.aut.personaltaskmanager.R;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDateSelectedListener onDateSelectedListener;
    private int year, month, day;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment fragment = getTargetFragment();
        if (!(fragment instanceof OnDateSelectedListener)) {
            throw new RuntimeException("The fragment does not implement the OnDateSelectedListener interface");
        }

        onDateSelectedListener = (OnDateSelectedListener) fragment;

        Bundle b = getArguments();
        year = b.getIntArray(getString(R.string.bundle_date_parts))[0];
        month = b.getIntArray(getString(R.string.bundle_date_parts))[1];
        day = b.getIntArray(getString(R.string.bundle_date_parts))[2];
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        return new DatePickerDialog(getTargetFragment().getContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        onDateSelectedListener.onDateSelected(year, month, day);
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int day);
    }
}