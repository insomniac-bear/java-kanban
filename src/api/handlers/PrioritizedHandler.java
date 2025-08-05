package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import entities.Task;
import utils.HttpMethod;
import utils.RequestParams;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        RequestParams params = new RequestParams(httpExchange);
        String method = params.getMethod();

        switch (HttpMethod.valueOf(method)) {
            case HttpMethod.GET:
                Set<Task> taskSet = taskManager.getPrioritizedTasks();
                String taskSetJson = gson.toJson(taskSet);
                sendSuccess(httpExchange, taskSetJson);
                break;
            default:
                sendNotAllowed(httpExchange);
        }

    }
}
