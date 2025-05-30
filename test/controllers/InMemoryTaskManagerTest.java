package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Status;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldReturnEpicsList() {
        Epic epic = new Epic("E", "D");
        taskManager.createTask(epic);
        assertNotNull(taskManager.getEpics(), "Возвращен Null");
    }

    @Test
    void shouldReturnSubTaskList() {
        Epic epic = new Epic("E", "D");
        SubTask subTask = new SubTask("S", "D");
        taskManager.createTask(subTask);
        assertNotNull(taskManager.getSubTasks(), "Возвращен Null");
    }

    @Test
    void shouldReturnTaskList() {
        Task task = new Task("T", "D");
        taskManager.createTask(task);
        assertNotNull(taskManager.getTasks(), "Возвращен Null");
    }

    @Test
    void shouldRemoveAllTasks() {
        Task task = new Task("T", "D");
        taskManager.createTask(task);
        taskManager.createTask(task);
        taskManager.createTask(task);
        taskManager.createTask(task);
        assertEquals(4, taskManager.getTasks().size(), "Количество созданных задач некорректно");
        taskManager.removeTasks();
        assertEquals(0, taskManager.getTasks().size(), "Удалены не все задачи");
    }

    @Test
    void shouldRemoveAllEpics() {
        Epic epic = new Epic("T", "D");
        taskManager.createTask(epic);
        taskManager.createTask(epic);
        taskManager.createTask(epic);
        taskManager.createTask(epic);
        assertEquals(4, taskManager.getEpics().size(), "Количество созданных эпиков некорректно");
        taskManager.removeEpics();
        assertEquals(0, taskManager.getEpics().size(), "Удалены не все эпики");
    }

    @Test
    void removeSubTasks() {
        Epic epic = new Epic("E", "D");
        Epic epic2 = new Epic("E2", "D2");
        taskManager.createTask(epic);
        taskManager.createTask(epic2);
        SubTask subTask = new SubTask("S", "D");
        subTask.setEpicId(epic.getId());
        SubTask subTask2 = new SubTask("S", "D");
        subTask2.setEpicId(epic.getId());
        SubTask subTask3 = new SubTask("S", "D");
        subTask3.setEpicId(epic2.getId());
        SubTask subTask4 = new SubTask("S", "D");
        subTask4.setEpicId(epic2.getId());
        taskManager.createTask(subTask);
        taskManager.createTask(subTask2);
        taskManager.createTask(subTask3);
        taskManager.createTask(subTask4);
        assertEquals(4,taskManager.getSubTasks().size(), "Количество созданных подзадач некорректно");
        taskManager.removeSubTasks();
        assertEquals(0, taskManager.getSubTasks().size(), "Удалены не все Подзадачи");
    }

    @Test
    void shouldReturnCorrectTask() {
        Task task = new Task("T", "D");
        Task task2 = new Task("T2", "D2");
        taskManager.createTask(task);
        taskManager.createTask(task2);
        assertEquals(task, taskManager.getTask(task.getId()), "Получена некорректная задача");
    }

    @Test
    void shouldReturnCorrectEpic() {
        Epic epic = new Epic("E", "D");
        Epic epic2 = new Epic("E2", "D2");
        taskManager.createTask(epic);
        taskManager.createTask(epic2);
        assertEquals(epic2, taskManager.getEpic(epic2.getId()), "Получен некорректный эпик");
    }

    @Test
    void shouldReturnCorrectSubTask() {
        Epic epic = new Epic("E", "D");
        Epic epic2 = new Epic("E2", "D2");
        taskManager.createTask(epic);
        taskManager.createTask(epic2);
        SubTask subTask = new SubTask("S", "D");
        SubTask subTask2 = new SubTask("S2", "D2");
        taskManager.createTask(subTask);
        taskManager.createTask(subTask2);
        assertEquals(subTask2, taskManager.getSubTask(subTask2.getId()), "Получена некорректная подзадача");
    }

    @Test
    void shouldReturnUpdatedTask() {
        Task task = new Task("T", "D");
        taskManager.createTask(task);
        Task updatedTask = new Task("NT", "ND");
        updatedTask.setStatus(Status.IN_PROGRESS);
        updatedTask.setId(task.getId());
        taskManager.updateTask(updatedTask);
        assertEquals(Status.IN_PROGRESS, taskManager.getTask(task.getId()).getStatus(), "Задача не обновлена");
    }

    @Test
    void shouldReturnUpdatedEpicAndNotUpdatedStatus() {
        Epic epic = new Epic("E", "D");
        taskManager.createTask(epic);
        Epic updatedEpic = new Epic("UE", "UD");
        updatedEpic.setId(epic.getId());
        updatedEpic.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(updatedEpic); ;
        assertNotEquals(Status.IN_PROGRESS, taskManager.getEpic(epic.getId()).getStatus(), "Статус эпика был обновлен");
        assertEquals(updatedEpic, taskManager.getEpic(epic.getId()), "Эпик не был обновлен");
    }

    @Test
    void shouldUpdateSubtaskAndUpdateEpicsStatus() {
        Epic epic = new Epic("E", "D");
        taskManager.createTask(epic);
        SubTask subTask = new SubTask("S", "D");
        subTask.setEpicId(epic.getId());
        taskManager.createTask(subTask);
        SubTask updatedSubTask = new SubTask("US", "UD");
        updatedSubTask.setId(subTask.getId());
        updatedSubTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(updatedSubTask);

        assertEquals(updatedSubTask, taskManager.getSubTask(subTask.getId()), "Подзадача не обновлена");
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic.getId()).getStatus(), "Статус эпика не изменился");
    }

    @Test
    void removeTask() {
        Task task = new Task("T", "D");
        taskManager.createTask(task);
        assertEquals(1, taskManager.getTasks().size(), "Задача не создана");
        taskManager.removeTask(task.getId());
        assertEquals(0, taskManager.getTasks().size(), "Задача не удалена");
    }

    @Test
    void removeEpic() {
        Epic epic = new Epic("E", "D");
        taskManager.createTask(epic);
        assertEquals(1, taskManager.getEpics().size(), "Эпик не создана");
        taskManager.removeEpic(epic.getId());
        assertEquals(0, taskManager.getEpics().size(), "Задача не удалена");
    }

    @Test
    void removeSubtask() {
        Epic epic = new Epic("E", "D");
        taskManager.createTask(epic);
        SubTask subTask = new SubTask("S", "D");
        subTask.setEpicId(epic.getId());
        SubTask subTask2 = new SubTask("S2", "D2");
        subTask2.setEpicId(epic.getId());
        taskManager.createTask(subTask);
        taskManager.createTask(subTask2);
        SubTask updatedSubTask = new SubTask("US", "UD");
        updatedSubTask.setId(subTask.getId());
        updatedSubTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(updatedSubTask);

        assertEquals(2, taskManager.getSubTasks().size(), "Подзадачи не созданы");
        taskManager.removeSubtask(subTask.getId());
        assertEquals(1, taskManager.getSubTasks().size(), "Подзадача не удалена");
        assertEquals(Status.NEW, taskManager.getEpic(epic.getId()).getStatus(), "Статус эпика не обновился");
    }

    @Test
    void getEpicSubTasks() {
        Epic epic = new Epic("E", "D");
        taskManager.createTask(epic);
        SubTask subTask = new SubTask("S", "D");
        SubTask subTask2 = new SubTask("S2", "D2");
        subTask.setEpicId(epic.getId());
        subTask2.setEpicId(epic.getId());
        taskManager.createTask(subTask);
        taskManager.createTask(subTask2);
        ArrayList<SubTask> subTasks = taskManager.getEpicSubTasks(epic.getId());

        assertNotNull(subTasks, "Список подзадач не создан");
        assertEquals(2, subTasks.size(), "Количество подзадач эпика некорректно");
    }

    @Test
    void getHistory() {
        Task task = new Task("T", "D");
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        assertEquals(1, taskManager.getHistory().size(), "История некорректна");
    }
}