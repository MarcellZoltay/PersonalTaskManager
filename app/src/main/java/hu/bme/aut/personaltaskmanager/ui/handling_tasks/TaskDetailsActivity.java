package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.DataManager;
import hu.bme.aut.personaltaskmanager.model.Project;
import hu.bme.aut.personaltaskmanager.model.Task;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.task_fragments.TaskDialogFragment;
import info.hoang8f.widget.FButton;

public class TaskDetailsActivity extends AppCompatActivity implements TaskDialogFragment.IEditTaskDialogListener{

    TextView tvTitle;
    TextView tvProject;
    TextView tvDate;
    TextView tvNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        final int projectPosition = getIntent().getIntExtra(getString(R.string.project_position), -1);
        final Project project = DataManager.getInstance().getProject(projectPosition);
        final int taskPosition = getIntent().getIntExtra(getString(R.string.task_position), -1);
        final Task t = project.getTasks().get(taskPosition);

        tvTitle = (TextView) findViewById(R.id.tvTitleOfTask);
        tvTitle.setText(t.getTitle());

        tvProject = (TextView) findViewById(R.id.tvProjectOfTask);
        tvProject.setText(t.getProject());

        tvDate = (TextView) findViewById(R.id.tvDateOfTask);
        tvDate.setText(DateFormatHelper.getFormattedDate(t.getDate(), "yyyy.MM.dd. HH:mm"));

        tvNote = (TextView) findViewById(R.id.tvNoteOfTask);
        tvNote.setText(t.getNote());

        FButton btnEdit = (FButton) findViewById(R.id.btnEditTask);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString(getString(R.string.project_name), project.getTitle());
                b.putInt(getString(R.string.project_position), projectPosition);
                b.putInt(getString(R.string.task_position), taskPosition);
                DialogFragment fragment = new TaskDialogFragment();
                fragment.setArguments(b);
                fragment.show(getSupportFragmentManager(), TaskDialogFragment.TAG);
            }
        });

        FButton btnDelete = (FButton) findViewById(R.id.btnDeleteTask);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
                adb.setMessage(R.string.delete_task_alert_msg);
                adb.setTitle("Delete " + t.getTitle() + " Task");
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataManager.getInstance().removeTask(projectPosition, t);
                        finish();
                    }
                });
                adb.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                adb.show();
            }
        });
    }

    @Override
    public void onTaskEdited(Task editedTask) {
        DataManager.getInstance().editTask(editedTask);

        tvTitle.setText(editedTask.getTitle());
        tvProject.setText(editedTask.getProject());
        tvDate.setText(DateFormatHelper.getFormattedDate(editedTask.getDate(), "yyyy.MM.dd. HH:mm"));
        tvNote.setText(editedTask.getNote());
    }
}

