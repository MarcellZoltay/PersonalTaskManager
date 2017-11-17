package hu.bme.aut.personaltaskmanager.ui.handling_tasks.task_fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.Task;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.DatePickerDialogFragment;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.TimePickerDialogFragment;

public class NewTaskDialogFragment extends DialogFragment
        implements DatePickerDialogFragment.OnDateSelectedListener, TimePickerDialogFragment.OnTimeSelectedListener {

    public static final String TAG = "NewTaskDialogFragment";
    public static final int ADD_DATE_REQUEST_CODE = 120;
    public static final int ADD_TIME_REQUEST_CODE = 121;


    public interface INewTaskDialogListener {
        void onTaskCreated(Task newTask);
    }
    private INewTaskDialogListener listener;

    private EditText titleEditText;
    private Button dateButton;
    private Button timeButton;
    private EditText noteEditText;
    private int year, month, day, hour, minute;

    private final Fragment fragment = this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof INewTaskDialogListener) {
            listener = (INewTaskDialogListener) activity;
        } else {
            throw new RuntimeException("Fragment/Activity must implement the INewProjectDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.new_task) + " " + getArguments().getString(getString(R.string.project_name)))
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onTaskCreated(getTask());
                        }
                    }

                    private boolean isValid() {
                        boolean res = true;
                        if(!(titleEditText.getText().length() > 0 && year != 0 && hour != 0)){
                            res = false;
                            Toast.makeText(fragment.getContext(), R.string.new_task_error_toast_msg, Toast.LENGTH_SHORT).show();
                        }

                        return res;
                    }

                    private Task getTask() {
                        Task t = new Task();
                        t.setTitle(titleEditText.getText().toString());
                        t.setProject(getArguments().getString(getString(R.string.project_name)));
                        t.setDate(new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis());
                        t.setNote(noteEditText.getText().toString());
                        return t;
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_task, null);
        titleEditText = (EditText) contentView.findViewById(R.id.TaskTitleEditText);
        dateButton = (Button) contentView.findViewById(R.id.TaskDateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment dialog = new DatePickerDialogFragment();
                dialog.setTargetFragment(fragment, ADD_DATE_REQUEST_CODE);

                Bundle b = new Bundle();
                int dateParts[] = {year, month, day};
                b.putIntArray(getString(R.string.bundle_date_parts), dateParts);
                dialog.setArguments(b);

                dialog.show(getFragmentManager(), TAG);
            }
        });
        timeButton = (Button) contentView.findViewById(R.id.TaskTimeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogFragment dialog = new TimePickerDialogFragment();
                dialog.setTargetFragment(fragment, ADD_TIME_REQUEST_CODE);

                Bundle b = new Bundle();
                int timeParts[] = {hour, minute};
                b.putIntArray(getString(R.string.bundle_time_parts), timeParts);
                dialog.setArguments(b);

                dialog.show(getFragmentManager(), TAG);
            }
        });
        noteEditText = (EditText) contentView.findViewById(R.id.TaskNoteEditText);
        return contentView;
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        this.year = year; this.month = month; this.day = day;

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        long date = c.getTimeInMillis();
        dateButton.setText(getFormattedDate(date, "yyyy.MM.dd"));
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        this.hour = hour; this.minute = minute;

        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);
        long date = c.getTimeInMillis();
        timeButton.setText(getFormattedDate(date, "HH:mm"));
    }

    private String getFormattedDate(long date, String dateFormat)
    {
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return formatter.format(c.getTime());
    }
}
