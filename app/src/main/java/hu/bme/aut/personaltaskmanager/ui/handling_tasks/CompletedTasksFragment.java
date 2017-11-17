package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.Task;
import hu.bme.aut.personaltaskmanager.model.TaskRecyclerAdapter;

public class CompletedTasksFragment extends Fragment {

    private RecyclerView recycleView;
    private TaskRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completed_tasks, container, false);

        recycleView = (RecyclerView) rootView.findViewById(R.id.CompletedTasksRecyclerView);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskRecyclerAdapter(new ITaskFilter() {
            @Override
            public boolean Filter(Task t) {
                return t.isDone();
            }
        });
        recycleView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.update();
    }
}
