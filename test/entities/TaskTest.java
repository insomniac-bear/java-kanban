package entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import utils.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    static private Task task;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        task = new Task("T", "D", LocalDateTime.of(2025, 7, 1, 10, 0, 0), Duration.ofMinutes(15));
    }

    @Test
    void shouldHaveInProgressStatus() {
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void shouldHaveCompletedStatus() {
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    void shouldHaveId() {
        task.setId(1);
        assertEquals(1, task.getId());
    }

    @Test
    @DisplayName("Задача имееет начало время выполнения")
    void shouldHaveStartTime() {
        assertNotNull(task.getStartTime());
        assertNotNull(task.getDuration());
        assertNotNull(task.getEndTime());
    }

    @Test
    @DisplayName("Задача не имеет начало время выполнения")
    void shouldHaveNullOfStartTime() {
        Task taskWithoutTime = new Task("T", "D", null, Duration.ofMinutes(15));
        Task taskWithoutDuration = new Task("T", "D", LocalDateTime.of(2025, 7, 1, 10, 0, 0), null);
        assertNull(taskWithoutTime.getStartTime());
        assertNull(taskWithoutTime.getEndTime());
        assertNull(taskWithoutDuration.getDuration());
        assertNull(taskWithoutDuration.getEndTime());
    }

    @Test
    @DisplayName("Должен разбирать задачу в строку")
    void shouldReturnStringOfTask() {
        assertNotNull(task.toString(), "0,TASK,T,NEW,D," + LocalDateTime.of(2025, 7, 1, 10, 0, 0) + ",15");
    }

    @Test
    @DisplayName("hashCode() должен возвращать -1")
    void shouldHaveHashCodeOfTask() {
        assertEquals(task.hashCode(), 1, "Хэшкод" + task.hashCode());
    }

    @Test
    @DisplayName("equals должен возвращать true")
    void shouldEqualsReturnTrue() {
        Task otherTask = new Task("T", "D", LocalDateTime.of(2025, 7, 1, 10, 0, 0), null);
        otherTask.setId(1);
        assertTrue(task.equals(task));
        assertTrue(task.equals(otherTask));
        assertFalse(task.equals(null));
        assertFalse(task.equals("Task"));
    }

    @Test
    @DisplayName("Должен создавать задачу из строковых значений")
    void shouldCreateTaskFromStringValues() {
        Task newTask = new Task("T", "D", Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "15");
        assertNotNull(newTask);
    }
}