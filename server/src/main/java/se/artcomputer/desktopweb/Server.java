package se.artcomputer.desktopweb;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

public class Server {

    private static final Logger LOG = LogManager.getLogger();

    private static final int PORT = 8080;

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        try {
            new Server().run();
        } catch (IOException e) {
            LOG.log(Level.ERROR, "Problem starting", e);
        }
        LOG.info(String.format("Server started on port %d in %d ms", PORT, (System.currentTimeMillis() - startTime)));
    }

    private void run() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/", rootHandler());
        httpServer.start();
    }

    private HttpHandler rootHandler() {
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
