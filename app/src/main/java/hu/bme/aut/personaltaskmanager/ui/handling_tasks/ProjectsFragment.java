package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

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

public class ProjectsFragment extends Fragment implements NewProjectDialogFragment.INewProjectDialogListener {

    public static final int REQUEST_CODE = 100;

    private RecyclerView recyclerView;
    private ProjectRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_projects, container, false);

        final Fragment frag = this;
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabProject);
        fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               NewProjectDialogFragment dialog = new NewProjectDialogFragment();
               dialog.setTargetFragment(frag, REQUEST_CODE);
               dialog.show(getFragmentManager(), NewProjectDialogFragment.TAG);
            }
       });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.ProjectRecyclerView);
        adapter = new ProjectRecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onProjectCreated(Project newProject) {
        //adapter.addProject(newProject);
        adapter.projectAdded();
        DataManager.getInstance().addProject(newProject);
        //newProject.save();
    }
}
