package entities;

import utils.Status;
import utils.TaskType;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
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
        sb.append(epicId);
        return sb.toString();
    }
}
