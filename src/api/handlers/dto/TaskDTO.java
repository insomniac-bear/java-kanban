package api.handlers.dto;

import entities.SubTask;
import utils.Status;
import utils.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskDTO {
    private TaskType taskType;
    private Integer id;
    private Integer epicId;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;
    private ArrayList<SubTask> subtaskList;

    public ArrayList<SubTask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(ArrayList<SubTask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }
}
