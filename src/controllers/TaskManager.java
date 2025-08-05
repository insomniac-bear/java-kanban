package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import exceptions.ManagerGetException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getSubTasks();

    ArrayList<Task> getTasks();

    void removeTasks();

    void removeEpics();

    void removeSubTasks();

    Task getTask(int id) throws ManagerGetException;

    Epic getEpic(int id) throws ManagerGetException;

    SubTask getSubTask(int id) throws ManagerGetException;

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(SubTask subTask);

    void updateTask(Task task) throws ManagerGetException;

    void updateTask(Epic epic) throws ManagerGetException;

    void updateTask(SubTask subTask) throws ManagerGetException;

    void removeTask(int id) throws ManagerGetException;

    void removeEpic(int id) throws ManagerGetException;

    void removeSubtask(int id) throws ManagerGetException;

    ArrayList<SubTask> getEpicSubTasks(int epicId) throws ManagerGetException;

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}
