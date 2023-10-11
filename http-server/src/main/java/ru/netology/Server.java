package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(64);
    private static ConcurrentMap<String, ConcurrentMap<String, Handler>> handlersMap = new ConcurrentHashMap<>();

    public void serverStart() {
        try (final ServerSocket serverSocket = new ServerSocket(9999)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                threadPool.submit(new MessageProcessor(socket));
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void addHandler(String method, String message, Handler handler) {
        ConcurrentMap<String, Handler> handlerConcurrentMap = new ConcurrentHashMap<>();
        handlerConcurrentMap.put(message, handler);
        if (handlersMap.containsKey(method)) {
            if (handlersMap.get(method).containsKey(message)) {
                System.out.println("Method - " + method + " with handler is in package ./public" + message);
            } else {
                handlersMap.put(method, handlerConcurrentMap);
            }
        } else {
            handlersMap.put(method, handlerConcurrentMap);
        }
    }

    public static Handler getHandler(String method, String path) {
        if (handlersMap.containsKey(method)) {
            if (handlersMap.get(method).containsKey(path)) {
                return handlersMap.get(method).get(path);
            }
        }
        return null;
    }
}

