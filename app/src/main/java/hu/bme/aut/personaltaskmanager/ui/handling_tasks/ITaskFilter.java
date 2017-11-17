package hu.bme.aut.personaltaskmanager.ui.handling_tasks;

import hu.bme.aut.personaltaskmanager.model.Task;

public interface ITaskFilter {
    boolean Filter(Task t);
}
