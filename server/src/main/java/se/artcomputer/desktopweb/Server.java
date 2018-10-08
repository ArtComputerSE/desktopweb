package se.artcomputer.desktopweb;

import com.sun.net.httpserver.HttpServer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

import static se.artcomputer.desktopweb.RootContext.rootHandler;

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
        showStage(primaryStage, createStageContent());
    }

    private VBox createStageContent() {
        String rootFolder = System.getProperty("user.dir");
        return new VBox(new Label(message), new Label("Location: " + rootFolder), createStopButton());
    }

    private Button createStopButton() {
        Button stopButton = new Button("Stop");
        stopButton.setStyle("-fx-font: 22 arial; -fx-base: #b60000;");
        stopButton.setOnAction(event -> System.exit(0));
        return stopButton;
    }

    private void showStage(Stage primaryStage, Parent content) {
        primaryStage.setScene(new Scene(content, 300, 250));
        primaryStage.setTitle("Desktop web server");
        primaryStage.show();
    }

    private void run() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/", rootHandler());
        httpServer.start();
    }


}
