package entities;

import utils.Status;
import utils.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
    }

    public SubTask(String name, String description, Status status, String startTime, String duration) {
        super(name, description, status, startTime, duration);
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
        String typeName = String.valueOf(TaskType.TASK);
        StringBuilder sb = new StringBuilder(super.toString());
        int idx = sb.indexOf(typeName);
        sb.replace(idx, idx + typeName.length(), String.valueOf(TaskType.SUBTASK));
        sb.append(",").append(epicId);
        return sb.toString();
    }
}
