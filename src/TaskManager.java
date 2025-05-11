import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int idCounter = 0;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, SubTask> subTasks;
    HashMap<Integer, Task> tasks;

    public TaskManager() {
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        tasks = new HashMap<>();
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Task> getTasks() {
        if (this.tasks.isEmpty()) return null;
        return tasks;
    }

    public void removeTasks(TaskType type) {
        switch (type) {
            case EPIC:
                epics.clear();
                subTasks.clear();
                break;
            case SUBTASK:
                subTasks.clear();
                for (Epic epic : epics.values()) {
                    epic.clearSubtaskList();
                }
                break;
            case TASK:
                tasks.clear();
                break;
        }
        tasks.clear();
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
        if (epic != null) {
            epic.setId(idCounter);
            epics.put(idCounter, epic);
            idCounter++;
        }
    }

    public void createTask(SubTask subTask) {
        if (subTask != null) {
            Epic targetEpic = this.getEpic(subTask.getEpicId());
            if (targetEpic != null) {
                subTask.setId(idCounter);
                subTasks.put(idCounter, subTask);
                targetEpic.addSubTask(subTask);
                targetEpic.calculateStatus();
                idCounter++;
            }
        }
    }

    public void updateTask(Task task) {
        if (task == null) return;
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id).equals(task)) {
                tasks.put(id, task);
            }
        }
    }

    public void updateTask(Epic epic) {
        if (epic == null) return;

        for (Integer id : epics.keySet()) {
            if (epics.get(id).equals(epic)) {
                if (epics.get(id).getStatus() != epic.getStatus()) {
                    epic.setStatus(epics.get(id).getStatus());
                }
                epics.put(id, epic);
            }
        }
    }

    public void updateTask(SubTask subTask) {
        if (subTask == null) return;

        for (Integer id : subTasks.keySet()) {
            if (subTasks.get(id).equals(subTask)) {
                SubTask oldSubTask = subTasks.get(id);
                Epic targetEpic = this.getEpic(oldSubTask.epicId);
                if (targetEpic == null) return;
                subTasks.put(id, subTask);
                targetEpic.calculateStatus();
            }
        }
    }

    public void removeTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
            for (Integer subtaskId : subTasks.keySet()) {
                if (subTasks.get(subtaskId).getEpicId() == id) {
                    subTasks.remove(subtaskId);
                }
            }
        } else if (subTasks.containsKey(id)) {
            SubTask targetSubTask = subTasks.get(id);
            Epic targetEpic = this.getEpic(targetSubTask.getEpicId());
            if (targetEpic == null) {
                this.subTasks.remove(id);
                return;
            }
            this.subTasks.remove(id);
            targetEpic.removeSubTask(targetSubTask);
            targetEpic.calculateStatus();
        }
    }

    public ArrayList<SubTask> getEpicSubTasks(int epicId) {
        Epic targetEpic = this.getEpic(epicId);
        if (targetEpic == null) return null;
        return targetEpic.getSubtaskList();
    }
}
