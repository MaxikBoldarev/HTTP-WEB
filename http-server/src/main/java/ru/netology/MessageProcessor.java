package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.util.List;

public class MessageProcessor implements Runnable {
    Socket socket;

    public MessageProcessor(Socket socket) {
        this.socket = socket;
    }

    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    @Override
    public void run() {
        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream());
        ) {

            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                return;
            }

            final String method = parts[0];
            final String path = parts[1];

            Request request = new Request(parts[0], URI.create(path));

            Handler handler = Server.getHandler(method, request.getUri().getPath());
            if (handler != null) {
                handler.handle(request, out);
            }
            out.flush();

            if (!validPaths.contains(request.getUri().getPath())) {
                out.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
