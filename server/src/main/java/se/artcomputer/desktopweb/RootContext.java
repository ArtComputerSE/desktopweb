package se.artcomputer.desktopweb;

import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

class RootContext {

    private static final String INDEX_HTML = "index.html";
    private String rootFolderName;

    RootContext(String rootFolderName) throws IOException {
        this.rootFolderName = rootFolderName;
        ensureRootHasIndexFile();
    }

    HttpHandler handler() {
        return http -> {
            http.getResponseHeaders().add("Content-type", "text/html");
            http.sendResponseHeaders(200, 0);
            returnRootContent(http.getResponseBody());
        };
    }

    private void returnRootContent(OutputStream stream) throws IOException {
        Path rootPath = Paths.get(rootFolderName, INDEX_HTML);
        Files.copy(rootPath, stream);
        stream.close();
    }

    private void ensureRootHasIndexFile() throws IOException {
        File rootFolder = new File(rootFolderName);
        String[] list = rootFolder.list((dir, name) -> name.equals(INDEX_HTML));
        if (list != null && list.length < 1) {
            createDefaultIndexFile();
        }

    }

    private void createDefaultIndexFile() throws IOException {
        List<String> lines = Arrays.asList(
                "<html><body>",
                "Default",
                "</body></html>"
        );
        Path file = Paths.get(rootFolderName, INDEX_HTML);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }
}
