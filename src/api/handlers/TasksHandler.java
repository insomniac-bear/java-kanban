package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import entities.Task;
import exceptions.ManagerGetException;
import exceptions.ManagerSaveException;
import utils.HttpMethod;
import utils.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager manager) {
        this.taskManager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        RequestParams params = new RequestParams(httpExchange);
        String method = params.getMethod();
        String[] urlParts = params.getUrlParts();

        switch (HttpMethod.valueOf(method)) {
            case HttpMethod.GET:
                if (urlParts.length < 3) {
                    try {
                        List<Task> tasks = taskManager.getTasks();
                        String tasksJson = gson.toJson(tasks);
                        this.sendSuccess(httpExchange, tasksJson);
                    } catch (ManagerGetException e) {
                        this.sendNotFound(httpExchange, e.getMessage());
                    }
                } else {
                    try {
                        Task task = taskManager.getTask(Integer.parseInt(urlParts[2]));
                        String taskJson = gson.toJson(task);
                        this.sendSuccess(httpExchange, taskJson);
                    } catch (ManagerGetException e) {
                        this.sendNotFound(httpExchange, e.getMessage());
                    }
                }
                break;
            case HttpMethod.POST:
                try {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), this.UTF_8);
                    Task newTask = gson.fromJson(body, Task.class);

                    if (newTask.getId() == null) {
                        taskManager.createTask(newTask);
                        this.sendCreate(httpExchange, "Задача успешно создана");
                    } else {
                        taskManager.updateTask(newTask);
                        this.sendCreate(httpExchange, "Задача успешно обновлена");
                    }
                } catch (ManagerSaveException e) {
                    this.sendHasIntersection(httpExchange);
                }
                break;
            case HttpMethod.DELETE:
                try {
                    taskManager.removeTask(Integer.parseInt(urlParts[2]));
                    this.sendSuccess(httpExchange, "Задача успешно удалена");
                } catch (ManagerGetException e) {
                    this.sendNotFound(httpExchange, e.getMessage());
                }
                break;
            default:
                this.sendNotAllowed(httpExchange);
        }
    }
}
