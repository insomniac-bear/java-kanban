package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    static private File data;
    FileBackedTaskManager manager;

    @BeforeEach
    void beforeEach() throws IOException {
        data = File.createTempFile("data", "csv", null);
        manager = Managers.getDefaultFileBackedTaskManager(data);
    }

    @Test
    @DisplayName("Загрузка данных из пустого файла")
    void shouldLoadEmptyFile() throws IOException {
        ArrayList<Task> tasks = manager.getTasks();
        ArrayList<Epic> epics = manager.getEpics();
        ArrayList<SubTask> subTasks = manager.getSubTasks();

        assertTrue(tasks.isEmpty(), "Список задач не пустой");
        assertTrue(epics.isEmpty(), "Список эпиков не пустой");
        assertTrue(subTasks.isEmpty(), "Список подзадач не пустой");
    }

    @Test
    @DisplayName("Корректное сохранение Task")
    void shouldSaveTask() throws IOException {
        Task task = new Task("T", "D");
        manager.createTask(task);
        Task savedTask = manager.getTask(task.getId());

        FileBackedTaskManager newManager = Managers.getDefaultFileBackedTaskManager(data);
        Task loadedTask = newManager.getTask(task.getId());

        assertEquals(savedTask, loadedTask, "Сохраненная задача не эквивалентна полученной");
    }


    @Test
    @DisplayName("Корректное сохранение Epic и Subtask")
    void shouldSaveEpicAndSubtask() throws IOException {
        Epic epic = new Epic("E", "D");
        manager.createEpic(epic);
        SubTask subTask = new SubTask("ST", "D");
        subTask.setEpicId(epic.getId());
        manager.createSubtask(subTask);
        Epic savedEpic = manager.getEpic(0);
        SubTask savedSubtask = manager.getSubTask(subTask.getId());

        FileBackedTaskManager newManager = Managers.getDefaultFileBackedTaskManager(data);
        Epic loadedEpic = newManager.getEpic(epic.getId());
        SubTask loadedSubtask = newManager.getSubTask(subTask.getId());

        assertEquals(savedEpic, loadedEpic, "Сохраненный эпик не эквивалентен полученному");
        assertNotNull(newManager.getEpicSubTasks(savedEpic.getId()), "Сохраненный эпик не содержит подзадач");
        assertEquals(savedSubtask, loadedSubtask, "Сохраненная подзадача не эквивалентна полученной");
    }
}