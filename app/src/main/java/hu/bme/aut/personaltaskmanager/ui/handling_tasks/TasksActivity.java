package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.pages_of_viewpager.CompletedTasksFragment;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.pages_of_viewpager.Next7DaysFragment;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.pages_of_viewpager.OverdueTasksFragment;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.pages_of_viewpager.ProjectsFragment;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.pages_of_viewpager.TodayFragment;

public class TasksActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        final TasksPagerAdapter adapter = new TasksPagerAdapter(getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.projects, ProjectsFragment.class)
                .add(R.string.today, TodayFragment.class)
                .add(R.string.next7days, Next7DaysFragment.class)
                .add(R.string.completed_tasks, CompletedTasksFragment.class)
                .add(R.string.overdue_tasks, OverdueTasksFragment.class)
                .create());

        ViewPager vpTasks = (ViewPager) findViewById(R.id.viewpager);
        vpTasks.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(vpTasks);
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getPage(position);
                if(!(fragment instanceof IUpdatablePageFragment))
                    throw new RuntimeException("Fragment does not implement IUpdatableFragment interface");

                ((IUpdatablePageFragment) fragment).updatePage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
