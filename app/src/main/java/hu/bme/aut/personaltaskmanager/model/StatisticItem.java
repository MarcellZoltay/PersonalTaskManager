package hu.bme.aut.personaltaskmanager.model;

public class StatisticItem {
    private int counterAllTasks;
    private int counterDoneTasks;
    private int counterOverdueTasks;

    public StatisticItem(){
        counterAllTasks = 0;
        counterDoneTasks = 0;
        counterOverdueTasks = 0;
    }

    public int getAll() { return counterAllTasks; }
    public void incrementAll() { counterAllTasks++; }

    public int getDone() { return counterDoneTasks; }
    public void incrementDone() { counterDoneTasks++; }

    public int getOverdue() { return counterOverdueTasks; }
    public void incrementOverdue() { counterOverdueTasks++; }
}
