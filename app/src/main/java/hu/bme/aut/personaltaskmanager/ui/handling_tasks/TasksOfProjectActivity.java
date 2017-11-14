package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

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
import hu.bme.aut.personaltaskmanager.model.TaskRecyclerAdapter;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.task_fragments.NewTaskDialogFragment;

public class TasksOfProjectActivity extends AppCompatActivity implements NewTaskDialogFragment.INewTaskDialogListener {

    public static final int ADD_PROJECT_REQUEST_CODE = 101;

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
                DialogFragment fragment = new NewTaskDialogFragment();
                fragment.setArguments(b);
                fragment.show(getSupportFragmentManager(), NewTaskDialogFragment.TAG);
            }
        });

        projectPosition = getIntent().getIntExtra(getString(R.string.project_position), -1);

        recyclerView = (RecyclerView) findViewById(R.id.TasksOfProjectRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskRecyclerAdapter(projectPosition);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTaskCreated(Task newTask) {
        DataManager.getInstance().addTask(projectPosition, newTask);
        adapter.addNewTask(newTask);
    }
}
