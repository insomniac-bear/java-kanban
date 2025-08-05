package api.handlers;

import api.adapters.DurationTypeAdapter;
import api.adapters.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    protected final Charset UTF_8 = StandardCharsets.UTF_8;
    protected final String CONTENT_TYPE = "application/json;charset=UTF-8";
    protected final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    protected void sendSuccess(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", CONTENT_TYPE);
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendCreate(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", CONTENT_TYPE);
        exchange.sendResponseHeaders(201, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(UTF_8);
        exchange.sendResponseHeaders(404, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendHasIntersection(HttpExchange exchange) throws IOException {
        byte[] response = "Задача имеет пересечение по времени с другими задачами".getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", CONTENT_TYPE);
        exchange.sendResponseHeaders(406, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotAllowed(HttpExchange exchange) throws IOException {
        byte[] response = "Метод не поддерживается".getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", CONTENT_TYPE);
        exchange.sendResponseHeaders(405, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendInternalServerError(HttpExchange exchange, String errorMsg) throws IOException {
        byte[] response = errorMsg.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", CONTENT_TYPE);
        exchange.sendResponseHeaders(500, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}
