package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

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

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.Project;

public class NewProjectDialogFragment extends DialogFragment {

    public static final String TAG = "NewProjectDialogFragment";

    public interface INewProjectDialogListener {
        void onProjectCreated(Project newProject);
    }

    private INewProjectDialogListener listener;

    private EditText titleEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = getFragmentManager().getFragments().get(getFragmentManager().getFragments().size()-2);
        if (fragment instanceof INewProjectDialogListener) {
            listener = (INewProjectDialogListener) fragment;
        } else {
            throw new RuntimeException("Activity must implement the INewShoppingItemDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_project)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onProjectCreated(getProject());
                        }
                    }

                    private boolean isValid() {
                        return titleEditText.getText().length() > 0;
                    }

                    private Project getProject() {
                        Project p = new Project();
                        p.setTitle(titleEditText.getText().toString());
                        //p.save();
                        return p;
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_project, null);
        titleEditText = (EditText) contentView.findViewById(R.id.ProjectTitleEditText);
        return contentView;
    }

}
