package api.handlers;

import api.handlers.dto.TaskDTO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import entities.Epic;
import entities.SubTask;
import exceptions.ManagerGetException;
import exceptions.ManagerSaveException;
import utils.HttpMethod;
import utils.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        RequestParams params = new RequestParams(httpExchange);
        String method = params.getMethod();
        String[] urlParts = params.getUrlParts();

        switch (HttpMethod.valueOf(method)) {
            case HttpMethod.GET:
                if (urlParts.length < 3) {
                    ArrayList<Epic> epics = taskManager.getEpics();
                    String epicsJson = gson.toJson(epics);
                    this.sendSuccess(httpExchange, epicsJson);
                } else if (urlParts.length < 4) {
                    try {
                        Epic epic = taskManager.getEpic(Integer.parseInt(urlParts[2]));
                        String epicsJson = gson.toJson(epic);
                        this.sendSuccess(httpExchange, epicsJson);
                    } catch (ManagerGetException e) {
                        this.sendNotFound(httpExchange, e.getMessage());
                    }
                } else {
                    try {
                        ArrayList<SubTask> subtaskList = taskManager.getEpicSubTasks(Integer.parseInt(urlParts[2]));
                        String subtaskListJson = gson.toJson(subtaskList);
                        this.sendSuccess(httpExchange, subtaskListJson);
                    } catch (ManagerGetException e) {
                        this.sendNotFound(httpExchange, e.getMessage());
                    }
                }
                break;
            case HttpMethod.POST:
                try {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), this.UTF_8);
                    TaskDTO rawTask = gson.fromJson(body, TaskDTO.class);
                    Epic epic = new Epic(rawTask.getName(), rawTask.getDescription());
                    if (rawTask.getId() != null) {
                        epic.setId(rawTask.getId());
                        taskManager.updateTask(epic);
                        this.sendSuccess(httpExchange, "Эпик успешно обновлен");
                    } else {
                        taskManager.createEpic(epic);
                        this.sendCreate(httpExchange, "Эпик успешно создан");
                    }
                } catch (ManagerSaveException e) {
                    this.sendNotFound(httpExchange, e.getMessage());
                }
                break;
            case HttpMethod.DELETE:
                try {
                    taskManager.removeEpic(Integer.parseInt(urlParts[2]));
                    this.sendSuccess(httpExchange, "Эпик успешно удален");
                } catch (ManagerGetException e) {
                    this.sendNotFound(httpExchange, e.getMessage());
                }
                break;
            default:
                this.sendNotAllowed(httpExchange);
        }
    }
}
