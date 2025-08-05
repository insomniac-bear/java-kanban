package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import entities.SubTask;
import exceptions.ManagerGetException;
import exceptions.ManagerSaveException;
import api.handlers.dto.TaskDTO;
import utils.HttpMethod;
import utils.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
                    ArrayList<SubTask> subTasks = taskManager.getSubTasks();
                    String subtasksJson = gson.toJson(subTasks);
                    BaseHttpHandler.sendSuccess(httpExchange, subtasksJson);
                } else {
                    try {
                        SubTask subtask = taskManager.getSubTask(Integer.parseInt(urlParts[2]));
                        String subtaskJson = gson.toJson(subtask);
                        BaseHttpHandler.sendSuccess(httpExchange, subtaskJson);
                    } catch (ManagerGetException e) {
                        BaseHttpHandler.sendNotFound(httpExchange, e.getMessage());
                    }
                }
                break;
            case HttpMethod.POST:
                try {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), UTF_8);
                    TaskDTO rawTask = gson.fromJson(body, TaskDTO.class);
                    SubTask subTask = new SubTask(rawTask.getName(), rawTask.getDescription(), rawTask.getStartTime(),
                            rawTask.getDuration());
                    subTask.setEpicId(rawTask.getEpicId());
                    if (rawTask.getId() != null) {
                        subTask.setId(rawTask.getId());
                        taskManager.updateTask(subTask);
                        sendSuccess(httpExchange, "Подзадача успешно обновлена");
                    } else {
                        taskManager.createSubtask(subTask);
                        sendCreate(httpExchange, "Подзадача успешно создана");
                    }
                } catch (ManagerSaveException e) {
                    sendNotFound(httpExchange, e.getMessage());
                }
                break;
            case HttpMethod.DELETE:
                try {
                    taskManager.removeSubtask(Integer.parseInt(urlParts[2]));
                    sendSuccess(httpExchange, "Подзадача успешно удалена");
                } catch (ManagerGetException e) {
                    sendNotFound(httpExchange, e.getMessage());
                }
                break;
            default:
                sendNotAllowed(httpExchange);
        }
    }
}
