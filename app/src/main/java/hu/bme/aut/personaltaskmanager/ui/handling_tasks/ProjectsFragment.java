package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.DataManager;
import hu.bme.aut.personaltaskmanager.model.Project;
import hu.bme.aut.personaltaskmanager.model.ProjectRecyclerAdapter;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.project_fragments.NewProjectDialogFragment;

public class ProjectsFragment extends Fragment implements NewProjectDialogFragment.INewProjectDialogListener {

    public static final int ADD_PROJECT_REQUEST_CODE = 100;

    private RecyclerView recyclerView;
    private ProjectRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_projects, container, false);

        final Fragment frag = this;
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabProject);
        fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               NewProjectDialogFragment dialog = new NewProjectDialogFragment();
               dialog.setTargetFragment(frag, ADD_PROJECT_REQUEST_CODE);
               dialog.show(frag.getFragmentManager(), NewProjectDialogFragment.TAG);
            }
       });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.ProjectRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProjectRecyclerAdapter(frag, new OnProjectSelectedListener() {
            @Override
            public void onProjectSelected(int projectPosition) {
                Intent showTasksIntent = new Intent();
                showTasksIntent.setClass(getContext(), TasksOfProjectActivity.class)
                        .putExtra(getString(R.string.project_name), DataManager.getInstance().getProject(projectPosition).getTitle())
                        .putExtra(getString(R.string.project_position), projectPosition);
                startActivity(showTasksIntent);
            }
        });
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onProjectCreated(Project newProject) {
        DataManager.getInstance().addProject(newProject);
        adapter.update();
    }

    @Override
    public void onProjectEdited(Project editedProject) {
        DataManager.getInstance().editProject(editedProject);
        adapter.update();
    }
}
