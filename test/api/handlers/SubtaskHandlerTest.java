package api.handlers;

import api.HttpTaskServer;
import api.adapters.DurationTypeAdapter;
import api.adapters.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import entities.Epic;
import entities.SubTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtasksHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    HttpClient client;
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    public SubtasksHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.removeTasks();
        manager.removeEpics();
        manager.removeEpics();
        taskServer.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @DisplayName("Add subtask")
    @Test
    public void testAddSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.createEpic(epic);

        SubTask subtask = new SubTask("Test 2", "Testing task 2",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "10");
        subtask.setEpicId(0);
        String taskToJson = gson.toJson(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request =
                HttpRequest.newBuilder().uri(url).header("Accept", "*").POST(HttpRequest.BodyPublishers.ofString(taskToJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<SubTask> tasksFromManager = manager.getSubTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(1, tasksFromManager.getFirst().getId(), "Некорректный id задачи");
    }

    @DisplayName("Get all subtasks")
    @Test
    public void testGetSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.createEpic(epic);

        SubTask subtask = new SubTask("Test 2", "Testing task 2",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "10");
        subtask.setEpicId(0);
        manager.createSubtask(subtask);
        List<SubTask> tasksFromManager = manager.getSubTasks();

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonArray tasksJsonArray = gson.fromJson(response.body(), JsonArray.class);
        String firstTaskName = tasksJsonArray.get(0).getAsJsonObject().get("name").getAsString();
        int tasksCount = tasksJsonArray.size();

        assertEquals(200, response.statusCode());

        assertNotNull(tasksJsonArray, "Задачи не возвращаются");
        assertEquals(tasksCount, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", firstTaskName, "Некорректное имя задачи");
    }

    @DisplayName("Get subtask by id")
    @Test
    public void testGetSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.createEpic(epic);

        SubTask subtask = new SubTask("Test 2", "Testing task 2",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "10");
        subtask.setEpicId(0);
        manager.createSubtask(subtask);
        SubTask subtaskFromManager = manager.getSubTask(1);

        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject tasksJson = gson.fromJson(response.body(), JsonObject.class);
        String taskName = tasksJson.get("name").getAsString();

        assertEquals(200, response.statusCode());

        assertNotNull(tasksJson, "Задача не возвращается");
        assertEquals(1, subtaskFromManager.getId(), "Некорректное id задачи");
        assertEquals("Test 2", taskName, "Некорректное имя задачи");
    }

    @DisplayName("Remove subtask")
    @Test
    public void testRemoveSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.createEpic(epic);

        SubTask subtask = new SubTask("Test 2", "Testing task 2",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "10");
        subtask.setEpicId(0);
        manager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getSubTasks().size(), "Задачи не возвращаются");
    }
}