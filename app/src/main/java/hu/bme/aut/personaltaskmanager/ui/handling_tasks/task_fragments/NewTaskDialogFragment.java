package hu.bme.aut.personaltaskmanager.ui.handling_tasks.task_fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.Task;

public class NewTaskDialogFragment extends DialogFragment {

    public static final String TAG = "NewTaskDialogFragment";

    public interface INewTaskDialogListener {
        void onTaskCreated(Task newTask);
    }
    private INewTaskDialogListener listener;

    private EditText titleEditText;
    private EditText noteEditText;

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
                .setTitle(getString(R.string.new_task) + getArguments().getString(getString(R.string.project_name)))
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onTaskCreated(getTask());
                        }
                    }

                    private boolean isValid() { return titleEditText.getText().length() > 0; }

                    private Task getTask() {
                        Task t = new Task();
                        t.setTitle(titleEditText.getText().toString());
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
        noteEditText = (EditText) contentView.findViewById(R.id.TaskNoteEditText);
        return contentView;
    }
}
