package hu.bme.aut.personaltaskmanager.ui.handling_tasks.pages_of_viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.GregorianCalendar;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.Task;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.ITaskFilter;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.IUpdatablePageFragment;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.TaskRecyclerAdapter;

public class Next7DaysFragment extends Fragment implements IUpdatablePageFragment {

    private RecyclerView recycleView;
    private TaskRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_next7_days, container, false);

        recycleView = (RecyclerView) rootView.findViewById(R.id.Next7RecyclerView);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskRecyclerAdapter(new ITaskFilter() {
            @Override
            public boolean Filter(Task t) {
                Calendar c = Calendar.getInstance();

                Calendar today = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

                c.add(Calendar.DAY_OF_WEEK, 7);
                Calendar next7 = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);

                return today.getTimeInMillis() <= t.getDate() && t.getDate() <= next7.getTimeInMillis();
            }
        });
        recycleView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void updatePage() {
        adapter.update();
    }
}
