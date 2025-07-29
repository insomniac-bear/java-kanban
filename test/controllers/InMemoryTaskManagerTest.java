package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import exceptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Status;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;
    InMemoryHistoryManager historyManager;

    @BeforeEach
    void beforeEach() throws IOException, ManagerSaveException {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldReturnEpicsList() {
        Epic epic = new Epic("E", "D");
        taskManager.createEpic(epic);
        assertNotNull(taskManager.getEpics(), "Возвращен Null");
    }

    @Test
    void shouldReturnExceptionThanSaveSubtaskWithoutEpicId() {
        SubTask subTask = new SubTask("S", "D", LocalDateTime.now(), Duration.ofMinutes(15));
        assertThrows(ManagerSaveException.class, () -> taskManager.createSubtask(subTask), "Эпик с id 0 не найден");
    }

    @Test
    void shouldReturnTaskList() {
        Task task = new Task("T", "D", LocalDateTime.now(), Duration.ofMinutes(15));
        taskManager.createTask(task);
        assertNotNull(taskManager.getTasks(), "Возвращен Null");
    }

    @Test
    void shouldRemoveAllTasksAndHistory() {
        Task task = new Task("T", "D", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        Task task1 = new Task("T", "D", LocalDateTime.of(2025, 1, 1, 0, 15, 0), Duration.ofMinutes(15));
        Task task2 = new Task("T", "D", LocalDateTime.of(2025, 1, 1, 0, 30, 0), Duration.ofMinutes(15));
        Task task3 = new Task("T", "D", LocalDateTime.of(2025, 1, 1, 0, 45, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        taskManager.createTask(task1);
        taskManager.getTask(task1.getId());
        taskManager.createTask(task2);
        taskManager.getTask(task2.getId());
        taskManager.createTask(task3);
        taskManager.getTask(task3.getId());
        assertEquals(4, taskManager.getTasks().size(), "Количество созданных задач некорректно");
        assertEquals(4, taskManager.getHistory().size(), "В истории не корректное количество задач");
        taskManager.removeTasks();
        assertEquals(0, taskManager.getTasks().size(), "Удалены не все задачи");
        assertEquals(0, taskManager.getHistory().size(), "В истории осталась удаленная задача");
    }

    @Test
    void shouldRemoveAllEpics() {
        Epic epic = new Epic("T", "D");
        Epic epic1 = new Epic("T", "D");
        Epic epic2 = new Epic("T", "D");
        taskManager.createEpic(epic);
        taskManager.getEpic(epic.getId());
        taskManager.createEpic(epic1);
        taskManager.getEpic(epic1.getId());
        taskManager.createEpic(epic2);
        taskManager.getEpic(epic2.getId());
        assertEquals(3, taskManager.getEpics().size(), "Количество созданных эпиков некорректно");
        assertEquals(3, taskManager.getHistory().size(), "В истории не корректное количество эпиков");
        taskManager.removeEpics();
        assertEquals(0, taskManager.getEpics().size(), "Удалены не все эпики");
        assertEquals(0, taskManager.getHistory().size(), "В истории остался удаленный эпик");
    }

    @Test
    void removeSubTasks() {
        Epic epic = new Epic("E", "D");
        Epic epic2 = new Epic("E2", "D2");
        taskManager.createEpic(epic);
        taskManager.getEpic(epic.getId());
        taskManager.createEpic(epic2);
        taskManager.getEpic(epic2.getId());
        SubTask subTask = new SubTask("S", "D", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        subTask.setEpicId(epic.getId());
        SubTask subTask2 = new SubTask("S", "D", LocalDateTime.of(2025, 1, 1, 0, 15, 0), Duration.ofMinutes(15));
        subTask2.setEpicId(epic.getId());
        SubTask subTask3 = new SubTask("S", "D", LocalDateTime.of(2025, 1, 1, 0, 30, 0), Duration.ofMinutes(15));
        subTask3.setEpicId(epic2.getId());
        SubTask subTask4 = new SubTask("S", "D", LocalDateTime.of(2025, 1, 1, 0, 45, 0), Duration.ofMinutes(15));
        subTask4.setEpicId(epic2.getId());
        taskManager.createSubtask(subTask);
        taskManager.getSubTask(subTask.getId());
        taskManager.createSubtask(subTask2);
        taskManager.getSubTask(subTask2.getId());
        taskManager.createSubtask(subTask3);
        taskManager.getSubTask(subTask3.getId());
        taskManager.createSubtask(subTask4);
        taskManager.getSubTask(subTask4.getId());
        assertEquals(4,taskManager.getSubTasks().size(), "Количество созданных подзадач некорректно");
        assertEquals(6, taskManager.getHistory().size(),
                "В историю добавлено некорректное количество Эпиков и Подзадач");
        taskManager.removeSubTasks();
        assertEquals(0, taskManager.getSubTasks().size(), "Удалены не все Подзадачи");
        assertEquals(2, taskManager.getHistory().size(),
                "В истории некорректное количество элементов'");
    }

    @Test
    void shouldReturnCorrectTask() {
        Task task = new Task("T", "D", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        Task task2 = new Task("T2", "D2", LocalDateTime.of(2025, 1, 1, 0, 15, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
        taskManager.createTask(task2);
        assertEquals(task, taskManager.getTask(task.getId()), "Получена некорректная задача");
    }

    @Test
    void shouldReturnCorrectEpic() {
        Epic epic = new Epic("E", "D");
        Epic epic2 = new Epic("E2", "D2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        taskManager.getEpics();
        assertEquals(epic2, taskManager.getEpic(epic2.getId()), "Получен некорректный эпик");
    }

    @Test
    void shouldReturnCorrectSubTask() {
        Epic epic = new Epic("E", "D");
        Epic epic2 = new Epic("E2", "D2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        SubTask subTask = new SubTask("S", "D", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("S2", "D2", LocalDateTime.of(2025, 1, 1, 0, 15, 0), Duration.ofMinutes(15));
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask2);
        assertEquals(subTask2, taskManager.getSubTask(subTask2.getId()), "Получена некорректная подзадача");
    }

    @Test
    void shouldReturnUpdatedTask() {
        Task task = new Task("T", "D", LocalDateTime.now(), Duration.ofMinutes(15));
        taskManager.createTask(task);
        Task updatedTask = new Task("NT", "ND", LocalDateTime.now(), Duration.ofMinutes(15));
        updatedTask.setStatus(Status.IN_PROGRESS);
        updatedTask.setId(task.getId());
        taskManager.updateTask(updatedTask);
        assertEquals(Status.IN_PROGRESS, taskManager.getTask(task.getId()).getStatus(), "Задача не обновлена");
    }

    @Test
    void shouldReturnUpdatedEpicAndNotUpdatedStatus() {
        Epic epic = new Epic("E", "D");
        taskManager.createEpic(epic);
        Epic updatedEpic = new Epic("UE", "UD");
        updatedEpic.setId(epic.getId());
        updatedEpic.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(updatedEpic); ;
        assertNotEquals(Status.IN_PROGRESS, taskManager.getEpic(epic.getId()).getStatus(),
                "Статус эпика был обновлен");
        assertEquals(updatedEpic, taskManager.getEpic(epic.getId()), "Эпик не был обновлен");
    }

    @Test
    void shouldUpdateSubtaskAndUpdateEpicsStatus() {
        Epic epic = new Epic("E", "D");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("S", "D", LocalDateTime.now(), Duration.ofMinutes(15));
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        SubTask updatedSubTask = new SubTask("US", "UD", LocalDateTime.now(), Duration.ofMinutes(15));
        updatedSubTask.setId(subTask.getId());
        updatedSubTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(updatedSubTask);

        assertEquals(updatedSubTask, taskManager.getSubTask(subTask.getId()), "Подзадача не обновлена");
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic.getId()).getStatus(),
                "Статус эпика не изменился");
    }

    @Test
    void removeTask() {
        Task task = new Task("T", "D", LocalDateTime.now(), Duration.ofMinutes(15));
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        assertEquals(1, taskManager.getTasks().size(), "Задача не создана");
        assertEquals(1, taskManager.getHistory().size(), "Задача не добавлена в историю");
        taskManager.removeTask(task.getId());
        assertEquals(0, taskManager.getTasks().size(), "Задача не удалена");
        assertEquals(0, taskManager.getHistory().size(), "Задача не удалена из истории");
    }

    @Test
    void removeEpic() {
        Epic epic = new Epic("E", "D");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("ST", "STD", LocalDateTime.now(), Duration.ofMinutes(15));
        subTask.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        taskManager.getEpic(epic.getId());
        taskManager.getSubTask(subTask.getId());
        assertEquals(1, taskManager.getEpics().size(), "Эпик не создана");
        assertEquals(2, taskManager.getHistory().size(), "Эпик и подзадача не добавлены в историю");
        taskManager.removeEpic(epic.getId());
        assertEquals(0, taskManager.getEpics().size(), "Эпик не удален");
        assertEquals(0, taskManager.getSubTasks().size(), "Подзадача не удалена");
        assertEquals(0, taskManager.getHistory().size(), "Эпик и подзадлача не удалены из истории");
    }

    @Test
    void removeSubtask() {
        Epic epic = new Epic("E", "D");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("S", "D", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        subTask.setEpicId(epic.getId());
        SubTask subTask2 = new SubTask("S2", "D2",LocalDateTime.of(2025, 1, 1, 0, 15, 0), Duration.ofMinutes(15));
        subTask2.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        taskManager.getSubTask(subTask.getId());
        taskManager.createSubtask(subTask2);
        SubTask updatedSubTask = new SubTask("US", "UD", LocalDateTime.now(), Duration.ofMinutes(15));
        updatedSubTask.setId(subTask.getId());
        updatedSubTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(updatedSubTask);

        assertEquals(2, taskManager.getSubTasks().size(), "Подзадачи не созданы");
        assertEquals(1, taskManager.getHistory().size(), "Подзадача не добавлена в историю");
        taskManager.removeSubtask(subTask.getId());
        assertEquals(1, taskManager.getSubTasks().size(), "Подзадача не удалена");
        assertEquals(0, taskManager.getHistory().size(), "Подзадача не удалена из истории");
        assertEquals(Status.NEW, taskManager.getEpic(epic.getId()).getStatus(), "Статус эпика не обновился");
    }

    @Test
    void getEpicSubTasks() {
        Epic epic = new Epic("E", "D");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("S", "D", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("S2", "D2", LocalDateTime.of(2025, 1, 1, 0, 15, 0), Duration.ofMinutes(15));
        subTask.setEpicId(epic.getId());
        subTask2.setEpicId(epic.getId());
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask2);
        ArrayList<SubTask> subTasks = taskManager.getEpicSubTasks(epic.getId());

        assertNotNull(subTasks, "Список подзадач не создан");
        assertEquals(2, subTasks.size(), "Количество подзадач эпика некорректно");
    }

    @Test
    void getHistory() {
        Task task = new Task("T", "D", LocalDateTime.now(), Duration.ofMinutes(15));
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        assertEquals(1, taskManager.getHistory().size(), "История некорректна");
    }
}