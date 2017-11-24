package hu.bme.aut.personaltaskmanager.ui.handling_tasks.project_fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.DataManager;
import hu.bme.aut.personaltaskmanager.model.Project;

public class NewProjectDialogFragment extends DialogFragment {

    public static final String TAG = "NewProjectDialogFragment";

    public interface INewProjectDialogListener {
        void onProjectCreated(Project newProject);
        void onProjectEdited(Project editedProject);
    }
    private INewProjectDialogListener listener;

    private EditText titleEditText;

    private boolean isEdit;
    private Project project;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = getTargetFragment();
        if (fragment instanceof INewProjectDialogListener) {
            listener = (INewProjectDialogListener) fragment;
        } else {
            throw new RuntimeException("Fragment/Activity must implement the INewProjectDialogListener interface!");
        }

        Bundle b = getArguments();
        int projectIndex = -1;
        if(b != null)
            projectIndex = b.getInt(getString(R.string.project_position));
        if(projectIndex != -1) {
            isEdit = true;
            project = DataManager.getInstance().getProject(projectIndex);
        }
        else
            project = new Project();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        if(!isEdit)
            dialog.setTitle(R.string.new_project);
        else {
            dialog.setTitle(R.string.edit_project);
        }

        dialog.setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            setProject();
                            if(!isEdit)
                                listener.onProjectCreated(project);
                            else {
                                listener.onProjectEdited(project);
                            }
                        }
                    }

                    private boolean isValid() {
                        boolean res = true;
                        if(!(titleEditText.getText().length() > 0)){
                            res = false;
                            Toast.makeText(getContext(), R.string.new_project_error_toast_msg, Toast.LENGTH_SHORT).show();
                        }

                        return res;
                    }

                    private void setProject() {
                        project.setTitle(titleEditText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return dialog.create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_project, null);
        titleEditText = (EditText) contentView.findViewById(R.id.ProjectTitleEditText);
        if(isEdit) {
            titleEditText.setText(project.getTitle());
            titleEditText.setSelection(titleEditText.getText().length());
            titleEditText.setSelectAllOnFocus(true);
        }
        return contentView;
    }

}
