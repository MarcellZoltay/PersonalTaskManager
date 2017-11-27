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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.DataManager;
import hu.bme.aut.personaltaskmanager.model.Task;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.DateFormatHelper;

public class TaskDialogFragment extends DialogFragment
        implements DatePickerDialogFragment.OnDateSelectedListener, TimePickerDialogFragment.OnTimeSelectedListener {

    public static final String TAG = "TaskDialogFragment";
    public static final int ADD_DATE_REQUEST_CODE = 120;
    public static final int ADD_TIME_REQUEST_CODE = 121;


    public interface INewTaskDialogListener {
        void onTaskCreated(Task newTask);
    }
    public interface IEditTaskDialogListener{
        void onTaskEdited(Task editedTask);
    }
    private INewTaskDialogListener newTaskListener;
    private IEditTaskDialogListener editTaskListener;

    private EditText titleEditText;
    private Button dateButton;
    private Button timeButton;
    private EditText noteEditText;
    private int year, month, day, hour = -1, minute;

    private final Fragment fragment = this;

    private boolean isEdit;
    private Task task;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof INewTaskDialogListener) {
            newTaskListener = (INewTaskDialogListener) activity;
        } else if (activity instanceof IEditTaskDialogListener) {
            editTaskListener = (IEditTaskDialogListener) activity;
        } else {
            throw new RuntimeException("Fragment/Activity must implement the INewProjectDialogListener interface!");
        }

        Bundle b = getArguments();
        int projectIndex = -1, taskIndex = -1;
        if(b != null){
            projectIndex = b.getInt(getString(R.string.project_position), -1);
            taskIndex = b.getInt(getString(R.string.task_position), -1);
        }
        if(taskIndex != -1 && projectIndex != -1){
            isEdit = true;
            task = DataManager.getInstance().getProject(projectIndex).getTasks().get(taskIndex);

            Date date = new Date(task.getDate());
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
        else
            task = new Task();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        if(!isEdit)
            dialog.setTitle(getString(R.string.new_task) + " " + getArguments().getString(getString(R.string.project_name)));
        else
            dialog.setTitle(getString(R.string.edit_task_in) + " " + getArguments().getString(getString(R.string.project_name)));

        dialog.setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            setTask();
                            if(!isEdit)
                                newTaskListener.onTaskCreated(task);
                            else
                                editTaskListener.onTaskEdited(task);
                        }
                    }

                    private boolean isValid() {
                        boolean res = true;
                        if(!(titleEditText.getText().length() > 0 && year != 0 && hour != -1)){
                            res = false;
                            Toast.makeText(fragment.getContext(), R.string.new_task_error_toast_msg, Toast.LENGTH_SHORT).show();
                        }

                        return res;
                    }
                    private void setTask() {
                        task.setTitle(titleEditText.getText().toString().trim());
                        task.setProject(getArguments().getString(getString(R.string.project_name)));
                        task.setDate(new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis());
                        task.setOverdue(false);
                        task.setNote(noteEditText.getText().toString().trim());
                    }
                })
                .setNegativeButton(R.string.cancel, null);
         return dialog.create();
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
        if(isEdit){
            titleEditText.setText(task.getTitle());
            titleEditText.setSelection(titleEditText.getText().length());
            dateButton.setText(DateFormatHelper.getFormattedDate(task.getDate(), "yyyy.MM.dd."));
            timeButton.setText(DateFormatHelper.getFormattedDate(task.getDate(), "HH:mm"));
            noteEditText.setText(task.getNote());
        }
        return contentView;
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        this.year = year; this.month = month; this.day = day;

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        long date = c.getTimeInMillis();
        dateButton.setText(DateFormatHelper.getFormattedDate(date, "yyyy.MM.dd."));
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        this.hour = hour; this.minute = minute;

        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);
        long date = c.getTimeInMillis();
        timeButton.setText(DateFormatHelper.getFormattedDate(date, "HH:mm"));
    }
}
