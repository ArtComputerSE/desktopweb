package se.artcomputer.desktopweb;

import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.io.PrintWriter;

class RootContext {

    private RootContext() {
        // Utility class
    }

    static HttpHandler rootHandler() {
        return http -> {
            http.getResponseHeaders().add("Content-type", "text/html");
            http.sendResponseHeaders(200, 0);
            OutputStream stream = http.getResponseBody();
            PrintWriter printWriter = new PrintWriter(stream);
            printWriter.write(String.format("<h1>Hello %s</h1>", http.getRemoteAddress().getHostName()));
            printWriter.close();
        };
    }
}
