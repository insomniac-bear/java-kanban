package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getSubTasks();

    ArrayList<Task> getTasks();

    void removeTasks();

    void removeEpics();

    void removeSubTasks();

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(SubTask subTask);

    void updateTask(Task task);

    void updateTask(Epic epic);

    void updateTask(SubTask subTask);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    ArrayList<SubTask> getEpicSubTasks(int epicId);

    List<Task> getHistory();
}
