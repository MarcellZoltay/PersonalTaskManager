package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import hu.bme.aut.personaltaskmanager.R;

public class TasksActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        TasksPagerAdapter adapter = new TasksPagerAdapter(getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.today, TodayFragment.class)
                .add(R.string.next7days, Next7DaysFragment.class)
                .add(R.string.completed_tasks, CompletedTasksFragment.class)
                .add(R.string.overdue, OverdueTasksFragment.class)
                .add(R.string.projects, ProjectsFragment.class)
                .create());

        ViewPager vpTasks = (ViewPager) findViewById(R.id.viewpager);
        vpTasks.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(vpTasks);
    }
}
