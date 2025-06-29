package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;
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
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void removeEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }
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
        if (task == null) {
            return;
        }

        if (task.getId() == -1) {
            task.setId(idCounter);
        } else {
            idCounter = task.getId();
        }
        tasks.put(idCounter, task);
        idCounter++;
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic == null) {
            return;
        }

        if (epic.getId() == -1) {
            epic.setId(idCounter);
        } else {
            idCounter = epic.getId();
        }
        epics.put(idCounter, epic);
        idCounter++;
    }

    @Override
    public void createSubtask(SubTask subTask) {
        if (subTask == null) {
            return;
        }
        int epicId = subTask.getEpicId();
        Epic targetEpic = this.epics.get(epicId);
        if (targetEpic == null) {
            return;
        }
        if (subTask.getId() == -1) {
            subTask.setId(idCounter);
        } else {
            idCounter = subTask.getId();
        }
        subTask.setEpicId(epicId);
        subTasks.put(idCounter, subTask);
        targetEpic.addSubTask(subTask);
        targetEpic.calculateStatus();
        idCounter++;
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Epic epic) {
        if (epic == null) {
            return;
        }
        int epicId = epic.getId();
        Epic oldEpic = this.epics.get(epicId);
        if (oldEpic.getStatus() != epic.getStatus()) {
            epic.setStatus(oldEpic.getStatus());
        }
        epics.put(epicId, epic);
    }

    @Override
    public void updateTask(SubTask subTask) {
        if (subTask == null) {
            return;
        }
        int subTaskId = subTask.getId();
        SubTask oldSubTask = subTasks.get(subTaskId);
        Epic targetEpic = this.epics.get(oldSubTask.getEpicId());
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
        historyManager.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        ArrayList<SubTask> deletingSubtasks = epics.get(id).getSubtaskList();
        for (SubTask subtask : deletingSubtasks) {
            subTasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        SubTask targetSubTask = subTasks.get(id);
        if (targetSubTask == null) {
            return;
        }

        historyManager.remove(targetSubTask.getId());
        Epic targetEpic = this.epics.get(targetSubTask.getEpicId());
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
