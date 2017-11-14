package hu.bme.aut.personaltaskmanager.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class Project extends SugarRecord {

    private String title;
    private List<Task> tasks;

    public Project(){ tasks = new ArrayList<>(); }
    public Project(String title){
        tasks = new ArrayList<>();
        this.title = title;
    }

    public void addTask(Task t){
        tasks.add(t);
    }

    //TODO: modify task

    public Task removeTask(int i){
        return tasks.remove(i);
    }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() { return tasks; }
    public void clearTasks(){ tasks.clear(); }
}
