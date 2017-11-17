package hu.bme.aut.personaltaskmanager.model;

import android.os.AsyncTask;

import java.util.ArrayList;
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

        p.getTasks().clear();

        p.delete();
        //projects.remove(p);
    }


    //public List<Task> getTasks(int index) { return projects.get(index).getTasks(); }
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
    public void removeTask(Task task){
        for (Project p: projects) {
            for (Task t: p.getTasks()) {
                if (t == task){
                    p.removeTask(task);
                    task.delete();
                    return;
                }
            }
        }
    }
}
