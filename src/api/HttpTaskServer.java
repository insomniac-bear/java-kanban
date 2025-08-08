package api;

import api.handlers.*;
import com.sun.net.httpserver.HttpServer;
import controllers.Managers;
import controllers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpServer = new HttpTaskServer(taskManager);
        httpServer.start();
    }


    public void start() {
        System.out.println("HTTP Task Server started on port " + PORT);
        httpServer.start();
    }

    public void stop() {
        System.out.println("Stopping HTTP Task Server...");
        httpServer.stop(0);
    }
}
