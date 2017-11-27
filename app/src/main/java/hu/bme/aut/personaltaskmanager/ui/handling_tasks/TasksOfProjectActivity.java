package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.DataManager;
import hu.bme.aut.personaltaskmanager.model.Task;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.task_fragments.TaskDialogFragment;

public class TasksOfProjectActivity extends AppCompatActivity implements TaskDialogFragment.INewTaskDialogListener {

    private RecyclerView recyclerView;
    private TaskRecyclerAdapter adapter;
    private int projectPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_of_project);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString(getString(R.string.project_name), getIntent().getStringExtra(getString(R.string.project_name)));
                DialogFragment fragment = new TaskDialogFragment();
                fragment.setArguments(b);
                fragment.show(getSupportFragmentManager(), TaskDialogFragment.TAG);
            }
        });

        projectPosition = getIntent().getIntExtra(getString(R.string.project_position), -1);

        recyclerView = (RecyclerView) findViewById(R.id.TasksOfProjectRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskRecyclerAdapter(new ITaskFilter() {
            @Override
            public boolean Filter(Task t) {
                return t.getProject().equals(DataManager.getInstance().getProject(projectPosition).getTitle());
            }
        }, new OnTaskSelectedListener() {
            @Override
            public void onTaskSelected(int taskPosition) {
                Intent showDetails = new Intent();
                showDetails.setClass(TasksOfProjectActivity.this, TaskDetailsActivity.class)
                        .putExtra(getString(R.string.project_position), projectPosition)
                        .putExtra(getString(R.string.task_position), taskPosition);
                startActivity(showDetails);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.update();
    }

    @Override
    public void onTaskCreated(Task newTask) {
        DataManager.getInstance().addTask(projectPosition, newTask);
        adapter.update();
    }
}
