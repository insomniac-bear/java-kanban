package utils;

import com.sun.net.httpserver.HttpExchange;

public class RequestParams {
    private final String method;
    private final String[] urlParts;

    public RequestParams(HttpExchange httpExchange) {
        this.method = httpExchange.getRequestMethod();
        this.urlParts = httpExchange.getRequestURI().toString().split("/");
    }

    public String[] getUrlParts() {
        return urlParts;
    }

    public String getMethod() {
        return method;
    }
}
