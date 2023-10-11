package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(64);

    public void serverStart() {
        try (final ServerSocket serverSocket = new ServerSocket(9999)) {
            while (true) {
                try (
                        final Socket socket = serverSocket.accept()
                ) {
                    threadPool.submit(new MessageProcessor(socket));
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}

