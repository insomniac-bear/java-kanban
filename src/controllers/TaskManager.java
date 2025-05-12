package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import utils.TaskType;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    int idCounter = 0;

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void removeSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtaskList();
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public void createTask(Task task) {
        if (task != null) {
            task.setId(idCounter);
            tasks.put(idCounter, task);
            idCounter++;
        }
    }

    public void createTask(Epic epic) {
        if (epic == null) {
            return;
        }
        epic.setId(idCounter);
        epics.put(idCounter, epic);
        idCounter++;
    }

    public void createTask(SubTask subTask) {
        if (subTask == null) {
            return;
        }
        Epic targetEpic = this.getEpic(subTask.getEpicId());
        if (targetEpic == null) {
            return;
        }
        subTask.setId(idCounter);
        subTasks.put(idCounter, subTask);
        targetEpic.addSubTask(subTask);
        targetEpic.calculateStatus();
        idCounter++;
    }

    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void updateTask(Epic epic) {
        if (epic == null) {
            return;
        }
        Epic oldEpic = this.getEpic(epic.getId());
        if (oldEpic.getStatus() != epic.getStatus()) {
            epic.setStatus(oldEpic.getStatus());
        }
        epics.put(epic.getId(), epic);
    }

    public void updateTask(SubTask subTask) {
        if (subTask == null) {
            return;
        }

        SubTask oldSubTask = subTasks.get(subTask.getId());
        Epic targetEpic = this.getEpic(oldSubTask.getEpicId());
        if (targetEpic == null) {
            return;
        }
        subTasks.put(subTask.getId(), subTask);
        targetEpic.calculateStatus();
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        epics.remove(id);
        for (Integer subtaskId : subTasks.keySet()) {
            if (subTasks.get(subtaskId).getEpicId() == id) {
                subTasks.remove(subtaskId);
            }
        }
    }

    public void removeSubtask(int id) {
        SubTask targetSubTask = subTasks.get(id);
        if (targetSubTask == null) {
            return;
        }

        Epic targetEpic = this.getEpic(targetSubTask.getEpicId());
        if (targetEpic == null) {
            this.subTasks.remove(id);
            return;
        }
        this.subTasks.remove(id);
        targetEpic.removeSubTask(targetSubTask);
        targetEpic.calculateStatus();
    }

    public ArrayList<SubTask> getEpicSubTasks(int epicId) {
        Epic targetEpic = this.getEpic(epicId);
        if (targetEpic == null) return null;
        return targetEpic.getSubtaskList();
    }
}
