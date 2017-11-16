package hu.bme.aut.personaltaskmanager.model;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager instance = new DataManager();

    private List<Project> projects;

    private DataManager() {
        projects = new ArrayList<>();
        projects.add(new Project("proba"));
        //loadItemsInBackground();
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
    public void addProject(Project p){ projects.add(p); }

    public List<Task> getTasks(int index) { return projects.get(index).getTasks(); }
    public void addTask(int index, Task t){ projects.get(index).addTask(t); }
}
