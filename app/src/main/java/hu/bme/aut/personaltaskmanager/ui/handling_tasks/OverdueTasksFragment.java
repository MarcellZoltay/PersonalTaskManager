package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.Task;
import hu.bme.aut.personaltaskmanager.model.TaskRecyclerAdapter;

public class OverdueTasksFragment extends Fragment {

    private RecyclerView recycleView;
    private TaskRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overdue_tasks, container, false);

        recycleView = (RecyclerView) rootView.findViewById(R.id.OverdueRecyclerView);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskRecyclerAdapter(new ITaskFilter() {
            @Override
            public boolean Filter(Task t) {
                if(t.isDone())
                    return false;

                if(t.isOverdue())
                    return true;

                Calendar c = Calendar.getInstance();
                long today = c.getTimeInMillis();

                Date d = new Date(t.getDate());
                c.setTime(d);
                long myDate = c.getTimeInMillis();

                if(myDate < today)
                    t.setOverdue(true);

                return t.isOverdue();
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
