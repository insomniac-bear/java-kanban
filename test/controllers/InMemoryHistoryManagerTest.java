package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static private InMemoryTaskManager taskManager;
    static private Task task;

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
        Epic epic = new Epic("Epic", "Description");
        task = new Task("Task", "Description");
        SubTask subTask = new SubTask("Subtask", "Description");
        taskManager.createTask(task);
        taskManager.createTask(epic);
        taskManager.createTask(subTask, epic.getId());
    }

    @Test
    void shouldAddTaskInHistory() {
        taskManager.getTask(0);
        ArrayList<Task> history = taskManager.getHistory();
        assertNotNull(history, "История не создана");
        assertEquals(1, history.size(), "Количество задач в истории не совпадает с ожидаемым");
        assertEquals(task, history.getFirst(), "Задача сохранена не на свое место");
    }

    @Test
    void shouldHaveOnlyTenTasksInHistory() {
        for (int i = 0; i < 11; i++) {
            taskManager.getTask(0);
        }
        ArrayList<Task> history = taskManager.getHistory();
        assertEquals(10, history.size(), "Количество задач в истории не совпадает с ожидаемым");
    }
}