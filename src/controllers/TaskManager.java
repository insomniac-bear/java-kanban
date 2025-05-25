package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;

import java.util.ArrayList;

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

    void createTask(Epic epic);

    void createTask(SubTask subTask, int epicId);

    void updateTask(Task task, Integer taskId);

    void updateTask(Epic epic, Integer epicId);

    void updateTask(SubTask subTask, Integer subTaskId);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    ArrayList<SubTask> getEpicSubTasks(int epicId);

    ArrayList<Task> getHistory();
}
