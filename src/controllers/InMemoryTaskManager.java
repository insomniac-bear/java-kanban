package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    int idCounter = 0;
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    public void removeEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtaskList();
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
            task.setId(idCounter);
            tasks.put(idCounter, task);
            idCounter++;
        }
    }

    @Override
    public void createTask(Epic epic) {
        if (epic == null) {
            return;
        }
        epic.setId(idCounter);
        epics.put(idCounter, epic);
        idCounter++;
    }

    @Override
    public void createTask(SubTask subTask, int epicId) {
        if (subTask == null) {
            return;
        }
        Epic targetEpic = this.epics.get(epicId);
        if (targetEpic == null) {
            return;
        }
        subTask.setId(idCounter);
        subTask.setEpicId(epicId);
        subTasks.put(idCounter, subTask);
        targetEpic.addSubTask(subTask);
        targetEpic.calculateStatus();
        idCounter++;
    }

    @Override
    public void updateTask(Task task, Integer taskId) {
        if (task == null) {
            return;
        }
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    @Override
    public void updateTask(Epic epic, Integer epicId) {
        if (epic == null) {
            return;
        }
        Epic oldEpic = this.getEpic(epicId);
        if (oldEpic.getStatus() != epic.getStatus()) {
            epic.setStatus(oldEpic.getStatus());
        }
        epics.put(epicId, epic);
    }

    @Override
    public void updateTask(SubTask subTask, Integer subTaskId) {
        if (subTask == null) {
            return;
        }

        SubTask oldSubTask = subTasks.get(subTaskId);
        Epic targetEpic = this.getEpic(oldSubTask.getEpicId());
        if (targetEpic == null) {
            return;
        }
        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        targetEpic.addSubTask(subTask);
        targetEpic.calculateStatus();
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        epics.remove(id);
        for (Integer subtaskId : subTasks.keySet()) {
            if (subTasks.get(subtaskId).getEpicId() == id) {
                subTasks.remove(subtaskId);
            }
        }
    }

    @Override
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

    @Override
    public ArrayList<SubTask> getEpicSubTasks(int epicId) {
        Epic targetEpic = this.epics.get(epicId);
        if (targetEpic == null) return null;
        return targetEpic.getSubtaskList();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}
