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

class EpicsHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    HttpClient client;
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    public EpicsHandlerTest() throws IOException {
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

    @DisplayName("Add epic")
    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        EpicDTO epic = new EpicDTO();
        epic.setName("epic");
        epic.setDescription("description");
        String taskToJson = gson.toJson(epic);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request =
                HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskToJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = manager.getEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(0, tasksFromManager.getFirst().getId(), "Некорректный id задачи");
    }

    @DisplayName("Get all epics")
    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonArray tasksJsonArray = gson.fromJson(response.body(), JsonArray.class);
        String firstTaskName = tasksJsonArray.get(0).getAsJsonObject().get("name").getAsString();
        int tasksCount = tasksJsonArray.size();

        assertEquals(200, response.statusCode());

        assertNotNull(tasksJsonArray, "Задачи не возвращаются");
        assertEquals(tasksCount, manager.getEpics().size(), "Некорректное количество задач");
        assertEquals("epic", firstTaskName, "Некорректное имя задачи");
    }

    @DisplayName("Get epic by id")
    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject tasksJson = gson.fromJson(response.body(), JsonObject.class);
        String taskName = tasksJson.get("name").getAsString();

        assertEquals(200, response.statusCode());

        assertNotNull(tasksJson, "Задача не возвращается");
        assertEquals(0, manager.getEpic(0).getId(), "Некорректное id задачи");
        assertEquals("epic", taskName, "Некорректное имя задачи");
    }

    @DisplayName("Remove epic")
    @Test
    public void testRemoveEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getSubTasks().size(), "Задача не удалена");
    }

    @DisplayName("Get epics subtasks")
    @Test
    public void testGetEpicsSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.createEpic(epic);

        SubTask subtask = new SubTask("Test 2", "Testing task 2",
                Status.NEW, LocalDateTime.of(2025, 7, 1, 10, 0, 0).toString(), "10");
        subtask.setEpicId(0);
        manager.createSubtask(subtask);
        List<SubTask> tasksFromManager = manager.getEpicSubTasks(0);

        URI url = URI.create("http://localhost:8080/epics/0/subtasks");
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
}

class EpicDTO {
    String name;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}