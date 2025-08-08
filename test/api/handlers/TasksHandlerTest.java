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
import entities.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

class TasksHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    HttpClient client;
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    public TasksHandlerTest() throws IOException {
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

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "10");
        String taskToJson = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request =
                HttpRequest.newBuilder().uri(url).header("Accept", "*").POST(HttpRequest.BodyPublishers.ofString(taskToJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(0, tasksFromManager.get(0).getId(), "Некорректный id задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 0", "Testing task 0",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "10");
        Task task1 = new Task("Test 1", "Testing task 1",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 10, 0).toString(), "10");
        Task task2 = new Task("Test 2", "Testing task 2",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 20, 0).toString(), "10");
        manager.createTask(task);
        manager.createTask(task1);
        manager.createTask(task2);
        List<Task> tasksFromManager = manager.getTasks();

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonArray tasksJsonArray = gson.fromJson(response.body(), JsonArray.class);
        String firstTaskName = tasksJsonArray.get(0).getAsJsonObject().get("name").getAsString();
        int tasksCount = tasksJsonArray.size();

        assertEquals(200, response.statusCode());

        assertNotNull(tasksJsonArray, "Задачи не возвращаются");
        assertEquals(tasksCount, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 0", firstTaskName, "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Test 0", "Testing task 0",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "10");
        Task task1 = new Task("Test 1", "Testing task 1",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 10, 0).toString(), "10");
        Task task2 = new Task("Test 2", "Testing task 2",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 20, 0).toString(), "10");
        manager.createTask(task);
        manager.createTask(task1);
        manager.createTask(task2);
        List<Task> tasksFromManager = manager.getTasks();

        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject tasksJson = gson.fromJson(response.body(), JsonObject.class);
        String taskName = tasksJson.get("name").getAsString();

        assertEquals(200, response.statusCode());

        assertNotNull(tasksJson, "Задача не возвращается");
        assertEquals("Test 1", taskName, "Некорректное имя задачи");

        URI urlHistory = URI.create("http://localhost:8080/history");
        HttpRequest historyRequest = HttpRequest.newBuilder().uri(urlHistory).GET().build();
        HttpResponse<String> historyResponse = client.send(historyRequest, HttpResponse.BodyHandlers.ofString());

        JsonArray history = gson.fromJson(historyResponse.body(), JsonArray.class);

        assertNotNull(history, "История не возвращается");
        assertEquals(1, history.size());

    }

    @Test
    public void testRemoveTask() throws IOException, InterruptedException {
        Task task = new Task("Test 0", "Testing task 0",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "10");
        manager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getTasks().size(), "Задачи не возвращаются");
    }
}