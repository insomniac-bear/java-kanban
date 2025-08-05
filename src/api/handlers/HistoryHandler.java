package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import entities.Task;
import utils.HttpMethod;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        switch (HttpMethod.valueOf(method)) {
            case HttpMethod.GET:
                List<Task> history = taskManager.getHistory();
                String historyJson = gson.toJson(history);
                BaseHttpHandler.sendSuccess(httpExchange, historyJson);
                break;
            default:
                BaseHttpHandler.sendNotAllowed(httpExchange);
        }
    }
}
