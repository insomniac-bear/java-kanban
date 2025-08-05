package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import exceptions.ManagerGetException;
import exceptions.ManagerSaveException;
import utils.Status;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(
                    Task::getStartTime,
                    Comparator.naturalOrder()
            )
    );

    private boolean hasTimeCrossing(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null) {
            return false;
        }

        return getPrioritizedTasks().stream()
                .filter(task -> !task.equals(newTask))
                .anyMatch(task -> isTaskCrossing(newTask, task));
    }

    private static boolean isDateBefore(LocalDateTime date1, LocalDateTime date2) {
        return date1 != null && date2 != null && date1.isBefore(date2);
    }

    private static boolean isTaskCrossing(Task task1, Task task2) {
        return isDateBefore(task2.getStartTime(), task1.getEndTime())
                && isDateBefore(task1.getStartTime(), task2.getEndTime());
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return this.prioritizedTasks;
    }

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
        tasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void removeEpics() {
        epics.values().forEach(epic -> {
            historyManager.remove(epic.getId());
            prioritizedTasks.remove(epic);
        });
        subTasks.values().forEach(subTask -> {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        });
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeSubTasks() {
        subTasks.values().forEach(subTask -> {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        });
        subTasks.clear();
        epics.values().forEach(Epic::clearSubtaskList);
    }

    @Override
    public Task getTask(int id) throws ManagerGetException {
        Task task = tasks.get(id);

        if (task == null) {
            throw new ManagerGetException("Задача с id " + id + " не найдена");
        }

        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) throws ManagerGetException {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new ManagerGetException("Эпик с id " + id + " не найден");
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) throws ManagerGetException {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            throw new ManagerGetException("Эпик с id " + id + " не найден");
        }
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public void createTask(Task task) {
        if (task == null) {
            return;
        }

        if (hasTimeCrossing(task)) {
            throw new ManagerSaveException("Задача имеет пересечение по времени");
        }

        if (task.getId() == null) {
            task.setId(idCounter);
        } else {
            idCounter = task.getId();
        }

        if (task.getStatus() == null) {
            task.setStatus(Status.NEW);
        }
        tasks.put(idCounter, task);
        idCounter++;

        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic == null) {
            return;
        }

        if (epic.getId() == null) {
            epic.setId(idCounter);
        } else {
            idCounter = epic.getId();
        }

        if (epic.getStatus() == null) {
            epic.setStatus(Status.NEW);
        }

        epics.put(idCounter, epic);
        idCounter++;
    }

    @Override
    public void createSubtask(SubTask subTask) {
        if (subTask == null) {
            return;
        }
        if (hasTimeCrossing(subTask)) {
            throw new ManagerSaveException("Подзадача имеет пересечение по времени");
        }

        int epicId = subTask.getEpicId();
        Epic targetEpic = this.epics.get(epicId);

        if (targetEpic == null) {
            throw new ManagerSaveException("Эпик с id " + epicId + " не найден");
        }

        if (subTask.getId() == null) {
            subTask.setId(idCounter);
        } else {
            idCounter = subTask.getId();
        }

        if (subTask.getStatus() == null) {
            subTask.setStatus(Status.NEW);
        }

        subTask.setEpicId(epicId);
        subTasks.put(idCounter, subTask);
        targetEpic.addSubTask(subTask);
        prioritizedTasks.add(subTask);
        targetEpic.calculateStatus();
        idCounter++;
    }

    @Override
    public void updateTask(Task task) throws ManagerGetException {
        if (task == null) {
            return;
        }
        if (hasTimeCrossing(task)) {
            throw new ManagerSaveException("Задача имеет пересечение по времени");
        }

        Task oldTask = tasks.get(task.getId());
        if (oldTask == null) {
            throw  new ManagerGetException("Задача с id " + task.getId() + " не найдена");
        }
        prioritizedTasks.remove(oldTask);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateTask(Epic epic)  throws ManagerGetException {
        if (epic == null) {
            return;
        }
        int epicId = epic.getId();
        Epic oldEpic = this.epics.get(epicId);
        if (oldEpic == null) {
            throw new ManagerGetException("Эпик с id " + epicId + " не найден");
        }
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
        if (hasTimeCrossing(subTask)) {
            throw new ManagerSaveException("Подзадача имеет пересечение по времени");
        }
        int subTaskId = subTask.getId();
        SubTask oldSubTask = subTasks.get(subTaskId);
        int epicId = oldSubTask.getEpicId();
        Epic targetEpic = this.epics.get(epicId);
        if (targetEpic == null) {
            throw new ManagerSaveException("Подзадача с id " + epicId + " не найдена");
        }
        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        prioritizedTasks.remove(subTask);
        targetEpic.removeSubTask(subTask);
        targetEpic.addSubTask(subTask);
        targetEpic.calculateStatus();
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        }
    }

    @Override
    public void removeTask(int id) throws ManagerGetException {
        Task removedTask = tasks.get(id);
        if (removedTask == null) {
            throw new ManagerGetException("Задача с id " + id + " не найдена");
        }
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpic(int id) throws ManagerGetException {
        Epic removedEpic = epics.get(id);
        if (removedEpic == null) {
            throw new ManagerGetException("Эпик с id " + id + " не найден");
        }
        ArrayList<SubTask> deletingSubtasks = epics.get(id).getSubtaskList();
        deletingSubtasks.forEach(subTask -> {
            subTasks.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
            historyManager.remove(subTask.getId());

        });
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtask(int id) throws ManagerGetException {
        SubTask targetSubTask = subTasks.get(id);
        if (targetSubTask == null) {
            throw new ManagerGetException("Подзадача с id " + id + " не найдена");
        }

        historyManager.remove(targetSubTask.getId());
        Epic targetEpic = this.epics.get(targetSubTask.getEpicId());
        if (targetEpic == null) {
            this.subTasks.remove(id);
            return;
        }
        prioritizedTasks.remove(targetSubTask);
        this.subTasks.remove(id);
        targetEpic.removeSubTask(targetSubTask);
        targetEpic.calculateStatus();
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(int epicId) throws ManagerGetException {
        Epic targetEpic = this.epics.get(epicId);
        if (targetEpic == null) {
            throw new ManagerGetException("Эпик с id " + epicId + " не найден");
        }
        return targetEpic.getSubtaskList();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
