package se.artcomputer.desktopweb;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

public class Server extends Application {

    private static final Logger LOG = LogManager.getLogger();

    private static final int PORT = 8080;

    private String message = "The server started at http://localhost:" + PORT;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        long startTime = System.currentTimeMillis();
        try {
            new Server().run();
        } catch (IOException e) {
            LOG.log(Level.ERROR, "Problem starting", e);
            message = "That did not go well: " + e.getMessage();
        }
        LOG.info(String.format("Server started on port %d in %d ms", PORT, (System.currentTimeMillis() - startTime)));
    }

    @Override
    public void start(Stage primaryStage) {
        Button stopButton = new Button("Stop");
        stopButton.setStyle("-fx-font: 22 arial; -fx-base: #b60000;");
        stopButton.setOnAction(event -> System.exit(0));
        Label label = new Label(message);
        VBox vBox = new VBox(label, stopButton);
        primaryStage.setScene(new Scene(vBox, 300, 250));
        primaryStage.setTitle("Desktop web server");
        primaryStage.show();
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
