package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import exceptions.ManagerGetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class InMemoryHistoryManagerTest {
    private static InMemoryTaskManager taskManager;
    private static InMemoryHistoryManager historyManager;
    private static Task task;
    private static SubTask subTask;
    private static Epic epic;

    @BeforeEach
    void beforeEach() throws Exception {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
        epic = new Epic("Epic", "Description");
        task = new Task("Task", "Description", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        subTask = new SubTask("Subtask", "Description", LocalDateTime.of(2025, 1, 1, 0, 15, 0), Duration.ofMinutes(15));
    }

    @Test
    void shouldAddTaskInHistory() throws ManagerGetException {
        taskManager.createTask(task);
        taskManager.getTask(0);
        List<Task> history = taskManager.getHistory();
        assertNotNull(history, "История не создана");
        assertEquals(1, history.size(), "Количество задач в истории не совпадает с ожидаемым");
        assertEquals(task, history.getFirst(), "Задача сохранена не на свое место");
    }

    @Test
    void shouldHaveOnlyOneUniqueTaskInHistory() throws ManagerGetException {
        taskManager.createTask(task);
        for (int i = 0; i < 3; i++) {
            taskManager.getTask(0);
        }
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size(), "Количество задач в истории не совпадает с ожидаемым");
    }

    @Test
    void shouldHaveTwoTasksInHistory() throws ManagerGetException {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        taskManager.getTask(0);
        taskManager.getSubTask(2);
        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "Количество задач в истории не совпадает с ожидаемым");
    }

    @Test
    void shouldHaveOneTasksInHistoryAfterDeleteFirstHistoryItem() throws ManagerGetException {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        taskManager.getTask(0);
        taskManager.getSubTask(2);
        List<Task> history = taskManager.getHistory();
        historyManager.remove(0);
        assertEquals(2, history.size(), "Количество задач в истории не совпадает с ожидаемым");
    }
}