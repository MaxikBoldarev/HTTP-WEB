package ru.netology;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        server.addHandler("GET", "/classic.html", handlerAsClassicHtml());
        server.addHandler("POST", "/spring.png", handlerAsOtherPath());

        server.serverStart();
    }

    public static Handler handlerAsClassicHtml() {
        Handler handler = (request, bos) -> {
            Path filePath = Path.of(".", "public", request.getPath());
            try {
                final String template = Files.readString(filePath);
                final String mimeType = Files.probeContentType(filePath);
                final byte[] content = template.replace(
                        "{time}",
                        LocalDateTime.now().toString()
                ).getBytes();
                bos.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + content.length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                bos.write(content);
                bos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return handler;
    }

    public static Handler handlerAsOtherPath() {
        Handler handler = (request, bos) -> {
            Path filePath = Path.of(".", "public", request.getPath());
            try {
                final String mimeType = Files.probeContentType(filePath);
                final long length = Files.size(filePath);
                bos.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                Files.copy(filePath, bos);
                bos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return handler;
    }
}