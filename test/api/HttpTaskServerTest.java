package api;

import controllers.Managers;
import controllers.TaskManager;
import entities.Epic;
import entities.SubTask;
import entities.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    protected TaskManager taskManager = Managers.getDefault();
    protected HttpTaskServer server = new HttpTaskServer(taskManager);
    protected HttpClient client;
    protected Task task1;
    protected Epic epic1;
    protected SubTask subtask1;

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    protected void beforeEach() {
        server.start();
        client = HttpClient.newHttpClient();

        task1 = new Task("T", "D", LocalDateTime.of(2025, 1, 1, 0, 0), Duration.ofMinutes(10));
        taskManager.createTask(task1);
        epic1 = new Epic("E", "D");
        taskManager.createEpic(epic1);
        subtask1 = new SubTask("ST", "D",
                LocalDateTime.of(2025, 1, 2, 0, 0), Duration.ofHours(1));
        subtask1.setEpicId(epic1.getId());
        taskManager.createSubtask(subtask1);
    }

    @AfterEach
    protected void afterEach() {
        client.close();
        server.stop();
    }

    @Test
    public void checkRunningServer() {
        try {
            URI uri = URI.create("http://localhost:8080/");
            HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}