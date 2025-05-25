package entities;

import java.util.ArrayList;
import utils.Status;

public class Epic extends Task {
    private final ArrayList<SubTask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<SubTask> getSubtaskList() {
        return new ArrayList<>(subtaskList);
    }

    public void clearSubtaskList() {
        subtaskList.clear();
        this.setStatus(Status.NEW);
    }

    public void addSubTask(SubTask subTask) {
        int subTaskIndex = subtaskList.indexOf(subTask);
        if (subTaskIndex >= 0) {
            subtaskList.add(subTaskIndex, subTask);
        } else {
            subtaskList.add(subTask);
        }
    }

    public void removeSubTask(SubTask subTask) {
        if (!subtaskList.contains(subTask)) return;
        subtaskList.remove(subTask);
    }

    public void calculateStatus() {
        if (subtaskList.isEmpty()) {
            this.setStatus(Status.NEW);
            return;
        }
        boolean isDone = true;
        boolean isNew = true;
        for (SubTask subTask : subtaskList) {
            if (subTask.getStatus() != Status.NEW) {
                isNew = false;
            }
            if (subTask.getStatus() != Status.DONE) {
                isDone = false;
            }
        }

        if (isNew) {
            this.setStatus(Status.NEW);
        } else if (isDone) {
            this.setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }
}
