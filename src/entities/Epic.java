package entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import utils.Status;
import utils.TaskType;

public class Epic extends Task {
    private final ArrayList<SubTask> subtaskList = new ArrayList<>();
    private LocalDateTime endTime;
    private LocalDateTime startTime;
    private Duration duration;

    public Epic(String name, String description) {
        super(name, description, null, null);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status, null, null);
    }

    private LocalDateTime getSubtaskMinStartTime() {
        return Collections.min(subtaskList, Comparator.comparing(Task::getStartTime)).getStartTime();
    }

    private LocalDateTime getSubtaskMaxEndTime() {
        return Collections.max(subtaskList, Comparator.comparing(Task::getEndTime)).getEndTime();
    }

    public ArrayList<SubTask> getSubtaskList() {
        return new ArrayList<>(subtaskList);
    }

    public void clearSubtaskList() {
        subtaskList.clear();
        this.setStatus(Status.NEW);
        this.startTime = null;
        this.duration = null;
        this.endTime = null;
    }

    public void addSubTask(SubTask subTask) {
        if (subTask.getDuration() == null || subTask.getStartTime() == null) {
            return;
        }

        if (subtaskList.isEmpty()) {
            subtaskList.add(subTask);

            this.duration = subTask.getDuration();
            this.startTime = subTask.getStartTime();
            this.endTime = this.startTime.plus(subTask.getDuration());
            return;
        }

        int subTaskIndex = subtaskList.indexOf(subTask);
        if (subTaskIndex >= 0) {
            Duration oldDuration = subtaskList.get(subTaskIndex).getDuration();
            if (!oldDuration.equals(subTask.getDuration())) {
                this.duration = duration.minus(oldDuration).plus(subTask.getDuration());
            }

            subtaskList.set(subTaskIndex, subTask);
        } else {
            this.duration = duration.plus(subTask.getDuration());
            subtaskList.add(subTask);
        }

        if (subTask.getStartTime().isBefore(this.startTime)) {
            this.startTime = subTask.getStartTime();
        }

        if (subTask.getEndTime().isAfter(this.endTime)) {
            this.endTime = subTask.getEndTime();
        }
    }

    public void removeSubTask(SubTask subTask) {
        if (!subtaskList.contains(subTask)) return;
        subtaskList.remove(subTask);

        if (subtaskList.isEmpty()) {
            this.startTime = null;
            this.endTime = null;
            this.duration = null;
            return;
        }

        this.duration = this.duration.minus(subTask.getDuration());
        if (this.startTime.isEqual(subTask.getStartTime())) {
            this.startTime = getSubtaskMinStartTime();
        }

        if (this.endTime.isEqual(subTask.getEndTime())) {
            this.endTime = getSubtaskMaxEndTime();
        }
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

    @Override
    public String toString() {
        String stringTime = this.startTime != null ? this.startTime.toString() : "";
        String stringDuration = this.duration != null ? String.valueOf(this.duration.toMinutes())  : "";
        String typeName = String.valueOf(TaskType.TASK);
        StringBuilder sb = new StringBuilder(super.toString());
        int idx = sb.indexOf(typeName);
        sb.replace(idx, idx + typeName.length(), String.valueOf(TaskType.EPIC))
                .append(stringTime)
                .append(",")
                .append(stringDuration);
        return sb.toString();
    }

    @Override
    public Duration getDuration() {
        return this.duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }
}
