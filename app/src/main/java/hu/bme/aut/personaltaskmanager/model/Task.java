package hu.bme.aut.personaltaskmanager.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task extends SugarRecord {
    private String title;
    private String project;
    private Date date;
    private int durationTime;
    private boolean done;
    private boolean overdue;
    private String note;
    private List<Date> reminders;

    public Task(){ reminders = new ArrayList<>(); }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getProject() { return project; }
    public void setProject(String project) { this.project = project; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getDurationTime() { return durationTime; }
    public void setDurationTime(int durationTime) { this.durationTime = durationTime; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    public boolean isOverdue() { return overdue; }
    public void setOverdue(boolean overdue) { this.overdue = overdue; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
