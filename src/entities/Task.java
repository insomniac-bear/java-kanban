package entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import utils.Status;
import utils.TaskType;

public class Task {
    private Integer id = null;
    private final String name;
    private Status status;
    private final String description;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status  = Status.NEW;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        this.status  = Status.NEW;
    }

    public Task(String name, String description, Status status, String startTime, String duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime != null ? LocalDateTime.parse(startTime) : null;
        this.duration = duration != null ? Duration.ofMinutes(Long.parseLong(duration)) : null;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
            return this.startTime;
    }

    public LocalDateTime getEndTime() {
        if (this.startTime == null || this.duration == null) {
            return null;
        }
        return this.startTime.plus(this.duration);

    }

    public Duration getDuration() {
        return this.duration;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        String stringStartTime = startTime != null ? startTime + "," : "null,";
        String stringDuration = duration != null ? String.valueOf(duration.toMinutes()) : "null";

        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + "," + stringStartTime + stringDuration;
    }
}
