package hu.bme.aut.personaltaskmanager.model;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager instance = new DataManager();

    private List<Project> projects;

    private DataManager() {
        projects = new ArrayList<>();
        //projects.add(new Project("Shopping"));
        //projects.add(new Project("Paying bills"));
        //projects.add(new Project("Work"));

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

    public void addProject(Project p){
        projects.add(p);
    }
}
