package hu.bme.aut.personaltaskmanager.model;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import hu.bme.aut.personaltaskmanager.ui.handling_tasks.ITaskFilter;

public class DataManager {

    private static DataManager instance = new DataManager();

    private List<Project> projects;

    private DataManager() {
        projects = new ArrayList<>();

        //loadItemsInBackground();
        projects = Project.listAll(Project.class);
        List<Task> tasks = Task.listAll(Task.class);
        for(Task t: tasks) {
            for (Project p : projects) {
                if(t.getProject().equals(p.getTitle()))
                    p.addTask(t);
            }
        }
    }

    public static DataManager getInstance() {
        return instance;
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Project>>() {

            @Override
            protected List<Project> doInBackground(Void... voids) {
                return Project.listAll(Project.class);
            }

            @Override
            protected void onPostExecute(List<Project> projectItems) {
                super.onPostExecute(projectItems);
                projects.addAll(projectItems);
            }
        }.execute();
    }


    // PROJECT
    public List<Project> getProjects(){
        return projects;
    }
    public Project getProject(int position) { return projects.get(position); }
    public void addProject(Project p){
        projects.add(p);
        p.save();
    }
    public void removeProject(Project p){
        for(Task t: p.getTasks())
            t.delete();

        p.clearTasks();
        p.delete();
    }
    public void editProject(Project p){
        p.save();
    }


    // TASK
    public List<Task> getTasks(ITaskFilter filter) {
        List<Task> tasks = new ArrayList<>();
        for (Project p: projects) {
            for (Task t: p.getTasks()) {
                if(filter.Filter(t))
                    tasks.add(t);
            }
        }
        return tasks;
    }
    public void addTask(int index, Task t){
        projects.get(index).addTask(t);
        t.save();
    }
    public void removeTask(int projectIndex, Task task){
        projects.get(projectIndex).removeTask(task);
        task.delete();
    }
    public void editTask(Task t){
        t.save();
    }


    // STATISTIC
    public List<StatisticItem> getStatistic(long firstDay, long lastDay){
        List<StatisticItem> items = new ArrayList<>();
        for(int i=0; i<7;i++)
            items.add(new StatisticItem());

        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.setTimeInMillis(firstDay);
        Calendar first = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        c.setTimeInMillis(lastDay);
        Calendar last = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        for(Project p: projects)
            for(Task t: p.getTasks()){
                if(first.getTimeInMillis() <= t.getDate() && t.getDate() <= last.getTimeInMillis()){
                    c.setTimeInMillis(t.getDate());
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                    if(dayOfWeek == 1) dayOfWeek = 8;
                    StatisticItem s = items.get(dayOfWeek-2);
                    s.incrementAll();

                    if(t.isDone()) s.incrementDone();
                    else if(t.isOverdue()) s.incrementOverdue();
                    else if(t.getDate() < now){
                        t.setOverdue(true);
                        s.incrementOverdue();
                    }
                }
            }

        return items;
    }
}
