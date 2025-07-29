package entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import utils.Status;
import utils.TaskType;

public class Task {
    private int id = -1;
    private final String name;
    private Status status;
    private final String description;
    private final Duration duration;
    private final LocalDateTime startTime;

    private Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this(name, description, Status.NEW, startTime, duration);
    }

    public Task(String name, String description, Status status, String startTime, String duration) {
        this(name, description, status, Optional.ofNullable(startTime).map(LocalDateTime::parse).orElse(null),
                Optional.ofNullable(duration).map(Long::parseLong).map(Duration::ofMinutes).orElse(null));
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
        String stringStartTime = startTime != null ? startTime + "," : "";
        String stringDuration = duration != null ? String.valueOf(duration.toMinutes()) : "";

        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + "," + stringStartTime + stringDuration;
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

    public int getId() {
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

}
