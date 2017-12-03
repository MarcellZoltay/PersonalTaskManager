package hu.bme.aut.personaltaskmanager.model;

import com.orm.SugarRecord;

public class Task extends SugarRecord {

    private String title;
    private String project;
    private long date;
    private boolean done;
    private boolean overdue;
    private String note;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getProject() { return project; }
    public void setProject(String project) { this.project = project; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    public boolean isOverdue() { return overdue; }
    public void setOverdue(boolean overdue) { this.overdue = overdue; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
